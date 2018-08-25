package eason.linyuzai.easonbar.tab.component;


import android.support.annotation.NonNull;

import java.util.List;

import eason.linyuzai.easonbar.tab.EasonTab;

/**
 * Created by linyuzai on 2018/5/4.
 *
 * @author linyuzai
 */

public interface EasonTabController {

    @NonNull
    List<EasonTab.Entity> entities();

    void add(@NonNull EasonTab.Entity... entities);

    void add(int index, @NonNull EasonTab.Entity entity);

    void remove(int index);

    void swap(int i, int j);

    void addWithLayout(@NonNull EasonTab.Entity... entity);

    void addWithLayout(int index, @NonNull EasonTab.Entity entities);

    void removeWithLayout(int index);

    void swapWithLayout(int i, int j);

    void update();

    void active(int index);

    void reset(int index);
}
