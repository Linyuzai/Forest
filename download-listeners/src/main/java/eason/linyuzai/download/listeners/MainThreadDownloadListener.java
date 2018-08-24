package eason.linyuzai.download.listeners;

import eason.linyuzai.download.task.DownloadTask;
import eason.linyuzai.download.task.DownloadTaskWrapper;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public abstract class MainThreadDownloadListener implements DownloadTask.DownloadListener {

    private Emitter<DownloadTaskWrapper> emitter;
    private Disposable disposable;

    public MainThreadDownloadListener() {
        Observable<DownloadTaskWrapper> observable = Observable.create(emitter -> this.emitter = emitter);
        disposable = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(call -> {
            switch (call.getCallType()) {
                case DownloadTaskWrapper.DOWNLOAD_CONTENT_LENGTH:
                    onDownloadContentLengthReadOnMainThread(call.getDownloadTask(), call.getContentLength());
                    break;
                case DownloadTaskWrapper.DOWNLOAD_BYTES_READ:
                    onDownloadBytesReadOnMainThread(call.getDownloadTask(), call.getBytesRead());
                    break;
                case DownloadTaskWrapper.DOWNLOAD_COMPLETE:
                    onDownloadCompleteOnMainThread(call.getDownloadTask());
                    break;
            }
        }, Throwable::printStackTrace);
    }

    @Override
    public final void onDownloadContentLengthRead(DownloadTask task, long contentLength) {
        emitter.onNext(DownloadTaskWrapper.downloadContentLength(task, contentLength));
    }

    @Override
    public final void onDownloadBytesRead(DownloadTask task, long bytesLength) {
        emitter.onNext(DownloadTaskWrapper.downloadBytesRead(task, bytesLength));
    }

    @Override
    public final void onDownloadComplete(DownloadTask task) {
        emitter.onNext(DownloadTaskWrapper.downloadComplete(task));
        emitter.onComplete();
        /*if (disposable != null && !disposable.isDisposed())
            disposable.dispose();*/
    }

    public void onDownloadContentLengthReadOnMainThread(DownloadTask task, long contentLength) {

    }

    public void onDownloadBytesReadOnMainThread(DownloadTask task, long bytesLength) {

    }

    public void onDownloadCompleteOnMainThread(DownloadTask task) {

    }
}
