package eason.linyuzai.download.listeners;

import eason.linyuzai.download.task.DownloadTask;
import io.reactivex.Emitter;

public abstract class BytesIncreaseIntervalDownloadListener extends IntervalDownloadListener<Long> {

    private long bytes;

    public BytesIncreaseIntervalDownloadListener(long interval) {
        super(interval);
    }

    public BytesIncreaseIntervalDownloadListener() {
        super();
    }

    @Override
    public void emit(Emitter<Long> emitter, DownloadTask task, long bytesLength, boolean isComplete) {
        long e = bytesLength - bytes;
        if (e >= 0) {
            emitter.onNext(e);
        }
        bytes = bytesLength;
    }
}
