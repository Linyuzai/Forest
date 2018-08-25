package eason.linyuzai.easonbar.ex.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import eason.linyuzai.easonbar.R;
import eason.linyuzai.easonbar.tab.impl.adapter.DefaultEasonTabFragmentAdapter;

/**
 * Created by linyuzai on 2018/5/11.
 *
 * @author linyuzai
 */

public class DefaultEasonMainFragmentAdapter<F extends Fragment> extends DefaultEasonTabFragmentAdapter<F> {
    public DefaultEasonMainFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, R.id.eason_frame);
    }
}
