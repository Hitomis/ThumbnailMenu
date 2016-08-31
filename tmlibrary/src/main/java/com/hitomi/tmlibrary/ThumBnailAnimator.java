package com.hitomi.tmlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.List;

/**
 * Created by hitomi on 2016/8/26.
 */
public class ThumbnailAnimator {

    private final Interpolator interpolator = new OvershootInterpolator();

    /**
     * 菜单方向
     */
    private int direction;

    /**
     * 缩略图容器布局
     */
    private ThumbnailLayout tmLayout;

    /**
     * Fragment 容器布局集合
     */
    private List<TransitionLayout> tranLayoutList;

    private boolean init = true;

    public ThumbnailAnimator(int direction, ThumbnailLayout tmLayout, List<TransitionLayout> tranLayoutList) {
        this.direction = direction;
        this.tmLayout = tmLayout;
        this.tranLayoutList = tranLayoutList;
    }

    public void openMenuAnimator() {
        switch (direction) {
            case ThumbnailFactory.MENU_DIRECTION_LEFT:
                openLeftMenu();
                break;
        }
    }

    public void closeMenuAnimator(TransitionLayout transitionLayout) {
        switch (direction) {
            case ThumbnailFactory.MENU_DIRECTION_LEFT:
                closeLeftMenu(transitionLayout);
                break;
        }
    }

    /**
     * 从左侧打开菜单
     */
    private void openLeftMenu() {
        float endTranX, endTranY;
        if (init) {
            for (int i = 0; i < tranLayoutList.size(); i++) {
                TransitionLayout currTranLayout = tranLayoutList.get(i);
                float tmpTranY = currTranLayout.getHeight() * (1.f - ThumbnailMenu.SCALE_RATIO) * .5f;

                endTranX = currTranLayout.getWidth() * (1.f - ThumbnailMenu.SCALE_RATIO) * .5f;
                endTranY = -tmpTranY + (i * currTranLayout.getHeight() * ThumbnailMenu.SCALE_RATIO);

                AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, currTranLayout);
                addOpenMenuAnimatorSetListener(animSet, currTranLayout, tmpTranY, i, endTranX);
            }
            init = false;
        } else {
            ThumbnailMenu menu = (ThumbnailMenu) tmLayout.getParent();
            TransitionLayout showingTranLayout = (TransitionLayout) menu.getChildAt(menu.getChildCount() - 1);

            float singleThumMenuHeight = showingTranLayout.getHeight() * ThumbnailMenu.SCALE_RATIO;
            int menuIndex = tranLayoutList.indexOf(showingTranLayout);

            // 调整菜单滚动的位置
            int adjustScrollLength = (int) (menuIndex * (singleThumMenuHeight + ThumbnailMenu.THUM_TOP_MARGIN));
            ScrollView scrollView = (ScrollView) tmLayout.getContainner().getParent();
            scrollView.scrollTo(0, adjustScrollLength);

            // 当前 ScrollView 滚动的距离
            int scrollDistance = scrollView.getScrollY();
            float tmpTranY = showingTranLayout.getHeight() * (1.f - ThumbnailMenu.SCALE_RATIO) * .5f;

            endTranX = showingTranLayout.getWidth() * (1.f - ThumbnailMenu.SCALE_RATIO) * .5f;
            endTranY = -tmpTranY + (menuIndex * singleThumMenuHeight) - scrollDistance;

            AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, showingTranLayout);
            addOpenMenuAnimatorSetListener(animSet, showingTranLayout, tmpTranY, menuIndex, endTranX);
        }

    }

    /**
     * 从左侧关闭菜单，并显示选中的 TransitionLayout
     * @param transitionLayout
     */
    private void closeLeftMenu(final TransitionLayout transitionLayout) {
        final ThumbnailMenu menu = (ThumbnailMenu) tmLayout.getParent();
        final ThumbnailContainer container = tmLayout.getContainner();

        // 当前 ScrollView 滚动的距离
        final int scrollDistance = ((ScrollView) container.getParent()).getScrollY();
        // 当前选中的 TransitionLayout 所在模型的 Top
        final int currTranTop = ((FrameLayout) transitionLayout.getParent()).getTop();

        // 将选中 TransitionLayout 从其所在模型中移除并放置在 ThumbnailMenu 相对模型所在同一个位置上
        final FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                transitionLayout.getWidth(),
                transitionLayout.getHeight());

        frameParams.topMargin = currTranTop - scrollDistance;
        transitionLayout.setLayoutParams(frameParams);
        final int choosenMenuIndex = tranLayoutList.indexOf(transitionLayout);
        FrameLayout frameLayout = (FrameLayout) container.getChildAt(choosenMenuIndex);
        frameLayout.removeView(transitionLayout);
        menu.addView(transitionLayout);

        float endTranX = (tmLayout.getWidth() - transitionLayout.getWidth()) * .5f;
        float endTranY = (tmLayout.getHeight() - transitionLayout.getHeight()) * .5f - currTranTop + scrollDistance;

        AnimatorSet animSet = makeCloseMenuAnimatorSet(transitionLayout, endTranX, endTranY);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                frameParams.topMargin = 0;
                // 还原选中的 TransitionLayout
                transitionLayout.setTranslationX(0);
                transitionLayout.setTranslationY(0);
                transitionLayout.setLayoutParams(frameParams);
            }
        });

    }

    /**
     * 创建打开菜单的动画集
     * @param endTranX
     * @param endTranY
     * @param transitionLayout
     * @return
     */
    @NonNull
    private AnimatorSet makeOpenMenuAnimatorSet(float endTranX, float endTranY, TransitionLayout transitionLayout) {
        ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleX", transitionLayout.getScaleX(), ThumbnailMenu.SCALE_RATIO);
        ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleY", transitionLayout.getScaleY(), ThumbnailMenu.SCALE_RATIO);

        ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationX", transitionLayout.getTranslationX(), -endTranX);
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
     * @param transitionLayout
     * @param endTranX
     * @param endTranY
     * @return
     */
    @NonNull
    private AnimatorSet makeCloseMenuAnimatorSet(TransitionLayout transitionLayout, float endTranX, float endTranY) {
        ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleX", transitionLayout.getScaleX(), transitionLayout.getScaleX() / ThumbnailMenu.SCALE_RATIO);
        ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleY", transitionLayout.getScaleX(), transitionLayout.getScaleY() / ThumbnailMenu.SCALE_RATIO);

        ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationX", transitionLayout.getTranslationX(), endTranX);
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
     * @param animSet
     * @param transitionLayout
     * @param tmpTranY
     * @param index
     * @param finalEndTranX
     */
    private void addOpenMenuAnimatorSetListener(AnimatorSet animSet, final TransitionLayout transitionLayout,
                                                final float tmpTranY, final int index, final float finalEndTranX) {
        final ThumbnailMenu menu = (ThumbnailMenu) tmLayout.getParent();
        final ThumbnailContainer container = tmLayout.getContainner();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                menu.removeViewAt(menu.indexOfChild(transitionLayout));

                // 设置为原来的宽高大小，是因为之前缩小过
                FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                        transitionLayout.getWidth(),
                        transitionLayout.getHeight()
                );

                FrameLayout frameLayout = (FrameLayout) container.getChildAt(index);
                transitionLayout.setTranslationX(-finalEndTranX);
                transitionLayout.setTranslationY(-tmpTranY);
                frameLayout.addView(transitionLayout, frameParams);
            }
        });
    }
}
