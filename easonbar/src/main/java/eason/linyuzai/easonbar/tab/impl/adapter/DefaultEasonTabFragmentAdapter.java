package eason.linyuzai.easonbar.tab.impl.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import eason.linyuzai.easonbar.tab.EasonTab;
import eason.linyuzai.easonbar.tab.component.EasonTabFragmentAdapter;

/**
 * Created by linyuzai on 2018/5/8.
 *
 * @author linyuzai
 */

public class DefaultEasonTabFragmentAdapter<F extends Fragment> implements EasonTabFragmentAdapter<F> {

    private FragmentManager fragmentManager;
    private int frameLayoutId;

    public DefaultEasonTabFragmentAdapter(FragmentManager fragmentManager, int frameLayoutId) {
        this.fragmentManager = fragmentManager;
        this.frameLayoutId = frameLayoutId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public F onAttachFragment(EasonTab.Entity entity) {
        Fragment f = fragmentManager.findFragmentByTag(entity.getFragmentTag());
        if (f == null) {
            f = (Fragment) entity.getFragment();
            fragmentManager.beginTransaction().add(frameLayoutId, f, entity.getFragmentTag()).commit();
        }
        return (F) f;
    }

    @Override
    public void onBindFragment(F fragment, EasonTab.Entity entity) {

    }

    @Override
    public void onShowFragment(F fragment, EasonTab.Entity entity) {
        fragmentManager.beginTransaction().show(fragment).commit();
    }

    @Override
    public void onHideFragment(F fragment, EasonTab.Entity entity) {
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}
