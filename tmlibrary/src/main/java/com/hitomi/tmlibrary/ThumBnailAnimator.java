package com.hitomi.tmlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.List;

/**
 * Created by hitomi on 2016/8/26.
 */
public class ThumbnailAnimator {

    private final Interpolator interpolator = new LinearInterpolator();

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

    private void openLeftMenu() {
        final ThumbnailMenu menu = (ThumbnailMenu) tmLayout.getParent();
        final ThumbnailContainer containner = tmLayout.getContainner();

        // 当前 ScrollView 滚动的距离
        final int scrollDistance = ((ScrollView) containner.getParent()).getScrollY();

        float endTranX, endTranY;
        for (int i = 0; i < tranLayoutList.size(); i++) {
            final TransitionLayout transitionLayout = tranLayoutList.get(i);
            final float tmpTranY = transitionLayout.getHeight() * (1.f - ThumbnailMenu.scaleRatio) * .5f;

            endTranX = transitionLayout.getWidth() * (1.f - ThumbnailMenu.scaleRatio) * .5f;
            endTranY = -tmpTranY + (i * transitionLayout.getHeight() * ThumbnailMenu.scaleRatio) - scrollDistance;

            ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(
                    transitionLayout, "scaleX", transitionLayout.getScaleX(), ThumbnailMenu.scaleRatio);
            ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(
                    transitionLayout, "scaleY", transitionLayout.getScaleY(), ThumbnailMenu.scaleRatio);

            ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
                    transitionLayout, "translationX", transitionLayout.getTranslationX(), -endTranX);
            ObjectAnimator tranYAnima = ObjectAnimator.ofFloat(
                    transitionLayout, "translationY", transitionLayout.getTranslationY(), endTranY);

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(300);
            animSet.play(scaleXAnima).with(scaleYAnima).with(tranXAnima).with(tranYAnima);
            animSet.setInterpolator(interpolator);
            animSet.start();

            final int index = i;
            final float finalEndTranX = endTranX;
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    menu.removeViewAt(menu.indexOfChild(transitionLayout));

                    // 设置为原来的宽高大小，是因为之前缩小过
                    FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                            transitionLayout.getWidth(),
                            transitionLayout.getHeight()
                    );

                    FrameLayout frameLayout = (FrameLayout) containner.getChildAt(index);
                    transitionLayout.setTranslationX(-finalEndTranX);
                    transitionLayout.setTranslationY(-tmpTranY);
                    frameLayout.addView(transitionLayout, frameParams);
                }
            });
        }
    }

    private void closeLeftMenu(final TransitionLayout transitionLayout) {
        final ThumbnailMenu menu = (ThumbnailMenu) tmLayout.getParent();
        final ThumbnailContainer container = tmLayout.getContainner();

        // 当前 ScrollView 滚动的距离
        final int scrollDistance = ((ScrollView) transitionLayout.getParent().getParent().getParent()).getScrollY();
        // 当前选中的 TransitionLayout 所在模型的 Top
        final int currTranTop = ((FrameLayout)transitionLayout.getParent()).getTop();

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

        ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleX", transitionLayout.getScaleX(), transitionLayout.getScaleX() / ThumbnailMenu.scaleRatio);
        ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "scaleY", transitionLayout.getScaleX(), transitionLayout.getScaleY() / ThumbnailMenu.scaleRatio);

        ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationX", transitionLayout.getTranslationX(), endTranX);
        ObjectAnimator tranYAnima = ObjectAnimator.ofFloat(
                transitionLayout, "translationY", transitionLayout.getTranslationY(), endTranY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.play(tranXAnima).with(tranYAnima).with(scaleXAnima).with(scaleYAnima);
        animSet.setInterpolator(interpolator);
        animSet.start();

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                frameParams.topMargin = 0;

                // 1. 还原选中的 TransitionLayout
                transitionLayout.setTranslationX(0);
                transitionLayout.setTranslationY(0);
                transitionLayout.setLayoutParams(frameParams);

                // 2. 移除菜单容器中模型中的 TransitionLayout
                int modleCount = container.getChildCount();
                FrameLayout modleLayout;
                for (int i = 0; i < modleCount; i++) {
                    modleLayout = (FrameLayout) container.getChildAt(i);
                    modleLayout.removeAllViews();
                }

                // 3. 还原其他的 TransitionLayout
                TransitionLayout tranLayout;
                // 获取当前选中的 TransitionLayout 在 ThumbnailMenu 布局层次的下标位置
                int tempIndex = menu.indexOfChild(transitionLayout);
                frameParams.topMargin = 0;
                for (int i = tranLayoutList.size() - 1; i >= 0; i--) {
                    tranLayout = tranLayoutList.get(i);
                    if (i != choosenMenuIndex) {
                        tranLayout.setScaleX(1.0f);
                        tranLayout.setScaleY(1.0f);
                        tranLayout.setTranslationX(0);
                        tranLayout.setTranslationY(0);
                        menu.addView(tranLayout, tempIndex++, frameParams);
                    }
                }
            }
        });

    }

}
