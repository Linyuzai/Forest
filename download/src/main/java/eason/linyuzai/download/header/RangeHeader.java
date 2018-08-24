package eason.linyuzai.download.header;

import java.util.HashMap;
import java.util.Map;

public class RangeHeader {
    public static final String RANGE = "Range";

    public static boolean hasRangeHeader(Map<String, String> headers) {
        if (headers == null)
            return false;
        for (String key : headers.keySet()) {
            if (key.equalsIgnoreCase(RANGE)) {
                return true;
            }
        }
        return false;
    }

    public static void addRangeHeader(Map<String, String> headers, long start) {
        addRangeHeader(headers, "bytes=" + start + "-");
    }

    public static void addRangeHeader(Map<String, String> headers, String range) {
        headers.put(RANGE, supportRange(range));
    }

    public static void updateRangeHeader(Map<String, String> headers, long start) {
        addRangeHeader(headers, start);
    }

    public static void copyRangeHeader(Map<String, String> from, Map<String, String> to) {
        if (to == null)
            to = new HashMap<>();
        else
            to.clear();
        for (Map.Entry<String, String> entry : from.entrySet()) {
            to.put(entry.getKey(), entry.getValue());
        }
    }

    public static long getStartRange(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(RANGE)) {
                String value = supportRange(entry.getValue().replace("bytes=", ""));
                return Long.valueOf(value.split("-")[0]);
            }
        }
        return 0L;
    }

    public static String supportRange(String range) {
        if (range.contains(","))
            throw new UnsupportedOperationException("Unsupported multi-range");
        String[] values = range.split("-");
        if (values[0].isEmpty()) {
            throw new UnsupportedOperationException("Unsupported -bytes,use bytes-* instead");
        }
        return range;
    }
}
