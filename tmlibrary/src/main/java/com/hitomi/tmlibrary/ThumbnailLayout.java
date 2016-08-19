package com.hitomi.tmlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 *
 * 用于放置缩略图
 *
 * Created by hitomi on 2016/8/19.
 */
public class ThumbnailLayout extends RelativeLayout{

    public ThumbnailLayout(Context context) {
        this(context, null);
    }

    public ThumbnailLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbnailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}
