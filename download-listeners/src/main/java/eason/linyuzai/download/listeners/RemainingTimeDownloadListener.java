package eason.linyuzai.download.listeners;

import android.widget.TextView;

import java.lang.ref.WeakReference;

import eason.linyuzai.download.listeners.common.ViewUpdater;
import eason.linyuzai.download.task.DownloadTask;
import io.reactivex.Emitter;

public class RemainingTimeDownloadListener extends BytesIncreaseIntervalDownloadListener implements ViewUpdater {

    private long remaining;
    private WeakReference<TextView> textView;

    public RemainingTimeDownloadListener(TextView textView) {
        super();
        this.textView = new WeakReference<>(textView);
    }

    public TextView getTextView() {
        return textView.get();
    }

    public void setTextView(TextView textView) {
        this.textView = new WeakReference<>(textView);
    }

    @Override
    public void emit(Emitter<Long> emitter, DownloadTask task, long bytesLength, boolean isComplete) {
        long total = task.getEntity().getTotalBytes();
        if (total == -1) {
            remaining = -1;
        } else {
            remaining = total - task.getDownloadBytesTemp();
        }
        super.emit(emitter, task, bytesLength, isComplete);
    }

    @Override
    public void onInterval(Long bytes) {
        TextView tv = textView.get();
        String rst;
        if (remaining == -1 || bytes == 0L) {
            rst = "未知";
        } else {
            long sec = remaining / bytes;
            //Log.d("sec", "sec=" + sec + ",total=" + total + ",bytes=" + bytes);
            if (sec >= 60 * 60 * 24) {
                int day = (int) (sec / (60 * 60 * 24));
                int hour = (int) ((sec - day * 60 * 60 * 24) / (60 * 60));
                rst = "剩余" + addZero(day) + "天" + addZero(hour) + "时";
            } else if (sec >= 60 * 60) {
                int hour = (int) (sec / (60 * 60));
                int minute = (int) ((sec - hour * 60 * 60) / 60);
                rst = "剩余" + addZero(hour) + "时" + addZero(minute) + "分";
            } else if (sec >= 60) {
                int minute = (int) (sec / 60);
                int second = (int) (sec - minute * 60);
                rst = "剩余" + addZero(minute) + "分" + addZero(second) + "秒";
            } else {
                int second = (int) sec;
                rst = "剩余" + addZero(second) + "秒";
            }
        }

        updateText(tv, rst);
    }

    private String addZero(int tan) {
        return tan >= 10 ? tan + "" : "0" + tan;
    }
}
