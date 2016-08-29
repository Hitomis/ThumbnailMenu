package com.hitomi.tmlibrary;

import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;
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
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Resources r = Resources.getSystem();
        switch (direction) {
            case ThumbnailFactory.MENU_DIRECTION_LEFT:
            case ThumbnailFactory.MENU_DIRECTION_RIGHT:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureLayoutHeight(), MeasureSpec.EXACTLY);
                break;

            case ThumbnailFactory.MENU_DIRECTION_BOTTOM:
//                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureLayoutHeight() {
        int measureHeight = 0;

        int childCount = getChildCount();
        TransitionLayout tranLayout;
        for (int i = 0; i < childCount; i++) {
            tranLayout = (TransitionLayout) getChildAt(i);
            measureHeight += tranLayout.getHeight() * tranLayout.getScaleY();
        }

        return measureHeight;
    }
}
