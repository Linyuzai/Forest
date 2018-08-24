package eason.linyuzai.download.listeners;

import eason.linyuzai.download.recycle.TaskRecycler;
import eason.linyuzai.download.task.DownloadTask;
import eason.linyuzai.download.task.DownloadTaskWrapper;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class MainThreadDownloadTaskListener implements DownloadTask.DownloadTaskListener {

    private Emitter<DownloadTaskWrapper> emitter;
    private Disposable disposable;

    public MainThreadDownloadTaskListener() {
        Observable<DownloadTaskWrapper> observable = Observable.create(emitter -> this.emitter = emitter);
        disposable = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(call -> {
            switch (call.getCallType()) {
                case DownloadTaskWrapper.TASK_PREPARE:
                    onDownloadTaskPrepareOnMainThread(call.getDownloadTask(), call.getObservable());
                    break;
                case DownloadTaskWrapper.TASK_START:
                    onDownloadTaskStartOnMainThread(call.getDownloadTask());
                    break;
                case DownloadTaskWrapper.TASK_PAUSE:
                    onDownloadTaskPauseOnMainThread(call.getDownloadTask());
                    break;
                case DownloadTaskWrapper.TASK_RESUME:
                    onDownloadTaskResumeOnMainThread(call.getDownloadTask());
                    break;
                case DownloadTaskWrapper.TASK_ERROR:
                    onDownloadTaskErrorOnMainThread(call.getDownloadTask(), call.getThrowable());
                    break;
                case DownloadTaskWrapper.TASK_COMPLETE:
                    onDownloadTaskCompleteOnMainThread(call.getDownloadTask());
                    break;
                case DownloadTaskWrapper.TASK_CANCEL:
                    onDownloadTaskCancelOnMainThread(call.getDownloadTask());
                    break;
                case DownloadTaskWrapper.TASK_RESET:
                    onDownloadTaskResetOnMainThread(call.getDownloadTask());
                    break;
                case DownloadTaskWrapper.TASK_RECYCLE:
                    onDownloadTaskRecycleOnMainThread(call.getDownloadTask(), call.getTaskRecycler());
                    break;
            }
        }, Throwable::printStackTrace);
    }

    @Override
    public void onDownloadTaskPrepare(DownloadTask task, Observable<ResponseBody> observable) {
        emitter.onNext(DownloadTaskWrapper.taskPrepare(task, observable));
    }

    @Override
    public void onDownloadTaskStart(DownloadTask task) {
        emitter.onNext(DownloadTaskWrapper.taskStart(task));
    }

    @Override
    public void onDownloadTaskPause(DownloadTask task) {
        emitter.onNext(DownloadTaskWrapper.taskPause(task));
    }

    @Override
    public void onDownloadTaskResume(DownloadTask task) {
        emitter.onNext(DownloadTaskWrapper.taskResume(task));
    }

    @Override
    public void onDownloadTaskError(DownloadTask task, Throwable e) {
        emitter.onNext(DownloadTaskWrapper.taskError(task, e));
    }

    @Override
    public void onDownloadTaskComplete(DownloadTask task) {
        emitter.onNext(DownloadTaskWrapper.taskComplete(task));
        emitter.onComplete();
        /*if (disposable != null && !disposable.isDisposed())
            disposable.dispose();*/
    }

    @Override
    public void onDownloadTaskCancel(DownloadTask task) {
        emitter.onNext(DownloadTaskWrapper.taskCancel(task));
    }

    @Override
    public void onDownloadTaskReset(DownloadTask task) {
        emitter.onNext(DownloadTaskWrapper.taskReset(task));
    }

    @Override
    public void onDownloadTaskRecycle(DownloadTask task, TaskRecycler taskRecycler) {
        emitter.onNext(DownloadTaskWrapper.taskRecycle(task, taskRecycler));
    }

    public void onDownloadTaskPrepareOnMainThread(DownloadTask task, Observable<ResponseBody> observable) {

    }

    public void onDownloadTaskStartOnMainThread(DownloadTask task) {

    }

    public void onDownloadTaskPauseOnMainThread(DownloadTask task) {

    }

    public void onDownloadTaskResumeOnMainThread(DownloadTask task) {

    }

    public void onDownloadTaskErrorOnMainThread(DownloadTask task, Throwable e) {

    }

    public void onDownloadTaskCompleteOnMainThread(DownloadTask task) {

    }

    public void onDownloadTaskCancelOnMainThread(DownloadTask task) {

    }

    public void onDownloadTaskResetOnMainThread(DownloadTask task) {

    }

    public void onDownloadTaskRecycleOnMainThread(DownloadTask task, TaskRecycler taskRecycler) {

    }
}
