package eason.linyuzai.easonbar.tab.impl.layout;

import android.view.View;

import java.util.List;

import eason.linyuzai.easonbar.tab.EasonTab;
import eason.linyuzai.easonbar.tab.component.EasonTabAdapter;
import eason.linyuzai.easonbar.tab.component.EasonTabFragmentAdapter;
import eason.linyuzai.easonbar.tab.component.EasonTabLayoutManager;
import eason.linyuzai.easonbar.tab.component.EasonTabRecycler;

/**
 * Created by linyuzai on 2018/5/7.
 *
 * @author linyuzai
 */

public class DefaultEasonTabLayoutManager implements EasonTabLayoutManager<EasonTab.TabItem> {

    @Override
    public void layoutItems(EasonTab parent, List<EasonTab.Entity> entities, EasonTabRecycler<EasonTab.TabItem> recycler, int currentIndex) {
        layoutChildren(parent, entities, currentIndex, parent.getChildCount(), recycler);
    }

    public void layoutChildren(EasonTab parent, List<EasonTab.Entity> entities, int childrenCount, int currentIndex,
                               EasonTabRecycler<EasonTab.TabItem> recycler) {
        int entitySize = entities.size();
        if (childrenCount > entitySize) {
            for (int index = entitySize; index < childrenCount; index++) {
                View v = parent.getChildAt(index);
                parent.removeView(v);
                recycler.recycle((EasonTab.TabItem) v);
            }
        } else if (childrenCount < entitySize) {
            int height = parent.getLayoutParams().height;
            for (int index = childrenCount; index < entitySize; index++) {
                EasonTab.TabItem item = recycler.getRecycleView();
                if (item == null) {
                    item = parent.getAdapter().onCreateTabItem(parent);
                    item.initialize(height, parent);
                }
                parent.addView(item);
            }
        }
        int updateIndex = currentIndex;
        for (int index = 0; index < entitySize; index++) {
            if (parent.getAdapter().updateIndex(parent, entities.get(index), index)) {
                updateIndex = index;
                break;
            }
        }
        if (updateIndex >= entitySize)
            updateIndex = 0;
        bindChildren(parent, entities, updateIndex);
    }

    @SuppressWarnings("unchecked")
    public void bindChildren(EasonTab parent, List<EasonTab.Entity> entities, int updateIndex) {
        EasonTabAdapter adapter = parent.getAdapter();
        int size = entities.size();
        //int newIndex = currentIndex >= size ? 0 : currentIndex;
        for (int index = 0; index < size; index++) {
            View v = parent.getChildAt(index);
            EasonTab.Entity entity = entities.get(index);
            boolean isActiveIndex = index == updateIndex;
            if (v instanceof EasonTab.TabItem) {
                adapter.onBindTabItem(parent, (EasonTab.TabItem) v, entity);
                if (isActiveIndex) {
                    parent.getController().active(index);
                    //adapter.onActiveTabItem(parent, (EasonTab.TabItem) v, entity);
                } else {
                    parent.getController().reset(index);
                    //adapter.onResetTabItem(parent, (EasonTab.TabItem) v, entity);
                }
            }
            attachFragment(parent, entity, isActiveIndex);
        }
    }

    @SuppressWarnings("unchecked")
    public void attachFragment(EasonTab parent, EasonTab.Entity entity, boolean checkIndex) {
        EasonTabFragmentAdapter fragmentAdapter = parent.getFragmentAdapter();
        if (fragmentAdapter != null) {
            Object f = fragmentAdapter.onAttachFragment(entity);
            fragmentAdapter.onBindFragment(f, entity);
            if (checkIndex) {
                fragmentAdapter.onShowFragment(f, entity);
            } else {
                fragmentAdapter.onHideFragment(f, entity);
            }
        }
    }
}
