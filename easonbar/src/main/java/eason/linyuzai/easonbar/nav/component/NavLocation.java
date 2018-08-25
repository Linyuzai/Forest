package eason.linyuzai.easonbar.nav.component;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import eason.linyuzai.easonbar.nav.EasonNav;


@IntDef({EasonNav.ACTION_BUTTON_CENTER, EasonNav.ACTION_BUTTON_LEFT_LEFT,
        EasonNav.ACTION_BUTTON_LEFT_RIGHT, EasonNav.ACTION_BUTTON_RIGHT_LEFT, EasonNav.ACTION_BUTTON_RIGHT_RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface NavLocation {
}
