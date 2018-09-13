package eason.linyuzai.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class OverlayDragLayoutManager extends RecyclerView.LayoutManager {

    private int mCurrentPosition = 0;
    private int mPreLoadCount;

    private boolean isLayered;

    public OverlayDragLayoutManager() {
        this(false, 3);
    }

    public OverlayDragLayoutManager(boolean isLayered, int mPreLoadCount) {
        this.isLayered = isLayered;
        this.mPreLoadCount = mPreLoadCount;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);

    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        layoutChildren(recycler, state);
    }

    public void layoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            Log.d("layoutChildren", "getItemCount:" + getItemCount());
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            Log.d("layoutChildren", "isPreLayout:" + state.isPreLayout());
            return;
        }
        detachAndScrapAttachedViews(recycler);
        int visiblePositionFrom = mCurrentPosition;
        int visiblePositionTo = visiblePositionFrom + mPreLoadCount;
        if (visiblePositionTo >= getItemCount()) visiblePositionTo = getItemCount() - 1;
        if (visiblePositionTo <= visiblePositionFrom) visiblePositionTo = visiblePositionFrom;
        Log.d("visiblePositionFrom", "visiblePositionFrom:" + visiblePositionFrom);
        Log.d("visiblePositionTo", "visiblePositionTo:" + visiblePositionTo);
        for (int i = visiblePositionFrom; i < visiblePositionTo; i++) {
            View child = recycler.getViewForPosition(i);
            addView(child);
            measureChildWithMargins(child, 0, 0);
            int height = getDecoratedMeasuredHeight(child);
            int width = getDecoratedMeasuredWidth(child);
            //Log.d("mRect", "mRect:" + mRect);
            layoutDecorated(child, 0, 0, width, height);
            if (i == visiblePositionFrom) {
                child.setOnDragListener((v, event) -> {
                    //event.getAction()
                    return true;
                });
            } else {
                child.setOnDragListener(null);
            }
        }
    }

    public void next(boolean rl) {

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
