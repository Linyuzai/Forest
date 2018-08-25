package eason.linyuzai.easonbar.tab.component;

import eason.linyuzai.easonbar.tab.EasonTab;

/**
 * Created by linyuzai on 2018/5/8.
 *
 * @author linyuzai
 */

public interface EasonTabFragmentAdapter<F> {

    F onAttachFragment(EasonTab.Entity entity);

    void onBindFragment(F fragment, EasonTab.Entity entity);

    void onShowFragment(F fragment, EasonTab.Entity entity);

    void onHideFragment(F fragment, EasonTab.Entity entity);
}
