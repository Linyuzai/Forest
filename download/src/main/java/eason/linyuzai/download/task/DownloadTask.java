package eason.linyuzai.download.task;

import java.util.ArrayList;
import java.util.List;

import eason.linyuzai.download.database.DatabaseManager;
import eason.linyuzai.download.entity.DownloadTaskEntity;
import eason.linyuzai.download.factory.DefaultOkHttpClientFactory;
import eason.linyuzai.download.factory.DefaultRetrofitFactory;
import eason.linyuzai.download.factory.OkHttpClientFactory;
import eason.linyuzai.download.factory.RetrofitFactory;
import eason.linyuzai.download.file.FileProcessor;
import eason.linyuzai.download.file.OkioBytesFileProcessor;
import eason.linyuzai.download.file.OkioSourceFileProcessor;
import eason.linyuzai.download.recycle.TaskRecycler;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public interface DownloadTask {

    DownloadTaskEntity getEntity();

    long getDownloadBytesTemp();

    DatabaseManager getDatabaseManager();

    void addDownloadListener(DownloadListener downloadListener);

    /**
     * 获得下载监听
     *
     * @return 下载监听
     */
    List<DownloadListener> getDownloadListeners();

    void addDownloadTaskListener(DownloadTaskListener downloadTaskListener);

    /**
     * 获得下载任务监听
     *
     * @return 下载任务监听
     */
    List<DownloadTaskListener> getDownloadTaskListeners();

    /**
     * 获得任务回收器
     *
     * @return 任务回收器
     */
    TaskRecycler getTaskRecycler();

    FileProcessor getFileProcessor();

    Builder newBuilder();

    /**
     * 是否是空闲状态
     *
     * @return 是否是空闲状态
     */
    boolean isIdle();

    /**
     * 是否是准备状态
     *
     * @return 是否是准备状态
     */
    boolean isAttach();

    /**
     * 是否是下载状态
     *
     * @return 是否是下载状态
     */
    boolean isRunning();

    /**
     * 是否是暂停状态
     *
     * @return 是否是暂停状态
     */
    boolean isPause();

    /**
     * 是否完成
     *
     * @return 是否完成
     */
    boolean isFinish();

    /**
     * 是否异常
     *
     * @return 是否异常
     */
    boolean isError();

    /**
     * 是否取消
     *
     * @return 是否取消
     */
    boolean isCancel();

    /**
     * 准备
     *
     * @param builder 设置下载url,headers,文件路径,文件名称等
     * @return 是否成功
     */
    boolean attach(Builder builder);

    /**
     * 开始下载
     *
     * @return 是否开始成功
     */
    default boolean start() {
        return start(false);
    }

    /**
     * 开始下载
     *
     * @param force 是否强制重新下载
     * @return 是否开始成功
     */
    boolean start(boolean force);

    /**
     * 暂停下载
     *
     * @return 是否暂停成功
     */
    boolean pause();

    /**
     * 继续下载
     *
     * @return 是否继续成功
     */
    boolean resume();

    /**
     * 取消下载
     *
     * @return 是否取消成功
     */
    default boolean cancel() {
        return cancel(false);
    }

    boolean cancel(boolean delFile);

    /**
     * 重置
     *
     * @return 是否重置成功
     */
    boolean reset();

    /**
     * 回收
     *
     * @return 是否回收成功
     */
    boolean recycle();

    boolean insert();

    boolean update();

    boolean delete();

    Observable<DownloadTaskWrapper> toObservable();

    Observable<DownloadTaskWrapper> toDownloadObservable();

    Observable<DownloadTaskWrapper> toTaskObservable();

    default Flowable<DownloadTaskWrapper> toFlowable() {
        return toFlowable(BackpressureStrategy.BUFFER);
    }

    default Flowable<DownloadTaskWrapper> toDownloadFlowable() {
        return toDownloadFlowable(BackpressureStrategy.BUFFER);
    }

    default Flowable<DownloadTaskWrapper> toTaskFlowable() {
        return toTaskFlowable(BackpressureStrategy.BUFFER);
    }

    Flowable<DownloadTaskWrapper> toFlowable(BackpressureStrategy strategy);

    Flowable<DownloadTaskWrapper> toDownloadFlowable(BackpressureStrategy strategy);

    Flowable<DownloadTaskWrapper> toTaskFlowable(BackpressureStrategy strategy);

    class Builder {
        OkHttpClientFactory okHttpClientFactory;
        RetrofitFactory retrofitFactory;
        DownloadTaskEntity entity;
        TaskRecycler taskRecycler;
        FileProcessor fileProcessor;
        DatabaseManager databaseManager;
        List<DownloadListener> downloadListeners = new ArrayList<>();
        List<DownloadTaskListener> downloadTaskListeners = new ArrayList<>();

        public Builder setOkHttpClientFactory(OkHttpClientFactory okHttpClientFactory) {
            this.okHttpClientFactory = okHttpClientFactory;
            return this;
        }

        public Builder setRetrofitFactory(RetrofitFactory retrofitFactory) {
            this.retrofitFactory = retrofitFactory;
            return this;
        }

        public Builder setEntity(DownloadTaskEntity entity) {
            this.entity = entity;
            return this;
        }

        public Builder setTaskRecycler(TaskRecycler taskRecycler) {
            this.taskRecycler = taskRecycler;
            return this;
        }

        public Builder setFileProcessor(FileProcessor fileProcessor) {
            this.fileProcessor = fileProcessor;
            return this;
        }

        public Builder setDatabaseManager(DatabaseManager databaseManager) {
            this.databaseManager = databaseManager;
            return this;
        }

        public DownloadTaskEntity getEntity() {
            return entity;
        }

        public Builder addDownloadListener(DownloadListener downloadListener) {
            if (downloadListener != null)
                this.downloadListeners.add(downloadListener);
            return this;
        }

        public Builder addDownloadTaskListener(DownloadTaskListener downloadTaskListener) {
            if (downloadTaskListener != null)
                this.downloadTaskListeners.add(downloadTaskListener);
            return this;
        }

        public List<DownloadListener> getDownloadListeners() {
            return downloadListeners;
        }

        public List<DownloadTaskListener> getDownloadTaskListeners() {
            return downloadTaskListeners;
        }

        public DownloadTask build() {
            if (okHttpClientFactory == null)
                okHttpClientFactory = DefaultOkHttpClientFactory.get();
            if (retrofitFactory == null)
                retrofitFactory = DefaultRetrofitFactory.get();
            if (fileProcessor == null)
                fileProcessor = new OkioSourceFileProcessor();
            return new RetrofitDownloadTask(this);
        }
    }

    interface ProgressListener {

        void reset();

        void update(long bytesRead, long contentLength, boolean done);
    }

    interface ResponseHeadersListener {

        void headers(Headers headers);
    }

    interface DownloadListener {

        /**
         * 获得文件大小
         *
         * @param task          下载任务
         * @param contentLength 文件大小
         */
        void onDownloadContentLengthRead(DownloadTask task, long contentLength);

        /**
         * 文件下载进度
         *
         * @param task        下载任务
         * @param bytesLength 已下载文件大小
         */
        void onDownloadBytesRead(DownloadTask task, long bytesLength);

        /**
         * 文件下载完成
         *
         * @param task 下载任务
         */
        void onDownloadComplete(DownloadTask task);
    }

    interface DownloadTaskListener {
        /**
         * 下载任务被观察者准备就绪
         *
         * @param task       下载任务
         * @param observable 下载任务被观察者
         */
        void onDownloadTaskPrepare(DownloadTask task, Observable<ResponseBody> observable);

        /**
         * 开始下载
         *
         * @param task 下载任务
         */
        void onDownloadTaskStart(DownloadTask task);

        /**
         * 下载任务暂停
         *
         * @param task 下载任务
         */
        void onDownloadTaskPause(DownloadTask task);

        /**
         * 下载任务继续
         *
         * @param task 下载任务
         */
        void onDownloadTaskResume(DownloadTask task);

        /**
         * 下载任务出错
         *
         * @param task 下载任务
         * @param e    异常
         */
        void onDownloadTaskError(DownloadTask task, Throwable e);

        /**
         * 下载任务完成
         *
         * @param task 下载任务
         */
        void onDownloadTaskComplete(DownloadTask task);

        /**
         * 下载任务取消
         *
         * @param task 下载任务
         */
        void onDownloadTaskCancel(DownloadTask task);

        /**
         * 下载任务重置
         *
         * @param task 下载任务
         */
        void onDownloadTaskReset(DownloadTask task);

        /**
         * 下载任务回收
         *
         * @param task         下载任务
         * @param taskRecycler 任务回收器
         */
        void onDownloadTaskRecycle(DownloadTask task, TaskRecycler taskRecycler);
    }
}
