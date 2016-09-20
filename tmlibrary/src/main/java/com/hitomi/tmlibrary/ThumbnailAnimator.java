package com.hitomi.tmlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.util.List;

/**
 * 打开或关闭菜单动画实现类 <br/>
 * Created by hitomi on 2016/8/26.
 * email : 196425254@qq.com
 */
public class ThumbnailAnimator {

    private final Interpolator interpolator = new OvershootInterpolator();

    /**
     * 缩略图菜单
     */
    private ThumbnailMenu thumbnailMenu;

    /**
     * 用于放置缩略图菜单的容器 ScrollView
     */
    private final FrameLayout scrollLayout;

    /**
     * 用于放置缩略图菜单的容器 ViewGroup
     */
    private ThumbnailContainer container;

    /**
     * Fragment 容器布局集合
     */
    private List<TransitionLayout> tranLayoutList;

    /**
     *菜单关闭时的监听器
     */
    private ThumbnailMenu.OnThumbnailMenuCloseListener onMenuCloseListener;

    /**
     * 菜单方向
     */
    private int direction;

    private boolean init = true;

    public ThumbnailAnimator(int direction, ThumbnailMenu thumbnailMenu, List<TransitionLayout> tranLayoutList) {
        this.direction = direction;
        this.thumbnailMenu = thumbnailMenu;
        this.tranLayoutList = tranLayoutList;

        scrollLayout = (FrameLayout) thumbnailMenu.findViewWithTag(ThumbnailMenu.TAG_SCROLL_LAYOUT);
        container = (ThumbnailContainer) scrollLayout.getChildAt(0);
    }

    /**
     * 动画模式打开菜单
     */
    public void openMenuAnimator() {
        scrollLayout.setVisibility(View.VISIBLE);
        switch (direction) {
            case ThumbnailFactory.MENU_DIRECTION_LEFT:
            case ThumbnailFactory.MENU_DIRECTION_RIGHT:
                horizontalOpenMenu();
                break;

            case ThumbnailFactory.MENU_DIRECTION_BOTTOM:
                verticalOpenMun();
                break;

        }
    }

    /**
     * 动画模式关闭菜单，并显示 transitionLayout
     *
     * @param transitionLayout 选中页面所在的 TransitionLayout
     * @param onMenuCloseListener
     */
    public void closeMenuAnimator(TransitionLayout transitionLayout,
                                  ThumbnailMenu.OnThumbnailMenuCloseListener onMenuCloseListener) {
        this.onMenuCloseListener = onMenuCloseListener;
        switch (direction) {
            case ThumbnailFactory.MENU_DIRECTION_LEFT:
            case ThumbnailFactory.MENU_DIRECTION_RIGHT:
                horizontalCloseMenu(transitionLayout);
                break;

            case ThumbnailFactory.MENU_DIRECTION_BOTTOM:
                verticalCloseMenu(transitionLayout);
                break;
        }
    }

    /**
     * 纵向打开菜单
     */
    private void verticalOpenMun() {
        float endTranX, endTranY;
        if (init) {
            for (int i = 0; i < tranLayoutList.size(); i++) {
                final TransitionLayout currTranLayout = tranLayoutList.get(i);
                final float tmpTranX = currTranLayout.getWidth() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

                endTranX = -tmpTranX + (i * (currTranLayout.getWidth() * thumbnailMenu.getScaleRatio() + ThumbnailMenu.THUM_MARGIN));
                endTranY = currTranLayout.getHeight() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

                AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, currTranLayout);
                addOpenMenuAnimatorSetListener(animSet, currTranLayout, endTranY, i, tmpTranX);
            }
            init = false;
        } else {
            TransitionLayout showingTranLayout = (TransitionLayout) thumbnailMenu.getChildAt(thumbnailMenu.getChildCount() - 1);

            float singleThumMenuWidth = showingTranLayout.getWidth() * thumbnailMenu.getScaleRatio();
            int menuIndex = tranLayoutList.indexOf(showingTranLayout);

            // 调整菜单滚动的位置
            int adjustScrollPosition = (int) (menuIndex * (singleThumMenuWidth + ThumbnailMenu.THUM_MARGIN));
            HorizontalScrollView scrollView = (HorizontalScrollView) scrollLayout;
            scrollView.scrollTo(adjustScrollPosition, 0);

            // 当前 ScrollView 滚动的距离
            int scrollDistance = scrollView.getScrollX();
            float tmpTranX = showingTranLayout.getWidth() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

            endTranX = -tmpTranX + (menuIndex * (singleThumMenuWidth + ThumbnailMenu.THUM_MARGIN)) - scrollDistance;
            endTranY = showingTranLayout.getHeight() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

            AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, showingTranLayout);
            addOpenMenuAnimatorSetListener(animSet, showingTranLayout, endTranY, menuIndex, tmpTranX);
        }
    }

    /**
     * 纵向(左侧或者右侧)关闭菜单，并显示选中的 TransitionLayout
     *
     * @param transitionLayout 选中页面所在的 TransitionLayout
     */
    private void verticalCloseMenu(final TransitionLayout transitionLayout) {
        HorizontalScrollView scrollView = (HorizontalScrollView) scrollLayout;
        // 当前 ScrollView 滚动的距离
        final int scrollDistance = scrollView.getScrollX();
        // 当前选中的 TransitionLayout 所在模型的 Left
        final int currTranLeft = ((FrameLayout) transitionLayout.getParent()).getLeft();

        int tranTop = ((FrameLayout) transitionLayout.getParent()).getTop();
        // 将选中 TransitionLayout 从其所在模型中移除并放置在 ThumbnailMenu 相对模型所在同一个位置上
        final int choosenMenuIndex = tranLayoutList.indexOf(transitionLayout);
        FrameLayout frameLayout = (FrameLayout) container.getChildAt(choosenMenuIndex);
        frameLayout.removeView(transitionLayout);

        final FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                transitionLayout.getWidth(),
                transitionLayout.getHeight());
        frameParams.leftMargin = currTranLeft - scrollDistance;
        frameParams.topMargin = tranTop;
        transitionLayout.setLayoutParams(frameParams);

        thumbnailMenu.addView(transitionLayout);

        float endTranX = (thumbnailMenu.getWidth() - transitionLayout.getWidth()) * .5f - currTranLeft + scrollDistance;
        float endTranY = -frameParams.topMargin;

        AnimatorSet animSet = makeCloseMenuAnimatorSet(transitionLayout, endTranX, endTranY);
        addCloseMenuAnimatorSetListener(transitionLayout, frameParams, animSet);
    }

    /**
     * 横向(左侧或者右侧)打开菜单
     */
    private void horizontalOpenMenu() {
        float endTranX, endTranY;
        if (init) {
            for (int i = 0; i < tranLayoutList.size(); i++) {
                TransitionLayout currTranLayout = tranLayoutList.get(i);
                float tmpTranY = currTranLayout.getHeight() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

                endTranX = currTranLayout.getWidth() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;
                endTranY = -tmpTranY + (i * (currTranLayout.getHeight() * thumbnailMenu.getScaleRatio() + ThumbnailMenu.THUM_MARGIN));

                AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, currTranLayout);
                addOpenMenuAnimatorSetListener(animSet, currTranLayout, tmpTranY, i, endTranX);
            }
            init = false;
        } else {
            TransitionLayout showingTranLayout = (TransitionLayout) thumbnailMenu.getChildAt(thumbnailMenu.getChildCount() - 1);

            float singleThumMenuHeight = showingTranLayout.getHeight() * thumbnailMenu.getScaleRatio();
            int menuIndex = tranLayoutList.indexOf(showingTranLayout);

            // 调整菜单滚动的位置
            int adjustScrollPosition = (int) (menuIndex * (singleThumMenuHeight + ThumbnailMenu.THUM_MARGIN));
            ScrollView scrollView = (ScrollView) scrollLayout;
            scrollView.scrollTo(0, adjustScrollPosition);

            // 当前 ScrollView 滚动的距离
            int scrollDistance = scrollView.getScrollY();
            float tmpTranY = showingTranLayout.getHeight() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

            endTranX = showingTranLayout.getWidth() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;
            endTranY = -tmpTranY + (menuIndex * (singleThumMenuHeight + ThumbnailMenu.THUM_MARGIN)) - scrollDistance;

            AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, showingTranLayout);
            addOpenMenuAnimatorSetListener(animSet, showingTranLayout, tmpTranY, menuIndex, endTranX);
        }

    }

    /**
     * 横向(左侧或者右侧)关闭菜单，并显示选中的 TransitionLayout
     *
     * @param transitionLayout 选中页面所在的TransitionLayout
     */
    private void horizontalCloseMenu(final TransitionLayout transitionLayout) {
        // 当前 ScrollView 滚动的距离
        final int scrollDistance = scrollLayout.getScrollY();
        // 当前选中的 TransitionLayout 所在模型的 Top
        final int currTranTop = ((FrameLayout) transitionLayout.getParent()).getTop();

        // 将选中 TransitionLayout 从其所在模型中移除并放置在 ThumbnailMenu 相对模型所在同一个位置上
        final int choosenMenuIndex = tranLayoutList.indexOf(transitionLayout);
        FrameLayout frameLayout = (FrameLayout) container.getChildAt(choosenMenuIndex);
        frameLayout.removeView(transitionLayout);

        final FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                transitionLayout.getWidth(),
                transitionLayout.getHeight());
        frameParams.topMargin = currTranTop - scrollDistance;
        transitionLayout.setLayoutParams(frameParams);

        thumbnailMenu.addView(transitionLayout);

        float endTranX = (thumbnailMenu.getWidth() - transitionLayout.getWidth()) * .5f;
        float endTranY = (thumbnailMenu.getHeight() - transitionLayout.getHeight()) * .5f - currTranTop + scrollDistance;

        AnimatorSet animSet = makeCloseMenuAnimatorSet(transitionLayout, endTranX, endTranY);
        addCloseMenuAnimatorSetListener(transitionLayout, frameParams, animSet);

    }

    private void addCloseMenuAnimatorSetListener(final TransitionLayout transitionLayout,
                                                 final FrameLayout.LayoutParams frameParams,
                                                 AnimatorSet animSet) {

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                frameParams.leftMargin = 0;
                frameParams.topMargin = 0;
                // 还原选中的 TransitionLayout
                transitionLayout.setTranslationX(0);
                transitionLayout.setTranslationY(0);
                transitionLayout.setLayoutParams(frameParams);

                if (onMenuCloseListener != null) {
                    onMenuCloseListener.onMenuCloseListener();
                }
                scrollLayout.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 创建打开菜单的动画集
     *
     * @param endTranX
     * @param endTranY
     * @param transitionLayout
     * @return
     */
    @NonNull
    private AnimatorSet makeOpenMenuAnimatorSet(float endTranX, float endTranY, TransitionLayout transitionLayout) {
        endTranX = direction == ThumbnailFactory.MENU_DIRECTION_LEFT ? -endTranX : endTranX;

        ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleX", transitionLayout.getScaleX(), thumbnailMenu.getScaleRatio());
        ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleY", transitionLayout.getScaleY(), thumbnailMenu.getScaleRatio());

        ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationX", transitionLayout.getTranslationX(), endTranX);
        ObjectAnimator tranYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationY", transitionLayout.getTranslationY(), endTranY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.setInterpolator(interpolator);
        animSet.play(scaleXAnima).with(scaleYAnima).before(tranXAnima).before(tranYAnima);
        animSet.start();
        return animSet;
    }

    /**
     * 创建关闭菜单的动画集
     *
     * @param transitionLayout
     * @param endTranX
     * @param endTranY
     * @return
     */
    @NonNull
    private AnimatorSet makeCloseMenuAnimatorSet(TransitionLayout transitionLayout, float endTranX, float endTranY) {
        ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleX", transitionLayout.getScaleX(), transitionLayout.getScaleX() / thumbnailMenu.getScaleRatio());
        ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleY", transitionLayout.getScaleX(), transitionLayout.getScaleY() / thumbnailMenu.getScaleRatio());

        float currTranX = direction == ThumbnailFactory.MENU_DIRECTION_RIGHT ?
                -transitionLayout.getTranslationX() :
                transitionLayout.getTranslationX();

        ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationX", currTranX, endTranX);
        ObjectAnimator tranYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationY", transitionLayout.getTranslationY(), endTranY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.play(tranXAnima).with(tranYAnima).before(scaleXAnima).before(scaleYAnima);
        animSet.setInterpolator(interpolator);
        animSet.start();
        return animSet;
    }

    /**
     * 为菜单打开动画集添加一个监听器
     *
     * @param animSet
     * @param transitionLayout
     * @param tmpTranY
     * @param index
     * @param finalEndTranX
     */
    private void addOpenMenuAnimatorSetListener(AnimatorSet animSet, final TransitionLayout transitionLayout,
                                                final float tmpTranY, final int index, final float finalEndTranX) {
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 1. 将 transitionLayout 从 menu 容器中移除
                thumbnailMenu.removeViewAt(thumbnailMenu.indexOfChild(transitionLayout));

                // 2. 将 transitionLayout 放入菜单容器中
                // 设置为原来的宽高大小，是因为之前缩小过
                FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                        transitionLayout.getWidth(),
                        transitionLayout.getHeight()
                );
                FrameLayout frameLayout = (FrameLayout) container.getChildAt(index);
                // 调整位置
                transitionLayout.setTranslationX(-finalEndTranX);
                transitionLayout.setTranslationY(-tmpTranY);
                // 将缩小后的 transitionLayout 放入模型中
                frameLayout.addView(transitionLayout, frameParams);
            }
        });
    }
}
