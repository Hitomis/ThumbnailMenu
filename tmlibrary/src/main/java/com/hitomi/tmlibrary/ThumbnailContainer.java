package com.hitomi.tmlibrary;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * ScrollView/HorizontalScrollView 中的 唯一子 ViewGroup <br/>
 * 用于放置缩略图模型
 * Created by hitomi on 2016/8/29.
 */
public class ThumbnailContainer extends LinearLayout {

    private ViewDragHelper viewDragHelper;

    private int direction;

    private HorizontalScrollView scrollView;

    public ThumbnailContainer(Context context, int direction) {
        super(context);
        this.direction = direction;


        // 初始化ViewDragHelper
        viewDragHelper = ViewDragHelper.create(this, 1.2f, new ViewDragHelperCallBack());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    private float preX, preY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = event.getX();
                preY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                scrollView.scrollTo((int) (curX - preX), 0);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child instanceof FrameLayout;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            System.out.println(top);
            return top;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        scrollView = (HorizontalScrollView) getParent();
    }
}
