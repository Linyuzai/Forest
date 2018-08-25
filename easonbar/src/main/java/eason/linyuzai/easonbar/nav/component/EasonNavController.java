package eason.linyuzai.easonbar.nav.component;

import eason.linyuzai.easonbar.nav.EasonNav;

/**
 * Created by linyuzai on 2018/5/11.
 *
 * @author linyuzai
 */

public interface EasonNavController {
    <T extends EasonNav.Entity> T getEntity(@NavLocation int location);

    <T extends EasonNav.Entity> void setEntity(T entity, @NavLocation int location);

    <T extends EasonNav.Entity> void setEntityWithLayout(T entity, @NavLocation int location);

    void update(@NavLocation int location);

    void update();
}
