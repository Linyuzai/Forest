package eason.linyuzai.easonbar.tab.component;

import eason.linyuzai.easonbar.tab.EasonTab;

/**
 * Created by linyuzai on 2018/5/7.
 *
 * @author linyuzai
 */

public interface EasonTabItemClickListener<V extends EasonTab.TabItem> {
    boolean intercept(EasonTab parent, V view, EasonTab.Entity entity);

    void onItemClick(EasonTab parent, V view, EasonTab.Entity entity);
}
