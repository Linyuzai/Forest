package eason.linyuzai.download.listeners;

import eason.linyuzai.download.task.DownloadTask;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public abstract class IntervalDownloadListener<T> implements DownloadTask.DownloadListener {

    public static final long INTERVAL_DEFAULT = 1000L;

    private long time;

    private long interval;

    private Emitter<T> emitter;
    private Disposable disposable;

    public IntervalDownloadListener(long interval) {
        this.interval = interval;
        Observable<T> observable = Observable.create(emitter -> this.emitter = emitter);
        disposable = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(this::onInterval, Throwable::printStackTrace);
    }

    public IntervalDownloadListener() {
        this(INTERVAL_DEFAULT);
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    @Override
    public void onDownloadContentLengthRead(DownloadTask task, long contentLength) {
        time = System.currentTimeMillis();
    }

    @Override
    public void onDownloadBytesRead(DownloadTask task, long bytesLength) {
        long current = System.currentTimeMillis();
        if (current - time >= interval) {
            emit(emitter, task, bytesLength, false);
            time = current;
        }
    }

    @Override
    public void onDownloadComplete(DownloadTask task) {
        emit(emitter, task, task.getEntity().getTotalBytes(), true);
        emitter.onComplete();
        /*if (disposable != null && !disposable.isDisposed())
            disposable.dispose();*/
    }

    public abstract void emit(Emitter<T> emitter, DownloadTask task, long bytesLength, boolean isComplete);

    public abstract void onInterval(T data);
}
