package com.hitomi.tmlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 *
 * 用于放置缩略图
 *
 * Created by hitomi on 2016/8/19.
 */
class ThumbnailLayout extends RelativeLayout{

    private int menuDirction = ThumbnailStyleFactory.MENU_DIRECTION_LEFT;

    public ThumbnailLayout(Context context) {
        this(context, null);
    }

    public ThumbnailLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbnailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initLayout();
    }

    private void initLayout() {
        ThumbnailStyleFactory factory = new ThumbnailStyleFactory();
        FrameLayout scrollLayout = factory.createMenuContainer(getContext(), menuDirction);
        addView(scrollLayout);
    }


}
