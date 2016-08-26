package com.hitomi.tmlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 *
 * 用于放置缩略图
 *
 * Created by hitomi on 2016/8/19.
 */
class ThumbnailLayout extends RelativeLayout{

    private int menuDirection;

    private FrameLayout thumbnailContainner;

    public ThumbnailLayout(Context context) {
        this(context, null);
    }

    public ThumbnailLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        setBackgroundColor(Color.parseColor("#00d2db"));
    }

    public ThumbnailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbnailMenu);
        menuDirection = typedArray.getInt(R.styleable.ThumbnailMenu_menu_direction, ThumbnailStyleFactory.MENU_DIRECTION_LEFT);
        typedArray.recycle();

        initLayout();
    }

    private void initLayout() {
        ThumbnailStyleFactory factory = new ThumbnailStyleFactory();
        thumbnailContainner = factory.createMenuContainer(getContext(), menuDirection);
        addView(thumbnailContainner);
    }

    public ViewGroup getContainner() {
        return (ViewGroup) thumbnailContainner.getChildAt(0);
    }
}
