package eason.linyuzai.download.listener;

import eason.linyuzai.download.recycle.TaskRecycler;
import eason.linyuzai.download.task.DownloadTask;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public abstract class AbsDownloadTaskListener implements DownloadTask.DownloadTaskListener {

    @Override
    public void onDownloadTaskPrepare(DownloadTask task, Observable<ResponseBody> observable) {

    }

    @Override
    public void onDownloadTaskStart(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskPause(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskResume(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskError(DownloadTask task, Throwable e) {

    }

    @Override
    public void onDownloadTaskComplete(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskCancel(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskReset(DownloadTask task) {

    }

    @Override
    public void onDownloadTaskRecycle(DownloadTask task, TaskRecycler taskRecycler) {

    }
}
