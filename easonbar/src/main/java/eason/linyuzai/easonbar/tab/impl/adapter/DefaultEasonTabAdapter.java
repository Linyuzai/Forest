package eason.linyuzai.easonbar.tab.impl.adapter;

import eason.linyuzai.easonbar.tab.EasonTab;
import eason.linyuzai.easonbar.tab.component.EasonTabAdapter;

/**
 * Created by linyuzai on 2018/5/5.
 *
 * @author linyuzai
 */

public class DefaultEasonTabAdapter implements EasonTabAdapter<EasonTab.TabItem> {

    @Override
    public EasonTab.TabItem onCreateTabItem(EasonTab parent) {
        return new EasonTab.DefaultTabItem(parent.getContext());
    }

    @Override
    public void onBindTabItem(EasonTab parent, EasonTab.TabItem view, EasonTab.Entity entity) {
        view.update(entity);
    }

    @Override
    public void onActiveTabItem(EasonTab parent, EasonTab.TabItem view, EasonTab.Entity entity) {
        view.active();
    }

    @Override
    public void onResetTabItem(EasonTab parent, EasonTab.TabItem view, EasonTab.Entity entity) {
        view.reset();
    }

    @Override
    public boolean updateIndex(EasonTab parent, EasonTab.Entity entity, int index) {
        return false;
    }
}
