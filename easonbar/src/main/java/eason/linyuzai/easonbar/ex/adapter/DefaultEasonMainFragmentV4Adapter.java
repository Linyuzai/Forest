package eason.linyuzai.easonbar.ex.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import eason.linyuzai.easonbar.R;
import eason.linyuzai.easonbar.tab.impl.adapter.DefaultEasonTabFragmentV4Adapter;

/**
 * Created by linyuzai on 2018/5/11.
 *
 * @author linyuzai
 */

public class DefaultEasonMainFragmentV4Adapter<F extends Fragment> extends DefaultEasonTabFragmentV4Adapter<F> {
    public DefaultEasonMainFragmentV4Adapter(FragmentManager fragmentManager) {
        super(fragmentManager, R.id.eason_frame);
    }
}
