package eason.linyuzai.easonbar.tab.impl.listener;

import eason.linyuzai.easonbar.tab.EasonTab;
import eason.linyuzai.easonbar.tab.component.EasonTabItemClickListener;

/**
 * Created by linyuzai on 2018/5/7.
 *
 * @author linyuzai
 */

public abstract class BaseEasonTabItemClickListener implements EasonTabItemClickListener<EasonTab.TabItem> {
    @Override
    public boolean intercept(EasonTab parent, EasonTab.TabItem view, EasonTab.Entity entity) {
        return false;
    }
}
