package eason.linyuzai.easonbar.tab.component;

import eason.linyuzai.easonbar.tab.EasonTab;

/**
 * Created by linyuzai on 2018/5/5.
 *
 * @author linyuzai
 */

public interface EasonTabAdapter<V extends EasonTab.TabItem> {
    V onCreateTabItem(EasonTab parent);

    void onBindTabItem(EasonTab parent, V view, EasonTab.Entity entity);

    void onActiveTabItem(EasonTab parent, V view, EasonTab.Entity entity);

    void onResetTabItem(EasonTab parent, V view, EasonTab.Entity entity);

    boolean updateIndex(EasonTab parent, EasonTab.Entity entity, int index);
}
