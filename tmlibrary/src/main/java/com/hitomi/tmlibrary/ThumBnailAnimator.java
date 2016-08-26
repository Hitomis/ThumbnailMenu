package com.hitomi.tmlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by hitomi on 2016/8/26.
 */
public class ThumbnailAnimator {

    private int direction;

    public ThumbnailAnimator(int direction) {
        this.direction = direction;
    }

    public void createMenuAnimtor(ThumbnailLayout tmLayout, List<TransitionLayout> tranLayoutList) {
        switch (direction) {
            case ThumbnailStyleFactory.MENU_DIRECTION_LEFT:
                makeleftAnimator(tmLayout, tranLayoutList);
                break;
        }
    }

    private void makeleftAnimator(ThumbnailLayout tmLayout, List<TransitionLayout> tranLayoutList) {
        final ThumbnailMenu menu = (ThumbnailMenu) tmLayout.getParent();
        final ViewGroup containner = tmLayout.getContainner();

        int totalLayoutSize = tranLayoutList.size();
        FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();

        final float scaleRatio = .618f;
        float endTranX, endTranY;
        for (int i = 0; i < tranLayoutList.size(); i++) {
            final TransitionLayout transitionLayout = tranLayoutList.get(i);

            endTranX = transitionLayout.getWidth() * (1.f - scaleRatio) * .5f;
            endTranY = -transitionLayout.getHeight() * (1.f - scaleRatio) * .5f + (i * transitionLayout.getHeight() * scaleRatio);

            ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(transitionLayout, "scaleX", 1.f, scaleRatio);
            ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(transitionLayout, "scaleY", 1.f, scaleRatio);
            ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
                    transitionLayout, "translationX", transitionLayout.getTranslationX(), -endTranX);
            ObjectAnimator tranYAnima = ObjectAnimator.ofFloat(
                    transitionLayout, "translationY", transitionLayout.getTranslationY(), endTranY);

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(600);
            animSet.play(scaleXAnima).with(scaleYAnima).with(tranXAnima).with(tranYAnima);
            animSet.setInterpolator(fastOutLinearInInterpolator);
            animSet.start();

            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    menu.removeViewAt(menu.indexOfChild(transitionLayout));

                    LinearLayout.LayoutParams linlayParams = new LinearLayout.LayoutParams(
                            (int)(transitionLayout.getWidth() * scaleRatio),
                            (int)(transitionLayout.getHeight() * scaleRatio)
                    );
                    transitionLayout.setScaleX(1.f);
                    transitionLayout.setScaleY(1.f);
                    transitionLayout.setTranslationX(0);
                    transitionLayout.setTranslationY(0);

                    containner.addView(transitionLayout, linlayParams);
                }
            });
        }
    }

}
