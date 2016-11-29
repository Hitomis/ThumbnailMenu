/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.hitomi.tmlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LukeSkywalker on 2016/9/29.
 */
public class ThumbnailView extends TransitionLayout {
  private TransitionLayout mTransitionLayout;

  public ThumbnailView(Context context) {
    super(context);
  }

  public ThumbnailView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ThumbnailView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setTransitionLayout(TransitionLayout transitionLayout) {
    mTransitionLayout = transitionLayout;
    removeAllViews();
    View mView = new View(getContext());
    addView(mView);
  }

  public TransitionLayout getTransitionLayout() {
    return mTransitionLayout;
  }

  @Override
  protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
    if (mTransitionLayout != null) {
      float sw = getMeasuredWidth() / (float) mTransitionLayout.getWidth();
      float sh = getMeasuredHeight() / (float) mTransitionLayout.getHeight();
      canvas.scale(sw, sh);
      mTransitionLayout.draw(canvas);
    }
    return super.drawChild(canvas, child, drawingTime);
  }
}
