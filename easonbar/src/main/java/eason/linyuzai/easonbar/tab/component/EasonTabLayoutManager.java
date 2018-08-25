package eason.linyuzai.easonbar.tab.component;

import java.util.List;

import eason.linyuzai.easonbar.tab.EasonTab;

/**
 * Created by linyuzai on 2018/5/7.
 *
 * @author linyuzai
 */

public interface EasonTabLayoutManager<V extends EasonTab.TabItem> {

    void layoutItems(EasonTab parent, List<EasonTab.Entity> entities, EasonTabRecycler<V> recycler, int currentIndex);
}
