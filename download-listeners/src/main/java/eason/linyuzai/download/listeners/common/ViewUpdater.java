package eason.linyuzai.download.listeners.common;

import android.widget.ProgressBar;
import android.widget.TextView;

public interface ViewUpdater {
    default void updateText(TextView textView, String text) {
        if (textView != null)
            textView.setText(text);
    }

    default void updateProgress(ProgressBar progressBar, int progress) {
        if (progressBar != null)
            progressBar.setProgress(progress);
    }
}
