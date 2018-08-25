package eason.linyuzai.easonbar.entity;

/**
 * Created by linyuzai on 2018/5/9.
 *
 * @author linyuzai
 */

public interface DataAccess {
    void add(String key, Object value);

    <T> T get(String key);
}
