/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.hitomi.tmlibrary;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 缩略图菜单
 * Created by hitomi on 2016/8/19.
 * email : 196425254@qq.com
 */
public class ThumbnailMenu extends FrameLayout {

  static final float SCALE_RATIO = .45f;

  static final int THUM_MARGIN = 2;

  static final String TAG_SCROLL_LAYOUT = "tag_scroll_layout";

  private ThumbnailFactory factory;

  private ThumbnailAnimator thumbnailAnimator;

  private ThumbnailMenuChooser thumbnailMenuChooser;

  private FrameLayout thumScrollLayout;

  private PagerAdapter pageAdapter;

  private OnThumbnailMenuCloseListener menuCloseListener;

  private List objects;

  private List<TransitionLayout> tranLayoutList;

  /**
   * 缩略图菜单位置方向
   */
  private int direction = ThumbnailFactory.MENU_DIRECTION_BOTTOM;

  /**
   * 缩略图当前缩放比率
   */
  private float scaleRation = SCALE_RATIO;

  /**
   * 第一次初始化标记
   */
  private boolean init = true;

  /**
   * 页面数量
   */
  private int pageCount;

  public ThumbnailMenu(Context context) {
    this(context, null);
  }

  public ThumbnailMenu(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ThumbnailMenu(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbnailMenu);
    direction = typedArray.getInt(R.styleable.ThumbnailMenu_menu_direction, direction);
    scaleRation = typedArray.getFloat(R.styleable.ThumbnailMenu_scale_ratio, scaleRation);
    scaleRation = scaleRation >= 1.f ? 1.f : scaleRation;
    scaleRation = scaleRation <= .1f ? .1f : scaleRation;
    typedArray.recycle();

    init();
  }

  private void init() {

    objects = new ArrayList();
    tranLayoutList = new ArrayList<>();

    thumbnailMenuChooser = new ThumbnailMenuChooser();
    factory = new ThumbnailFactory();
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (init) {
      thumScrollLayout = factory.createMenuContainer(getContext(), direction);
      thumScrollLayout.setTag(TAG_SCROLL_LAYOUT);
      addView(thumScrollLayout, 0);

      thumbnailAnimator = new ThumbnailAnimator(direction, this, tranLayoutList);
      buildingModels();

      init = false;
    }
  }

  /**
   * 绑定页面适配器
   *
   * @param adapter
   */
  public void setAdapter(PagerAdapter adapter) {
    if (pageAdapter != null) {
      pageAdapter.startUpdate(this);
      for (int i = 0; i < adapter.getCount(); i++) {
        pageAdapter.destroyItem((ViewGroup) getChildAt(i), i, objects.get(i));
      }
      pageAdapter.finishUpdate(this);
    }

    pageAdapter = adapter;
    pageCount = pageAdapter.getCount();

    pageAdapter.startUpdate(this);

    LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    for (int i = 0; i < pageCount; i++) {
      TransitionLayout frameLayout = new TransitionLayout(getContext());
      frameLayout.setId(i + 1);// id 不能为0
      frameLayout.setLayoutParams(layoutParams);
      addView(frameLayout);
      tranLayoutList.add(0, frameLayout); // 倒序添加

      Object object = pageAdapter.instantiateItem(frameLayout, i);
      objects.add(object);
    }
    pageAdapter.finishUpdate(this);
  }

  /**
   * 获取缩略图菜单容器中的 ScrollView 中唯一 ViewGroup [这里是LinearLayout]
   *
   * @return
   */
  public ThumbnailContainer getThumContainner() {
    return (ThumbnailContainer) thumScrollLayout.getChildAt(0);
  }

  /**
   * 创建 modelCount 数量的缩略图模型添加到缩略图菜单容器中
   */
  private void buildingModels() {
    final ThumbnailContainer containerLayout = getThumContainner();
    containerLayout.removeAllViews();

    for (int i = 0; i < pageCount; i++) {
      ThumbnailContainer.LayoutParams containerLayoutParams = new ThumbnailContainer.LayoutParams(
          (int) (getWidth() * scaleRation), (int) (getHeight() * scaleRation));
      if (i != 0) {
        if (direction == ThumbnailFactory.MENU_DIRECTION_BOTTOM) {
          containerLayoutParams.leftMargin = THUM_MARGIN;
        } else {
          containerLayoutParams.topMargin = THUM_MARGIN;
        }
      }

      if (direction == ThumbnailFactory.MENU_DIRECTION_BOTTOM) {
        containerLayoutParams.topMargin = getHeight() - containerLayoutParams.height;
      }
      if (direction == ThumbnailFactory.MENU_DIRECTION_RIGHT) {
        containerLayoutParams.leftMargin = getWidth() - containerLayoutParams.width;
      }

      ThumbnailView modelLayout = new ThumbnailView(getContext());
      modelLayout.setTag(i);
      modelLayout.setTransitionLayout(tranLayoutList.get(i));
      modelLayout.setOnClickListener(thumbnailMenuChooser);
      containerLayout.addView(modelLayout, containerLayoutParams);
    }
  }

  /**
   * 打开菜单
   */
  public void openMenu() {
    if (!isOpen()) {
      thumbnailAnimator.openMenuAnimator();
    }
  }

  /**
   * 关闭菜单
   *
   * @param transitionLayout
   */
  private void closeMenu(TransitionLayout transitionLayout) {
    if (isOpen()) {
      thumbnailAnimator.closeMenuAnimator(transitionLayout, menuCloseListener);
    }
  }

  /**
   * 菜单是否打开
   *
   * @return
   */
  public boolean isOpen() {
    if (thumbnailAnimator == null) {
      return false;
    }
    return thumbnailAnimator.isOpen();
  }

  /**
   * 返回缩略图缩放的比率
   *
   * @return
   */
  public float getScaleRatio() {
    return scaleRation;
  }

  public void setOnMenuCloseListener(OnThumbnailMenuCloseListener menuCloseListener) {
    this.menuCloseListener = menuCloseListener;
  }

  public interface OnThumbnailMenuCloseListener {
    void onMenuCloseListener();
  }

  private class ThumbnailMenuChooser implements OnClickListener {

    @Override
    public void onClick(View view) {
      if (view instanceof ThumbnailView) {
        TransitionLayout choosenLayout = ((ThumbnailView) view).getTransitionLayout();
        closeMenu(choosenLayout);
      }
    }
  }

  @Override
  public void bringChildToFront(View child) {
    ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
    detachViewFromParent(child);
    attachViewToParent(child, -1, layoutParams);
    child.setClickable(true);//防止事件透传
    invalidate();
  }

  public void takeChildToBehind(View child) {
    ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
    detachViewFromParent(child);
    attachViewToParent(child, 0, layoutParams);
    invalidate();
  }

}
