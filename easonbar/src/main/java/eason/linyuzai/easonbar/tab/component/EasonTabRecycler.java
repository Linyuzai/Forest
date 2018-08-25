package eason.linyuzai.easonbar.tab.component;

import eason.linyuzai.easonbar.tab.EasonTab;

/**
 * Created by linyuzai on 2018/5/7.
 *
 * @author linyuzai
 */

public interface EasonTabRecycler<V extends EasonTab.TabItem> {

    V getRecycleView();

    void recycle(V view);
}
