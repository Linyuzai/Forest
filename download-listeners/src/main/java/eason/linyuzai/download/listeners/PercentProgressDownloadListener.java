package eason.linyuzai.download.listeners;

import android.widget.ProgressBar;
import android.widget.TextView;

import eason.linyuzai.download.listeners.common.ViewUpdater;

public class PercentProgressDownloadListener extends ProgressDownloadListener implements ViewUpdater{

    public PercentProgressDownloadListener(TextView textView, ProgressBar progressBar) {
        super(textView, progressBar);
    }

    public PercentProgressDownloadListener(TextView textView, ProgressBar progressBar, long interval) {
        super(textView, progressBar, interval);
    }

    @Override
    public void onProgress(TextView textView, ProgressBar progressBar, long total, long bytes, double percent) {
        String rst = getDecimalFormat().format(percent) + "%";
        updateText(textView, rst);
        updateProgress(progressBar, (int) percent);
    }
}
