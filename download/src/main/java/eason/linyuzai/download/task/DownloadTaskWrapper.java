package eason.linyuzai.download.task;

import eason.linyuzai.download.recycle.TaskRecycler;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class DownloadTaskWrapper {

    public static final int DOWNLOAD_CONTENT_LENGTH = 0;
    public static final int DOWNLOAD_BYTES_READ = 1;
    public static final int DOWNLOAD_COMPLETE = 2;

    public static final int TASK_PREPARE = 3;
    public static final int TASK_START = 4;
    public static final int TASK_PAUSE = 5;
    public static final int TASK_RESUME = 6;
    public static final int TASK_ERROR = 7;
    public static final int TASK_COMPLETE = 8;
    public static final int TASK_CANCEL = 9;
    public static final int TASK_RESET = 10;
    public static final int TASK_RECYCLE = 11;

    private DownloadTask downloadTask;
    private int callType;

    private long contentLength;
    private long bytesRead;

    private Observable<ResponseBody> observable;
    private Throwable e;
    private TaskRecycler taskRecycler;

    public static DownloadTaskWrapper downloadContentLength(DownloadTask downloadTask, long contentLength) {
        return new DownloadTaskWrapper(downloadTask, DOWNLOAD_CONTENT_LENGTH, contentLength);
    }

    public static DownloadTaskWrapper downloadBytesRead(DownloadTask downloadTask, long bytesRead) {
        return new DownloadTaskWrapper(downloadTask, DOWNLOAD_BYTES_READ, bytesRead);
    }

    public static DownloadTaskWrapper downloadComplete(DownloadTask downloadTask) {
        return new DownloadTaskWrapper(downloadTask, DOWNLOAD_COMPLETE);
    }

    public static DownloadTaskWrapper taskPrepare(DownloadTask downloadTask, Observable<ResponseBody> observable) {
        return new DownloadTaskWrapper(downloadTask, observable);
    }

    public static DownloadTaskWrapper taskStart(DownloadTask downloadTask) {
        return new DownloadTaskWrapper(downloadTask, TASK_START);
    }

    public static DownloadTaskWrapper taskPause(DownloadTask downloadTask) {
        return new DownloadTaskWrapper(downloadTask, TASK_PAUSE);
    }

    public static DownloadTaskWrapper taskResume(DownloadTask downloadTask) {
        return new DownloadTaskWrapper(downloadTask, TASK_RESUME);
    }

    public static DownloadTaskWrapper taskError(DownloadTask downloadTask, Throwable e) {
        return new DownloadTaskWrapper(downloadTask, e);
    }

    public static DownloadTaskWrapper taskComplete(DownloadTask downloadTask) {
        return new DownloadTaskWrapper(downloadTask, TASK_COMPLETE);
    }

    public static DownloadTaskWrapper taskCancel(DownloadTask downloadTask) {
        return new DownloadTaskWrapper(downloadTask, TASK_CANCEL);
    }

    public static DownloadTaskWrapper taskReset(DownloadTask downloadTask) {
        return new DownloadTaskWrapper(downloadTask, TASK_RESET);
    }

    public static DownloadTaskWrapper taskRecycle(DownloadTask downloadTask, TaskRecycler taskRecycler) {
        return new DownloadTaskWrapper(downloadTask, taskRecycler);
    }

    private DownloadTaskWrapper(DownloadTask downloadTask, int callType) {
        this.downloadTask = downloadTask;
        this.callType = callType;
    }

    private DownloadTaskWrapper(DownloadTask downloadTask, int callType, long longData) {
        this.downloadTask = downloadTask;
        this.callType = callType;
        if (callType == DOWNLOAD_CONTENT_LENGTH) {
            contentLength = longData;
        }
        if (callType == DOWNLOAD_BYTES_READ) {
            bytesRead = longData;
        }
    }

    private DownloadTaskWrapper(DownloadTask downloadTask, Observable<ResponseBody> observable) {
        this.downloadTask = downloadTask;
        this.callType = TASK_PREPARE;
        this.observable = observable;
    }

    private DownloadTaskWrapper(DownloadTask downloadTask, Throwable e) {
        this.downloadTask = downloadTask;
        this.callType = TASK_ERROR;
        this.e = e;
    }

    private DownloadTaskWrapper(DownloadTask downloadTask, TaskRecycler taskRecycler) {
        this.downloadTask = downloadTask;
        this.callType = TASK_RECYCLE;
        this.taskRecycler = taskRecycler;
    }

    public long getContentLength() {
        return contentLength;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public DownloadTask getDownloadTask() {
        return downloadTask;
    }

    public int getCallType() {
        return callType;
    }

    public Observable<ResponseBody> getObservable() {
        return observable;
    }

    public Throwable getThrowable() {
        return e;
    }

    public TaskRecycler getTaskRecycler() {
        return taskRecycler;
    }
}
