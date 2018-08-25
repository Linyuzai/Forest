package eason.linyuzai.easonbar.common;

import android.util.Log;

/**
 * Created by linyuzai on 2018/5/7.
 *
 * @author linyuzai
 */

public class Logger {
    private static final String TAG = "EASON-LOGGER";

    public static void log(Object o) {
        Log.d(TAG, o + "");
    }
}
