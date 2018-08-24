package eason.linyuzai.download.listeners.common;

import java.text.DecimalFormat;

public class DefaultDecimalFormat {
    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private DefaultDecimalFormat() {
    }

    public static DecimalFormat get() {
        return decimalFormat;
    }
}
