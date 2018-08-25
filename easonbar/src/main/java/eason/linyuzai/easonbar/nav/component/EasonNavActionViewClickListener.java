package eason.linyuzai.easonbar.nav.component;

import eason.linyuzai.easonbar.nav.EasonNav;

/**
 * Created by linyuzai on 2018/5/9.
 *
 * @author linyuzai
 */

public interface EasonNavActionViewClickListener {
    void onActionButtonClick(EasonNav.ActionView button, @NavLocation int location);
}
