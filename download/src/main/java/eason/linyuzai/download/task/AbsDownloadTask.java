package eason.linyuzai.download.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eason.linyuzai.download.database.DatabaseManager;
import eason.linyuzai.download.entity.DownloadTaskEntity;
import eason.linyuzai.download.file.RandomAccessChannelFileProcessor;
import eason.linyuzai.download.file.FileProcessor;
import eason.linyuzai.download.header.RangeHeader;
import eason.linyuzai.download.recycle.TaskRecycler;

import static eason.linyuzai.download.entity.DownloadTaskEntity.STATE_ATTACH;
import static eason.linyuzai.download.entity.DownloadTaskEntity.STATE_CANCEL;
import static eason.linyuzai.download.entity.DownloadTaskEntity.STATE_ERROR;
import static eason.linyuzai.download.entity.DownloadTaskEntity.STATE_FINISH;
import static eason.linyuzai.download.entity.DownloadTaskEntity.STATE_IDLE;
import static eason.linyuzai.download.entity.DownloadTaskEntity.STATE_PAUSE;
import static eason.linyuzai.download.entity.DownloadTaskEntity.STATE_RUNNING;

/**
 * 下载任务
 */
abstract class AbsDownloadTask implements DownloadTask {

    private DownloadTaskEntity entity;
    private long downloadBytesTemp;
    private DatabaseManager databaseManager;
    private List<DownloadListener> downloadListeners;//下载监听
    private List<DownloadTaskListener> downloadTaskListeners;//下载任务监听
    private FileProcessor fileProcessor;
    private TaskRecycler taskRecycler;//任务回收器

    @Override
    public DownloadTaskEntity getEntity() {
        return entity;
    }

    public void setEntity(DownloadTaskEntity entity) {
        this.entity = entity;
    }

    @Override
    public long getDownloadBytesTemp() {
        return downloadBytesTemp;
    }

    public void setDownloadBytesTemp(long downloadBytesTemp) {
        this.downloadBytesTemp = downloadBytesTemp;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void addDownloadListener(DownloadListener downloadListener) {
        if (downloadListener == null)
            return;
        if (downloadListeners == null)
            downloadListeners = new ArrayList<>();
        downloadListeners.add(downloadListener);
    }

    /**
     * 获得下载监听
     *
     * @return 下载监听
     */
    @Override
    public List<DownloadListener> getDownloadListeners() {
        return downloadListeners;
    }

    @Override
    public void addDownloadTaskListener(DownloadTaskListener downloadTaskListener) {
        if (downloadTaskListener == null)
            return;
        if (downloadTaskListeners == null)
            downloadTaskListeners = new ArrayList<>();
        downloadTaskListeners.add(downloadTaskListener);
    }

    /**
     * 获得下载任务监听
     *
     * @return 下载任务监听
     */
    @Override
    public List<DownloadTaskListener> getDownloadTaskListeners() {
        return downloadTaskListeners;
    }

    /**
     * 获得任务回收器
     *
     * @return 任务回收器
     */
    @Override
    public TaskRecycler getTaskRecycler() {
        return taskRecycler;
    }

    /**
     * 设置任务回收器
     *
     * @param taskRecycler 任务回收器
     */
    public void setTaskRecycler(TaskRecycler taskRecycler) {
        this.taskRecycler = taskRecycler;
    }

    @Override
    public FileProcessor getFileProcessor() {
        return fileProcessor;
    }

    public void setFileProcessor(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Override
    public boolean insert() {
        return getDatabaseManager().createDownloadTask(entity);
    }

    @Override
    public boolean update() {
        return getDatabaseManager().updateDownloadTask(entity);
    }

    @Override
    public boolean delete() {
        return getDatabaseManager().deleteDownloadTask(entity);
    }

    @Override
    public Builder newBuilder() {
        return new Builder().setEntity(entity);
    }

    /**
     * 是否是空闲状态
     *
     * @return 是否是空闲状态
     */
    @Override
    public boolean isIdle() {
        return getEntity().getState() == STATE_IDLE;
    }

    /**
     * 设为空闲状态
     */
    void toIdle() {
        getEntity().setState(STATE_IDLE);
    }

    /**
     * 是否是准备状态
     *
     * @return 是否是准备状态
     */
    @Override
    public boolean isAttach() {
        return getEntity().getState() == STATE_ATTACH;
    }

    /**
     * 设为准备状态
     */
    void toAttach() {
        getEntity().setState(STATE_ATTACH);
    }

    /**
     * 是否是下载状态
     *
     * @return 是否是下载状态
     */
    @Override
    public boolean isRunning() {
        return getEntity().getState() == STATE_RUNNING;
    }

    /**
     * 设为下载状态
     */
    void toRunning() {
        getEntity().setState(STATE_RUNNING);
    }

    /**
     * 是否是暂停状态
     *
     * @return 是否是暂停状态
     */
    @Override
    public boolean isPause() {
        return getEntity().getState() == STATE_PAUSE;
    }

    /**
     * 设为暂停状态
     */
    void toPause() {
        getEntity().setState(STATE_PAUSE);
    }

    /**
     * 是否完成
     *
     * @return 是否完成
     */
    @Override
    public boolean isFinish() {
        return getEntity().getState() == STATE_FINISH;
    }

    /**
     * 设为完成
     */
    void toFinish() {
        getEntity().setState(STATE_FINISH);
    }

    /**
     * 是否异常
     *
     * @return 是否异常
     */
    @Override
    public boolean isError() {
        return getEntity().getState() == STATE_ERROR;
    }

    /**
     * 设为异常
     */
    void toError() {
        getEntity().setState(STATE_ERROR);
    }

    /**
     * 是否取消
     *
     * @return 是否取消
     */
    @Override
    public boolean isCancel() {
        return getEntity().getState() == STATE_CANCEL;
    }

    /**
     * 设为取消
     */
    void toCancel() {
        getEntity().setState(STATE_CANCEL);
    }


    /**
     * 设置下载信息（OkHttp,Retrofit,任务回收器除外）
     *
     * @param builder builder
     */
    void fromBuilder(Builder builder) {
        this.downloadListeners = builder.downloadListeners;
        this.downloadTaskListeners = builder.downloadTaskListeners;
        if (this.entity.getUrl() == null)
            throw new NullPointerException("Url is Null");
        if (this.entity.getFilePath() == null)
            throw new NullPointerException("File path is Null");
        if (this.entity.getHeaders() == null)
            this.entity.setHeaders(new HashMap<>());
        if (this.entity.getDownloadBytes() == 0L)
            this.entity.setCreateTime(System.currentTimeMillis());
        if (this.entity.getTaskId() == null)
            this.entity.setTaskId(generateTaskId());
        setDownloadBytesTemp(this.entity.getDownloadBytes());
        //setStateFromEntity(this.entity);
        /*if (!RangeHeader.hasRangeHeader(this.entity.getHeaders()))
            RangeHeader.addRangeHeader(this.entity.getHeaders(), 0);*/
    }

    private void setStateFromEntity(DownloadTaskEntity entity) {
        switch (entity.getState()) {
            case DownloadTaskEntity.STATE_PAUSE:
                toPause();
                break;
            case DownloadTaskEntity.STATE_FINISH:
                toFinish();
                break;
            case DownloadTaskEntity.STATE_CANCEL:
                toCancel();
                break;
            case DownloadTaskEntity.STATE_ERROR:
                toError();
                break;
        }
    }

    private String generateTaskId() {
        return String.valueOf(getEntity().getCreateTime());
    }

}
