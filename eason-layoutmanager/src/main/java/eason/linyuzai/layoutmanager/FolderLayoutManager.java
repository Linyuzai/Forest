package eason.linyuzai.layoutmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class FolderLayoutManager extends RecyclerView.LayoutManager {

    private int mScrollOffset;//滚动距离

    private int mShowCount;//显示个数
    private float mItemSpace;//item间隔

    private float mMinDegree;//最小旋转角度
    private float mMaxDegree;//最大旋转角度

    private float mMinScale;//最小缩放比例
    private float mMaxScale;//最大缩放比例

    private ExpandViewState mExpandViewState = new ExpandViewState();

    public FolderLayoutManager() {
        setShowCount(5);
        setMinDegree(10f);
        setMaxDegree(20f);
        setMinScale(0.95f);
        setMaxScale(1f);
    }

    /**
     * 设置显示个数
     *
     * @param showCount 显示个数
     */
    public void setShowCount(int showCount) {
        this.mShowCount = showCount;
        this.mItemSpace = 1f / showCount;
    }

    /**
     * 获得显示个数
     *
     * @return 显示个数
     */
    public int getShowCount() {
        return mShowCount;
    }

    /**
     * 设置最小旋转角度
     *
     * @param minDegree 最小旋转角度
     */
    public void setMinDegree(float minDegree) {
        this.mMinDegree = minDegree;
    }

    /**
     * 获得最小旋转角度
     *
     * @return 最小旋转角度
     */
    public float getMinDegree() {
        return mMinDegree;
    }

    /**
     * 设置最大旋转角度
     *
     * @param maxDegree 最大旋转角度
     */
    public void setMaxDegree(float maxDegree) {
        this.mMaxDegree = maxDegree;
    }

    /**
     * 获得最大旋转角度
     *
     * @return 最大旋转角度
     */
    public float getMaxDegree() {
        return mMaxDegree;
    }

    /**
     * 设置最小缩放比例
     *
     * @param minScale 最小缩放比例
     */
    public void setMinScale(float minScale) {
        this.mMinScale = minScale;
    }

    /**
     * 获得最小缩放比例
     *
     * @return 最小缩放比例
     */
    public float getMinScale() {
        return mMinScale;
    }

    /**
     * 设置最大缩放比例
     *
     * @param maxScale 最大缩放比例
     */
    public void setMaxScale(float maxScale) {
        this.mMaxScale = maxScale;
    }

    /**
     * 获得最大缩放比例
     *
     * @return 最大缩放比例
     */
    public float getMaxScale() {
        return mMaxScale;
    }

    /**
     * 展开某个View
     *
     * @param v 需要展开的View
     */
    public void expandView(View v) {
        expandView(v, 300);
    }

    /**
     * 展开某个View
     *
     * @param v        需要展开的View
     * @param duration 展开动画时长
     */
    public void expandView(View v, long duration) {
        if (mExpandViewState.state == ExpandViewState.COLLAPSED) {
            mExpandViewState.view = v;
            mExpandViewState.rotationX = v.getRotationX();
            mExpandViewState.scaleX = v.getScaleX();
            mExpandViewState.duration = duration;
            mExpandViewState.index = ((ViewGroup) v.getParent()).indexOfChild(v);
            mExpandViewState.position = getPosition(v);
            mExpandViewState.state = ExpandViewState.REQUEST_EXPAND;
            mExpandViewState.use = true;
            requestLayout();
        }
    }

    /**
     * 折叠某个View
     *
     * @param v 需要折叠的View
     */
    public void collapseView(View v) {
        collapseView(v, 300);
    }

    /**
     * 折叠某个View
     *
     * @param v        需要折叠的View
     * @param duration 折叠动画时长
     */
    public void collapseView(View v, long duration) {
        if (mExpandViewState.state == ExpandViewState.EXPANDED) {
            mExpandViewState.view = v;
            mExpandViewState.duration = duration;
            mExpandViewState.state = ExpandViewState.REQUEST_COLLAPSE;
            mExpandViewState.use = true;
            requestLayout();
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        layoutChildren(recycler, state);
    }

    /**
     * 布局子控件
     *
     * @param recycler recycler
     * @param state    state
     */
    public void layoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (state.isPreLayout()) {
            return;
        }
        /*
        如果当前数据数量小于显示数量，则按当前数据数量计算item间隔
         */
        if (getItemCount() < mShowCount)
            mItemSpace = 1f / getItemCount();
        fill(recycler, state);
    }

    /**
     * 填充View
     *
     * @param recycler recycler
     * @param state    state
     */
    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mExpandViewState.use) {
            if (mExpandViewState.state == ExpandViewState.REQUEST_EXPAND) {
                expandAnimation();
            } else if (mExpandViewState.state == ExpandViewState.REQUEST_COLLAPSE) {
                collapseAnimation();
            }
        } else {
            /*
            额外占用的高度，计算入回收item的区间，在额外的高度之内不回收
             */
            float extra = getHeight() * mItemSpace * getExtraHolderCount();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int position = getPosition(child);
                /*
                当前位置对应的item的top和bottom（包括滚动）是否在RecyclerView的高度加上滚动距离，加上额外高度之内
                不在此范围内则removeAndRecycle
                 */
                if (getHeight() * mItemSpace * position < mScrollOffset - extra ||
                        getHeight() * mItemSpace * (position + 1) > getHeight() + mScrollOffset + extra) {
                    removeAndRecycleViewAt(i, recycler);
                }
            }
            /*
            把剩下的全部detach
             */
            detachAndScrapAttachedViews(recycler);
            for (int i = 0; i < getItemCount(); i++) {
                /*
                同上，计算如果top，bottom在显示范围内，则显示View
                 */
                if (getHeight() * mItemSpace * i >= mScrollOffset - extra &&
                        getHeight() * mItemSpace * (i + 1) <= getHeight() + mScrollOffset + extra) {
                    View child = recycler.getViewForPosition(i);
                    addView(child);
                    measureChild(child, 0, 0);
                    int width = getDecoratedMeasuredWidth(child);
                    int height = getDecoratedMeasuredHeight(child);
                    int top = (int) (getHeight() * mItemSpace * i);
                    layoutDecoratedWithMargins(child, 0, top - mScrollOffset, width, height + top - mScrollOffset);
                    child.post(new ViewTransformRunnable(child, getHeight() * mItemSpace * i - mScrollOffset));
                }
            }
        }
    }

    /**
     * 展开动画
     */
    private void expandAnimation() {
        mExpandViewState.state = ExpandViewState.EXPANDING;
        View v = mExpandViewState.view;
        AnimatorSet set = new AnimatorSet();
        set.setDuration(mExpandViewState.duration);
        ObjectAnimator r = ObjectAnimator.ofFloat(v, "rotationX", mExpandViewState.rotationX, 0f);
        ObjectAnimator s = ObjectAnimator.ofFloat(v, "scaleX", mExpandViewState.scaleX, 1f);
        ObjectAnimator ty = ObjectAnimator.ofFloat(v, "translationY", 0f,
                -(getHeight() * mItemSpace * mExpandViewState.position - mScrollOffset));
        AnimatorSet.Builder builder = set.play(r).with(s).with(ty);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ObjectAnimator t = null;
            if (i < mExpandViewState.index) {
                float offset = getHeight() * mItemSpace * (mExpandViewState.position - (mExpandViewState.index - i)) - mScrollOffset;
                if (offset > 0) {
                    t = ObjectAnimator.ofFloat(child, "translationY", 0f, -offset);
                }
            }
            if (i > mExpandViewState.index) {
                t = ObjectAnimator.ofFloat(child, "translationY", 0f,
                        getHeight() - (getHeight() * mItemSpace * (mExpandViewState.position + i - mExpandViewState.index) - mScrollOffset));
            }
            if (t != null)
                builder.with(t);
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExpandViewState.state = ExpandViewState.EXPANDED;
                mExpandViewState.use = false;
            }
        });
        set.start();
    }

    /**
     * 折叠动画
     */
    private void collapseAnimation() {
        mExpandViewState.state = ExpandViewState.COLLAPSING;
        View v = mExpandViewState.view;
        AnimatorSet set = new AnimatorSet();
        set.setDuration(mExpandViewState.duration);
        ObjectAnimator r = ObjectAnimator.ofFloat(v, "rotationX", 0f, mExpandViewState.rotationX);
        ObjectAnimator s = ObjectAnimator.ofFloat(v, "scaleX", 1f, mExpandViewState.scaleX);
        ObjectAnimator ty = ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY(), 0f);
        AnimatorSet.Builder builder = set.play(r).with(s).with(ty);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (i != mExpandViewState.index) {
                ObjectAnimator t = ObjectAnimator.ofFloat(child, "translationY", child.getTranslationY(), 0f);
                builder.with(t);
            }
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExpandViewState.state = ExpandViewState.COLLAPSED;
                mExpandViewState.use = false;
            }
        });
        set.start();
    }

    /**
     * 上下都不回收该数量的View
     *
     * @return 不回收view的数量
     */
    public int getExtraHolderCount() {
        return 2;
    }

    /**
     * 根据View的Y获得旋转度数
     *
     * @param relativeY View的Y
     * @return 旋转度数
     */
    public float getRelativeDegree(float relativeY) {
        if (getItemCount() == 1)
            return 0f;
        return (mMaxDegree - mMinDegree) * relativeY / getHeight() + mMinDegree;
    }

    /**
     * 根据View的Y获得缩放比例
     *
     * @param relativeY View的Y
     * @return 缩放比例
     */
    public float getRelativeScale(float relativeY) {
        if (getItemCount() == 1)
            return 1f;
        return (mMaxScale - mMinScale) * relativeY / getHeight() + mMinScale;
    }

    @Override
    public boolean canScrollVertically() {
        /*
        不在展开折叠过程中，并且在折叠状态才可以滚动
         */
        return !mExpandViewState.use && mExpandViewState.state == ExpandViewState.COLLAPSED;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy > 0) {
            /*
             如果超出总高度，计算剩余滚动距离
             */
            if (mScrollOffset + getHeight() + dy > getHeight() * mItemSpace * getItemCount()) {
                dy = (int) (getHeight() * mItemSpace * getItemCount() - getHeight() - mScrollOffset);
            }
        } else {
            /*
             如果到顶部，计算剩余滚动距离
             */
            if (mScrollOffset + dy < 0) {
                dy = -mScrollOffset;
            }
        }
        if (dy == 0) return 0;
        mScrollOffset += dy;
        if (mScrollOffset < 0) return 0;
        offsetChildrenVertical(-dy);
        fill(recycler, state);
        return dy;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 初始化View的旋转度数和缩放比例
     *
     * @param view      需要初始化的View
     * @param relativeY View的Y
     */
    public void runViewTransform(final View view, final float relativeY) {
        view.setPivotY(0f);
        view.setScaleX(getRelativeScale(relativeY));
        //view.setScaleY(getRelativeScale(relativeY));
        view.setRotationX(-getRelativeDegree(relativeY));
    }

    /**
     * View初始化Runnable
     */
    class ViewTransformRunnable implements Runnable {

        private final View view;
        private final float relativeY;

        ViewTransformRunnable(final View view, final float relativeY) {
            this.view = view;
            this.relativeY = relativeY;
        }

        @Override
        public void run() {
            runViewTransform(view, relativeY);
        }
    }

    /**
     * 展开折叠状态
     */
    static class ExpandViewState {
        public static final int COLLAPSED = 0;//折叠
        public static final int REQUEST_EXPAND = 1;//请求展开
        public static final int EXPANDING = 2;//展开中
        public static final int EXPANDED = 3;//展开
        public static final int REQUEST_COLLAPSE = 4;//请求折叠
        public static final int COLLAPSING = 5;//折叠中

        boolean use = false;//是否在展开折叠过程中
        View view;
        int index;//ViewGroup中的位置
        int position;//Adapter中的位置
        float rotationX;//原本旋转角度
        float scaleX;//原本缩放比例
        long duration;//动画时长
        int state = COLLAPSED;//当前状态
    }

}
