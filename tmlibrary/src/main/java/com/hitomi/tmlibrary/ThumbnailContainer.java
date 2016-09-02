package com.hitomi.tmlibrary;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * 用于放置缩略图的容器
 * Created by hitomi on 2016/8/29.
 */
public class ThumbnailContainer extends LinearLayout {

    private int direction;

    public ThumbnailContainer(Context context, int direction) {
        super(context);
        this.direction = direction;
    }

}
