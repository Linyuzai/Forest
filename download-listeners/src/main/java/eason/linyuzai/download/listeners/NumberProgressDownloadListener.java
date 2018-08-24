package eason.linyuzai.download.listeners;

import android.widget.ProgressBar;
import android.widget.TextView;

import eason.linyuzai.download.listeners.common.BytesLengthConverter;
import eason.linyuzai.download.listeners.common.ViewUpdater;

public class NumberProgressDownloadListener extends ProgressDownloadListener implements ViewUpdater{

    public NumberProgressDownloadListener(TextView textView, ProgressBar progressBar) {
        super(textView, progressBar);
    }

    public NumberProgressDownloadListener(TextView textView, ProgressBar progressBar, long interval) {
        super(textView, progressBar, interval);
    }

    @Override
    public void onProgress(TextView textView, ProgressBar progressBar, long total, long bytes, double percent) {
        BytesLengthConverter.ConvertEntity totalEntity = BytesLengthConverter.convert(total, getDecimalFormat());
        BytesLengthConverter.ConvertEntity bytesEntity = BytesLengthConverter.convert(bytes, getDecimalFormat());
        String rst = bytesEntity.getContent() + BytesLengthConverter.getNameFromUnit(bytesEntity.getUnit()) + "/" +
                totalEntity.getContent() + BytesLengthConverter.getNameFromUnit(totalEntity.getUnit());
        updateText(textView, rst);
        updateProgress(progressBar, (int) percent);
    }
}
