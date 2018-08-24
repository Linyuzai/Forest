package eason.linyuzai.download.listeners.common;

import java.text.DecimalFormat;

public class BytesLengthConverter {
    public static final int B = 0;
    public static final int K = 1;
    public static final int M = 2;
    public static final int G = 3;
    public static final int T = 4;

    public static class ConvertEntity {
        private String content;
        private int unit;

        private ConvertEntity(String content, int unit) {
            this.content = content;
            this.unit = unit;
        }

        public String getContent() {
            return content;
        }

        public int getUnit() {
            return unit;
        }
    }

    public static String getSpeedFromUnit(int unit) {
        switch (unit) {
            case T:
                return "t/s";
            case G:
                return "g/s";
            case M:
                return "m/s";
            case K:
                return "k/s";
            case B:
                return "b/s";
            default:
                return "";
        }
    }

    public static String getNameFromUnit(int unit) {
        switch (unit) {
            case T:
                return "T";
            case G:
                return "G";
            case M:
                return "M";
            case K:
                return "K";
            case B:
                return "";
            default:
                return "";
        }
    }

    public static ConvertEntity convert(long bytes, DecimalFormat format) {
        if (bytes >= 1024L * 1024L * 1024L * 1024L) {
            double s = bytes * 1.0 / 1024 / 1024 / 1024 / 1024;
            return new ConvertEntity(format.format(s), T);
        } else if (bytes >= 1024L * 1024L * 1024L) {
            double s = bytes * 1.0 / 1024 / 1024 / 1024;
            return new ConvertEntity(format.format(s), G);
        } else if (bytes >= 1024L * 1024L) {
            double s = bytes * 1.0 / 1024 / 1024;
            return new ConvertEntity(format.format(s), M);
        } else if (bytes >= 1024L) {
            double s = bytes * 1.0 / 1024;
            return new ConvertEntity(format.format(s), K);
        } else {
            return new ConvertEntity(String.valueOf(bytes), B);
        }
    }
}
