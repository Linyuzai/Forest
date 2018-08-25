package eason.linyuzai.easonbar.ex;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import eason.linyuzai.easonbar.R;
import eason.linyuzai.easonbar.nav.EasonNav;
import eason.linyuzai.easonbar.tab.EasonTab;

/**
 * Created by linyuzai on 2018/5/9.
 *
 * @author linyuzai
 */

public class EasonMainView extends RelativeLayout {

    private EasonNav easonNav;

    private EasonTab easonTab;

    private TabDivider tabDivider;

    public EasonMainView(Context context) {
        super(context);
        initialize(context);
    }

    public EasonMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public EasonMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EasonMainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    public void initialize(Context context) {
        LayoutParams params;
        easonNav = new EasonNav(context);
        easonNav.setId(R.id.eason_nav);
        addView(easonNav, new LayoutParams(LayoutParams.MATCH_PARENT, dip(50)));
        LinearLayout layout = new LinearLayout(context);
        layout.setId(R.id.eason_tab);
        layout.setOrientation(LinearLayout.VERTICAL);
        tabDivider = new TabDivider(context);
        tabDivider.setDividerColor(Color.LTGRAY);
        tabDivider.setDividerHeight(dip(0.5f));
        layout.addView(tabDivider);
        easonTab = new EasonTab(context);
        layout.addView(easonTab, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip(50)));
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        addView(layout, params);
        FrameLayout frame = new FrameLayout(context);
        frame.setId(R.id.eason_frame);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(BELOW, R.id.eason_nav);
        params.addRule(ABOVE, R.id.eason_tab);
        addView(frame, params);
    }

    public EasonNav getEasonNav() {
        return easonNav;
    }

    public EasonTab getEasonTab() {
        return easonTab;
    }

    public TabDivider getTabDivider() {
        return tabDivider;
    }

    public int getFrameId() {
        return R.id.eason_frame;
    }

    private int dip(int dipValue) {
        return dip((float) dipValue);
    }

    private int dip(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static class TabDivider extends View {

        public TabDivider(Context context) {
            super(context);
        }

        public TabDivider(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TabDivider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public TabDivider(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public void setDividerHeight(int height) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params == null)
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            else
                params.height = height;
            setLayoutParams(params);
        }

        public void setDividerColor(int color) {
            setBackgroundColor(color);
        }

        public void setDividerDrawable(Drawable drawable) {
            setBackgroundDrawable(drawable);
        }
    }
}
