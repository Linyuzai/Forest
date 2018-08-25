package eason.linyuzai.easonbar.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import eason.linyuzai.easonbar.R;
import eason.linyuzai.easonbar.common.Checker;
import eason.linyuzai.easonbar.entity.EasonEntity;
import eason.linyuzai.easonbar.tab.component.EasonTabAdapter;
import eason.linyuzai.easonbar.tab.component.EasonTabController;
import eason.linyuzai.easonbar.tab.component.EasonTabFragmentAdapter;
import eason.linyuzai.easonbar.tab.component.EasonTabItemClickListener;
import eason.linyuzai.easonbar.tab.component.EasonTabLayoutManager;
import eason.linyuzai.easonbar.tab.component.EasonTabRecycler;
import eason.linyuzai.easonbar.tab.impl.adapter.DefaultEasonTabAdapter;
import eason.linyuzai.easonbar.tab.impl.layout.DefaultEasonTabLayoutManager;

/**
 * Created by linyuzai on 2018/5/4.
 *
 * @author linyuzai
 */

public class EasonTab extends LinearLayout implements View.OnClickListener {

    private EasonTabAdapter adapter;

    private EasonTabFragmentAdapter fragmentAdapter;

    private EasonTabLayoutManager layoutManager;

    private EasonTabItemClickListener listener;

    private EasonTabController controller = new Controller();

    private EasonTabRecycler recycler = new Recycler();

    private int activeIndex = 0;

    public EasonTab(Context context) {
        super(context);
        initialize();
    }

    public EasonTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public EasonTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EasonTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(Color.WHITE);
        setAdapter(new DefaultEasonTabAdapter());
        setLayoutManager(new DefaultEasonTabLayoutManager());
    }

    public EasonTabAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(EasonTabAdapter adapter) {
        this.adapter = adapter;
    }

    public EasonTabFragmentAdapter getFragmentAdapter() {
        return fragmentAdapter;
    }

    public void setFragmentAdapter(EasonTabFragmentAdapter fragmentAdapter) {
        this.fragmentAdapter = fragmentAdapter;
    }

    public EasonTabLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(EasonTabLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EasonTabItemClickListener getOnItemClickListener() {
        return listener;
    }

    public void setOnItemClickListener(EasonTabItemClickListener listener) {
        this.listener = listener;
    }

    public EasonTabController getController() {
        return controller;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public static class Entity extends EasonEntity {
        private static final String ACTIVE_DRAWABLE = "active_drawable";
        private static final String DEFAULT_DRAWABLE = "default_drawable";
        private static final String ACTIVE_COLOR = "active_color";
        private static final String DEFAULT_COLOR = "default_color";
        private static final String TEXT = "text";
        private static final String TEXT_SIZE = "text_size";
        private static final String MESSAGE_COUNT = "message_count";
        private static final String MESSAGE_COLOR = "message_color";
        private static final String MESSAGE_TEXT_SIZE = "message_text_size";
        private static final String FRAGMENT = "fragment";
        private static final String FRAGMENT_TAG = "fragment_tag";

        public void setActiveDrawable(Drawable drawable) {
            add(getKey(ACTIVE_DRAWABLE), drawable);
        }

        public Drawable getActiveDrawable() {
            return (Drawable) get(getKey(ACTIVE_DRAWABLE));
        }

        public void setDefaultDrawable(Drawable drawable) {
            add(getKey(DEFAULT_DRAWABLE), drawable);
        }

        public Drawable getDefaultDrawable() {
            return (Drawable) get(getKey(DEFAULT_DRAWABLE));
        }

        public void setText(String text) {
            add(getKey(TEXT), text);
        }

        public String getText() {
            return (String) get(getKey(TEXT));
        }

        public void setTextSize(float size) {
            add(getKey(TEXT_SIZE), size);
        }

        public float getTextSize() {
            Object size = get(getKey(TEXT_SIZE));
            if (size == null)
                return 15f;
            return (float) size;
        }

        public void setActiveColor(int color) {
            add(getKey(ACTIVE_COLOR), color);
        }

        public int getActiveColor() {
            Object color = get(getKey(ACTIVE_COLOR));
            if (color == null)
                return Color.BLACK;
            return (int) color;
        }

        public void setDefaultColor(int color) {
            add(getKey(DEFAULT_COLOR), color);
        }

        public int getDefaultColor() {
            Object color = get(getKey(DEFAULT_COLOR));
            if (color == null)
                return Color.LTGRAY;
            return (int) color;
        }

        public void setMessageTextSize(float size) {
            add(getKey(MESSAGE_TEXT_SIZE), size);
        }

        public float getMessageTextSize() {
            Object size = get(getKey(MESSAGE_TEXT_SIZE));
            if (size == null)
                return 10f;
            return (float) size;
        }

        public void setMessageCount(int count) {
            add(getKey(MESSAGE_COUNT), count);
        }

        public int getMessageColor() {
            Object color = get(getKey(MESSAGE_COLOR));
            if (color == null)
                return Color.parseColor("#ffcc0000");
            return (int) color;
        }

        public void setMessageColor(int color) {
            add(getKey(MESSAGE_COLOR), color);
        }

        public int getMessageCount() {
            Object count = get(getKey(MESSAGE_COUNT));
            if (count == null)
                return 0;
            return (int) count;
        }

        public void setFragment(String tag, Object fragment) {
            add(getKey(FRAGMENT), fragment);
            add(getKey(FRAGMENT_TAG), tag);
        }

        public Object getFragment() {
            return get(getKey(FRAGMENT));
        }

        public String getFragmentTag() {
            return (String) get(getKey(FRAGMENT_TAG));
        }
    }

    public static class DefaultTabItem extends BottomTitleTabItem {
        private ImageView image;

        public DefaultTabItem(@NonNull Context context) {
            super(context);
        }

        public DefaultTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public DefaultTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public DefaultTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void active() {
            super.active();
            image.setImageDrawable(getEntity().getActiveDrawable());
        }

        @Override
        public void reset() {
            super.reset();
            image.setImageDrawable(getEntity().getDefaultDrawable());
        }

        @Override
        public View getTopView(Context context, int height) {
            return image = getIconView(context, height);
        }

        public ImageView getIconView(Context context, int height) {
            ImageView image = new ImageView(context);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image.setLayoutParams(getIconViewLayoutParams(height));
            return image;
        }

        public ViewGroup.LayoutParams getIconViewLayoutParams(int height) {
            int size = (int) (0.6f * height);
            return new FrameLayout.LayoutParams(size, size, Gravity.CENTER);
        }
    }

    public static abstract class BottomTitleTabItem extends TwoLayerTabItem {
        private TextView text;
        private TextView message;

        public BottomTitleTabItem(@NonNull Context context) {
            super(context);
        }

        public BottomTitleTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public BottomTitleTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public BottomTitleTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void update(Entity entity) {
            super.update(entity);
            text.setText(entity.getText());
            int count = entity.getMessageCount();
            if (count == 0) {
                message.setVisibility(View.GONE);
            } else {
                message.setText(String.valueOf(count));
                message.setVisibility(View.VISIBLE);
            }
            GradientDrawable drawable = (GradientDrawable) message.getBackground();
            drawable.setSize(message.getLayoutParams().width, message.getLayoutParams().height);
            drawable.setColor(entity.getMessageColor());
        }

        @Override
        public void active() {
            text.setTextColor(getEntity().getActiveColor());
        }

        @Override
        public void reset() {
            text.setTextColor(getEntity().getDefaultColor());
        }

        @Override
        public View getFirstLayerView(Context context, int height) {
            FrameLayout frame = new FrameLayout(context);
            frame.addView(getTopView(context, height));
            message = getMessageView(getContext(), height);
            frame.addView(message);
            frame.setLayoutParams(getFrameLayoutParams(height));
            return frame;
        }

        @Override
        public View getSecondLayerView(Context context, int height) {
            return text = getTitleView(context, height);
        }

        public abstract View getTopView(Context context, int height);

        public TextView getTitleView(Context context, int height) {
            TextView text = new TextView(context);
            text.setGravity(Gravity.CENTER);
            text.setTextSize(getEntity().getTextSize());
            text.setLayoutParams(getTitleViewLayoutParams(height));
            return text;
        }

        public ViewGroup.LayoutParams getTitleViewLayoutParams(int height) {
            return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (0.3f * height));
        }

        public TextView getMessageView(Context context, int height) {
            TextView message = new TextView(context);
            message.setTextSize(getEntity().getMessageTextSize());
            message.setTextColor(Color.WHITE);
            message.setVisibility(View.GONE);
            message.setBackgroundResource(R.drawable.red_round);
            message.setLayoutParams(getMessageViewLayoutParams(height));
            return message;
        }

        public ViewGroup.LayoutParams getMessageViewLayoutParams(int height) {
            int msgSize = (int) (0.3f * height);
            return new FrameLayout.LayoutParams(msgSize, msgSize, Gravity.RIGHT | Gravity.END);
        }

        public ViewGroup.LayoutParams getFrameLayoutParams(int height) {
            return new LinearLayout.LayoutParams((int) (0.9f * height), LayoutParams.WRAP_CONTENT);
        }
    }

    public static abstract class TwoLayerTabItem extends TabItem {

        public TwoLayerTabItem(@NonNull Context context) {
            super(context);
        }

        public TwoLayerTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TwoLayerTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public TwoLayerTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public View getView(Context context, int height) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(getFirstLayerView(getContext(), height));
            layout.addView(getSecondLayerView(getContext(), height));
            return layout;
        }

        public abstract View getFirstLayerView(Context context, int height);

        public abstract View getSecondLayerView(Context context, int height);
    }

    public static abstract class TabItem extends FrameLayout {

        private Entity entity;

        private boolean isInitialized;

        public TabItem(@NonNull Context context) {
            super(context);
        }

        public TabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TabItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public TabItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public void initialize(int height, OnClickListener l) {
            if (isInitialized)
                return;
            View view = getView(getContext(), height);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params == null) {
                view.setLayoutParams(getViewLayoutParams(height));
            }
            addView(view);
            setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            setOnClickListener(l);
            isInitialized = true;
        }

        public void update(Entity entity) {
            setEntity(entity);
        }

        public abstract View getView(Context context, int height);

        public ViewGroup.LayoutParams getViewLayoutParams(int height) {
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.topMargin = getTopMargin(height);
            params.bottomMargin = getBottomMargin(height);
            return params;
        }

        public abstract void active();

        public abstract void reset();

        public Entity getEntity() {
            return entity;
        }

        public void setEntity(Entity entity) {
            this.entity = entity;
        }

        public int getTopMargin(int height) {
            return (int) (0.05f * height);
        }

        public int getBottomMargin(int height) {
            return (int) (0.05f * height);
        }

        public boolean isInitialized() {
            return isInitialized;
        }

        public void setInitialized(boolean initialized) {
            isInitialized = initialized;
        }
    }

    private class Recycler implements EasonTabRecycler<TabItem> {
        private Queue<TabItem> recycleItems = new LinkedList<>();

        @Override
        public TabItem getRecycleView() {
            return recycleItems.poll();
        }

        @Override
        public void recycle(TabItem view) {
            recycleItems.offer(view);
        }
    }

    private class Controller implements EasonTabController {
        private List<Entity> entities = new ArrayList<>();

        @Override
        @NonNull
        public List<EasonTab.Entity> entities() {
            return entities;
        }

        @Override
        public void add(@NonNull EasonTab.Entity... entities) {
            for (Entity entity : entities)
                this.entities.add(Checker.checkNull(entity));
        }

        @Override
        public void add(int index, @NonNull EasonTab.Entity entity) {
            entities.add(index, entity);
        }

        @Override
        public void remove(int index) {
            entities.remove(index);
        }

        @Override
        public void swap(int i, int j) {
            Collections.swap(entities, i, j);
        }

        @Override
        public void addWithLayout(@NonNull Entity... entities) {
            add(entities);
            update();
        }

        @Override
        public void addWithLayout(int index, @NonNull Entity entity) {
            add(index, entity);
            update();
        }

        @Override
        public void removeWithLayout(int index) {
            remove(index);
            update();
        }

        @Override
        public void swapWithLayout(int i, int j) {
            swap(i, j);
            update();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void update() {
            layoutManager.layoutItems(EasonTab.this, entities, recycler, activeIndex);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void active(int index) {
            activeIndex = index;
            adapter.onActiveTabItem(EasonTab.this, (TabItem) getChildAt(index), entities.get(index));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void reset(int index) {
            adapter.onResetTabItem(EasonTab.this, (TabItem) getChildAt(index), entities.get(index));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        int index = indexOfChild(v);
        Entity entity = controller.entities().get(index);
        if (listener == null || !listener.intercept(this, (TabItem) v, entity)) {
            controller.reset(activeIndex);
            controller.active(index);
            if (fragmentAdapter != null) {
                for (int i = 0; i < controller.entities().size(); i++) {
                    Entity e = controller.entities().get(i);
                    Object f = fragmentAdapter.onAttachFragment(e);
                    fragmentAdapter.onBindFragment(f, e);
                    if (i == activeIndex) {
                        fragmentAdapter.onShowFragment(f, e);
                    } else {
                        fragmentAdapter.onHideFragment(f, e);
                    }
                }
            }
        }
        if (listener != null)
            listener.onItemClick(this, (TabItem) v, entity);
    }
}
