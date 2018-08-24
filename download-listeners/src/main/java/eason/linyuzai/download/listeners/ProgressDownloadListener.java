package eason.linyuzai.download.listeners;

import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

import eason.linyuzai.download.listeners.common.DefaultDecimalFormat;
import eason.linyuzai.download.task.DownloadTask;

public abstract class ProgressDownloadListener extends BytesLengthIntervalDownloadListener {

    private long total;

    private DecimalFormat decimalFormat = DefaultDecimalFormat.get();

    private WeakReference<TextView> textView;
    private WeakReference<ProgressBar> progressBar;

    public ProgressDownloadListener(TextView textView, ProgressBar progressBar) {
        this(textView, progressBar, 300);
    }

    public ProgressDownloadListener(TextView textView, ProgressBar progressBar, long interval) {
        super(interval);
        this.textView = new WeakReference<>(textView);
        this.progressBar = new WeakReference<>(progressBar);
    }

    @Override
    public void onDownloadContentLengthRead(DownloadTask task, long contentLength) {
        this.total = task.getEntity().getTotalBytes();
        super.onDownloadContentLengthRead(task, contentLength);
    }

    public TextView getTextView() {
        return textView.get();
    }

    public void setTextView(TextView textView) {
        this.textView = new WeakReference<>(textView);
    }

    public ProgressBar getProgressBar() {
        return progressBar.get();
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = new WeakReference<>(progressBar);
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public void onInterval(Long data) {
        TextView tv = textView.get();
        ProgressBar pb = progressBar.get();
        if (data == null)
            return;
        onProgress(tv, pb, total, data, data == total ? 100.0 : (data * 1.0 / total * 100));
    }

    public abstract void onProgress(TextView textView, ProgressBar progressBar, long total, long bytes, double percent);
}
