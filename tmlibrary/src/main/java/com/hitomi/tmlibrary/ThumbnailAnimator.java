package com.hitomi.tmlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
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
   * 菜单是否打开
   */
  private boolean isOpen = false;
  /**
   * 是否正在执行动画
   */
  private boolean mIsInAnimation = false;
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
   * 菜单关闭时的监听器
   */
  private ThumbnailMenu.OnThumbnailMenuCloseListener onMenuCloseListener;

  /**
   * 菜单方向
   */
  private int direction;

  private boolean init = true;

  public ThumbnailAnimator(int direction, ThumbnailMenu thumbnailMenu,
      List<TransitionLayout> tranLayoutList) {
    this.direction = direction;
    this.thumbnailMenu = thumbnailMenu;
    this.tranLayoutList = tranLayoutList;

    scrollLayout = (FrameLayout) thumbnailMenu.findViewWithTag(ThumbnailMenu.TAG_SCROLL_LAYOUT);
    container = (ThumbnailContainer) scrollLayout.getChildAt(0);
  }

  public boolean isOpen() {
    return isOpen;
  }

  /**
   * 动画模式打开菜单
   */
  public void openMenuAnimator() {
    if (mIsInAnimation || isOpen) {
      return;
    }
    mIsInAnimation = true;
    switch (direction) {
      case ThumbnailFactory.MENU_DIRECTION_LEFT:
      case ThumbnailFactory.MENU_DIRECTION_RIGHT:
        horizontalOpenMenu();
        break;

      case ThumbnailFactory.MENU_DIRECTION_BOTTOM:
        verticalOpenMenu();
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
    if (mIsInAnimation || !isOpen) {
      return;
    }
    mIsInAnimation = true;
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
  private void verticalOpenMenu() {
    float endTranX, endTranY;
    if (init) {
      for (int i = 0; i < tranLayoutList.size(); i++) {
        final TransitionLayout currTranLayout = tranLayoutList.get(i);
        final float tmpTranX =
            currTranLayout.getWidth() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

        endTranX = -tmpTranX + (i * (currTranLayout.getWidth() * thumbnailMenu.getScaleRatio()
            + ThumbnailMenu.THUM_MARGIN));
        endTranY = currTranLayout.getHeight() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

        AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, currTranLayout);
        addOpenMenuAnimatorSetListener(animSet, currTranLayout, i);
      }
      init = false;
    } else {
      TransitionLayout showingTranLayout =
          (TransitionLayout) thumbnailMenu.getChildAt(thumbnailMenu.getChildCount() - 1);

      float singleThumMenuWidth = showingTranLayout.getWidth() * thumbnailMenu.getScaleRatio();
      int menuIndex = tranLayoutList.indexOf(showingTranLayout);


      HorizontalScrollView scrollView = (HorizontalScrollView) scrollLayout;

      // 当前 ScrollView 滚动的距离
      int scrollDistance = scrollView.getScrollX();
      float tmpTranX = showingTranLayout.getWidth() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

      endTranX = -tmpTranX + (menuIndex * (singleThumMenuWidth + ThumbnailMenu.THUM_MARGIN))
          - scrollDistance;
      endTranY = showingTranLayout.getHeight() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

      AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, showingTranLayout);
      addOpenMenuAnimatorSetListener(animSet, showingTranLayout, menuIndex);
    }
  }

  /**
   * 纵向(左侧或者右侧)关闭菜单，并显示选中的 TransitionLayout
   *
   * @param transitionLayout 选中页面所在的 TransitionLayout
   */
  private void verticalCloseMenu(final TransitionLayout transitionLayout) {
    thumbnailMenu.bringChildToFront(transitionLayout);

    HorizontalScrollView scrollView = (HorizontalScrollView) scrollLayout;
    // 当前 ScrollView 滚动的距离
    final int scrollDistance = scrollView.getScrollX();

    final int choosenMenuIndex = tranLayoutList.indexOf(transitionLayout);
    ThumbnailView frameLayout = (ThumbnailView) container.getChildAt(choosenMenuIndex);
    // 当前选中的 TransitionLayout 所在模型的 Left
    final int currTranLeft = frameLayout.getLeft();

    float offsetX = (thumbnailMenu.getWidth() - frameLayout.getWidth()) * .5f;
    float offsetY = (thumbnailMenu.getHeight() - frameLayout.getHeight()) * .5f;
    transitionLayout.setTranslationX(currTranLeft - scrollDistance - offsetX);
    transitionLayout.setTranslationY(offsetY);

    AnimatorSet animSet = makeCloseMenuAnimatorSet(transitionLayout, 0, 0);
    addCloseMenuAnimatorSetListener(animSet);
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
        endTranY = -tmpTranY + (i * (currTranLayout.getHeight() * thumbnailMenu.getScaleRatio()
            + ThumbnailMenu.THUM_MARGIN));

        endTranX = direction == ThumbnailFactory.MENU_DIRECTION_LEFT ? -endTranX : endTranX;
        AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, currTranLayout);
        addOpenMenuAnimatorSetListener(animSet, currTranLayout, i);
      }
      init = false;
    } else {
      TransitionLayout showingTranLayout =
          (TransitionLayout) thumbnailMenu.getChildAt(thumbnailMenu.getChildCount() - 1);

      float singleThumMenuHeight = showingTranLayout.getHeight() * thumbnailMenu.getScaleRatio();
      int menuIndex = tranLayoutList.indexOf(showingTranLayout);

      ScrollView scrollView = (ScrollView) scrollLayout;

      // 当前 ScrollView 滚动的距离
      int scrollDistance = scrollView.getScrollY();
      float tmpTranY = showingTranLayout.getHeight() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;

      endTranX = showingTranLayout.getWidth() * (1.f - thumbnailMenu.getScaleRatio()) * .5f;
      endTranY = -tmpTranY + (menuIndex * (singleThumMenuHeight + ThumbnailMenu.THUM_MARGIN))
          - scrollDistance;

      endTranX = direction == ThumbnailFactory.MENU_DIRECTION_LEFT ? -endTranX : endTranX;
      AnimatorSet animSet = makeOpenMenuAnimatorSet(endTranX, endTranY, showingTranLayout);
      addOpenMenuAnimatorSetListener(animSet, showingTranLayout, menuIndex);
    }

  }

  /**
   * 横向(左侧或者右侧)关闭菜单，并显示选中的 TransitionLayout
   *
   * @param transitionLayout 选中页面所在的TransitionLayout
   */
  private void horizontalCloseMenu(final TransitionLayout transitionLayout) {
    thumbnailMenu.bringChildToFront(transitionLayout);

    // 当前 ScrollView 滚动的距离
    final int scrollDistance = scrollLayout.getScrollY();

    final int choosenMenuIndex = tranLayoutList.indexOf(transitionLayout);
    ThumbnailView frameLayout = (ThumbnailView) container.getChildAt(choosenMenuIndex);
    // 当前选中的 TransitionLayout 所在模型的 Top
    final int currTranTop = frameLayout.getTop();

    float offsetX = (thumbnailMenu.getWidth() - frameLayout.getWidth()) * .5f;
    float translationX =
        direction == ThumbnailFactory.MENU_DIRECTION_LEFT ? -offsetX : offsetX;
    transitionLayout.setTranslationX(translationX);
    transitionLayout.setTranslationY(currTranTop - scrollDistance
        - (thumbnailMenu.getHeight() - frameLayout.getHeight()) * .5f);

    AnimatorSet animSet = makeCloseMenuAnimatorSet(transitionLayout, 0, 0);
    addCloseMenuAnimatorSetListener(animSet);

  }

  private void addCloseMenuAnimatorSetListener(AnimatorSet animSet) {

    animSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        if (onMenuCloseListener != null) {
          onMenuCloseListener.onMenuCloseListener();
        }
        isOpen = false;
        mIsInAnimation = false;
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
  private AnimatorSet makeOpenMenuAnimatorSet(float endTranX, float endTranY,
      TransitionLayout transitionLayout) {
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
    animSet.play(scaleXAnima).with(scaleYAnima).with(tranXAnima).with(tranYAnima);
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
  private AnimatorSet makeCloseMenuAnimatorSet(TransitionLayout transitionLayout, float endTranX,
      float endTranY) {
    ObjectAnimator scaleXAnima = ObjectAnimator.ofFloat(
        transitionLayout, "scaleX", transitionLayout.getScaleX(),
        transitionLayout.getScaleX() / thumbnailMenu.getScaleRatio());
    ObjectAnimator scaleYAnima = ObjectAnimator.ofFloat(
        transitionLayout, "scaleY", transitionLayout.getScaleX(),
        transitionLayout.getScaleY() / thumbnailMenu.getScaleRatio());

    float currTranX = transitionLayout.getTranslationX();

    ObjectAnimator tranXAnima = ObjectAnimator.ofFloat(
        transitionLayout, "translationX", currTranX, endTranX);
    ObjectAnimator tranYAnima = ObjectAnimator.ofFloat(
        transitionLayout, "translationY", transitionLayout.getTranslationY(), endTranY);

    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(300);
    animSet.play(tranXAnima).with(tranYAnima).with(scaleXAnima).with(scaleYAnima);
    animSet.setInterpolator(interpolator);
    animSet.start();
    return animSet;
  }

  /**
   * 为菜单打开动画集添加一个监听器
   * 
   * @param animSet
   * @param transitionLayout
   * @param index
   */
  private void addOpenMenuAnimatorSetListener(AnimatorSet animSet,
      final TransitionLayout transitionLayout,
      final int index) {
    animSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {

        // ThumbnailView frameLayout = (ThumbnailView) container.getChildAt(index);
        // frameLayout.setVisibility(View.VISIBLE);

        // 移到屏幕外进行隐藏处理
        transitionLayout.setTranslationY(transitionLayout.getHeight());

        isOpen = true;
        mIsInAnimation = false;
      }
    });
  }
}
