package com.hitomi.tmlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 缩略图菜单
 * Created by hitomi on 2016/8/19.
 */
public class ThumbnailMenu extends FrameLayout{

    public ThumbnailMenu(Context context) {
        this(context, null);
    }

    public ThumbnailMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbnailMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
