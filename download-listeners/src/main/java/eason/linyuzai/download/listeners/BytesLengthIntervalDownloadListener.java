package eason.linyuzai.download.listeners;

import eason.linyuzai.download.task.DownloadTask;
import io.reactivex.Emitter;

public abstract class BytesLengthIntervalDownloadListener extends IntervalDownloadListener<Long> {
    public BytesLengthIntervalDownloadListener(long interval) {
        super(interval);
    }

    public BytesLengthIntervalDownloadListener() {
        super();
    }

    @Override
    public void emit(Emitter<Long> emitter, DownloadTask task, long bytesLength, boolean isComplete) {
        if (isComplete) {
            emitter.onNext(task.getEntity().getTotalBytes());
        } else {
            emitter.onNext(task.getDownloadBytesTemp());
        }
    }
}
