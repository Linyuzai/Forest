package eason.linyuzai.easonbar.common;

/**
 * Created by linyuzai on 2018/5/4.
 *
 * @author linyuzai
 */

public class Checker {
    public static <T> T checkNull(T o) {
        if (o == null)
            throw new NullPointerException();
        return o;
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNonNull(Object o) {
        return !isNull(o);
    }
}
