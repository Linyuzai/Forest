package eason.linyuzai.download.listeners;

import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

import eason.linyuzai.download.listeners.common.BytesLengthConverter;
import eason.linyuzai.download.listeners.common.DefaultDecimalFormat;
import eason.linyuzai.download.listeners.common.ViewUpdater;

public class NetPerSecDownloadListener extends BytesIncreaseIntervalDownloadListener implements ViewUpdater {

    private DecimalFormat decimalFormat = DefaultDecimalFormat.get();

    private WeakReference<TextView> textView;

    public NetPerSecDownloadListener(TextView textView) {
        super();
        this.textView = new WeakReference<>(textView);
    }

    public TextView getTextView() {
        return textView.get();
    }

    public void setTextView(TextView textView) {
        this.textView = new WeakReference<>(textView);
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public void onInterval(Long bytes) {
        TextView tv = textView.get();
        BytesLengthConverter.ConvertEntity entity = BytesLengthConverter.convert(bytes, decimalFormat);
        String rst;
        String unit = BytesLengthConverter.getSpeedFromUnit(entity.getUnit());
        /*if (bytes >= 1024 * 1024) {
            double s = bytes * 1.0 / 1024 / 1024;
            rst = decimalFormat.format(s) + "m/s";
        } else if (bytes >= 1024) {
            double s = bytes * 1.0 / 1024;
            rst = decimalFormat.format(s) + "k/s";
        } else {
            rst = bytes + "b/s";
        }*/
        rst = entity.getContent() + unit;
        updateText(tv, rst);
    }
}
