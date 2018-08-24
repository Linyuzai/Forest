package eason.linyuzai.download.database;

import java.util.List;

import eason.linyuzai.download.entity.DownloadTaskEntity;
import eason.linyuzai.download.recycle.TaskRecycler;
import eason.linyuzai.download.task.DownloadTask;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public abstract class DatabaseManager implements DownloadTask.DownloadTaskListener {

    public static final String DB_NAME = "download_task.db";
    public static final String TABLE_NAME = "t_download_task";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_HEADERS = "headers";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_URL_DECODER = "url_decoder";
    public static final String COLUMN_TOTAL_BYTES = "total_bytes";
    public static final String COLUMN_DOWNLOAD_BYTES = "download_bytes";
    public static final String COLUMN_FILE_PATH = "file_path";
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_EXTRA = "extra";

    public static String getDatabaseName() {
        return DB_NAME;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnTaskId() {
        return COLUMN_TASK_ID;
    }

    public static String getColumnCreateTime() {
        return COLUMN_CREATE_TIME;
    }

    public static String getColumnState() {
        return COLUMN_STATE;
    }

    public static String getColumnHeaders() {
        return COLUMN_HEADERS;
    }

    public static String getColumnUrl() {
        return COLUMN_URL;
    }

    public static String getColumnUrlDecoder() {
        return COLUMN_URL_DECODER;
    }

    public static String getColumnTotalBytes() {
        return COLUMN_TOTAL_BYTES;
    }

    public static String getColumnDownloadBytes() {
        return COLUMN_DOWNLOAD_BYTES;
    }

    public static String getColumnFilePath() {
        return COLUMN_FILE_PATH;
    }

    public static String getColumnFileName() {
        return COLUMN_FILE_NAME;
    }

    public static String getColumnExtra() {
        return COLUMN_EXTRA;
    }

    @Override
    public void onDownloadTaskPrepare(DownloadTask task, Observable<ResponseBody> observable) {
        if (count(task.getEntity()) == 0)
            createDownloadTask(task.getEntity());
    }

    @Override
    public void onDownloadTaskStart(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskPause(DownloadTask task) {
        updateDownloadTask(task.getEntity());
    }

    @Override
    public void onDownloadTaskResume(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskError(DownloadTask task, Throwable e) {
        updateDownloadTask(task.getEntity());
    }

    @Override
    public void onDownloadTaskComplete(DownloadTask task) {
        updateDownloadTask(task.getEntity());
    }

    @Override
    public void onDownloadTaskCancel(DownloadTask task) {
        updateDownloadTask(task.getEntity());
    }

    @Override
    public void onDownloadTaskReset(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskRecycle(DownloadTask task, TaskRecycler taskRecycler) {

    }

    public abstract boolean createDownloadTask(DownloadTaskEntity entity);

    public abstract boolean updateDownloadTask(DownloadTaskEntity entity);

    public abstract boolean deleteDownloadTask(DownloadTaskEntity entity);

    public abstract int count(DownloadTaskEntity entity);

    public abstract List<DownloadTaskEntity> selectAll(DownloadTaskEntity.Creator creator);

    public abstract void deleteAll();
}
