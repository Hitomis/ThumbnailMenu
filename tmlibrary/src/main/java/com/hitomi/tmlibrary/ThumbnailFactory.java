package com.hitomi.tmlibrary;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.hitomi.tmlibrary.util.DensityUtil;

/**
 * Created by hitomi on 2016/8/19.
 */
class ThumbnailFactory {

    static final int MENU_DIRECTION_LEFT = 1000;

    static final int MENU_DIRECTION_BOTTOM = 1001;

    static final int MENU_DIRECTION_RIGHT = 1002;

    FrameLayout createMenuContainer(Context context, int direction) {
        FrameLayout scrollLayout;
        RelativeLayout.LayoutParams rlayoutParams;

        int scrollWidth = (int) (DensityUtil.getScreenWidth(context) * ThumbnailMenu.SCALE_RATIO);
        int scrollHeight = (int) (DensityUtil.getScreenHeight(context) * (1.0f - ThumbnailMenu.SCALE_RATIO));

        LinearLayout containerLayout = new ThumbnailContainer(context, direction);
        ScrollView.LayoutParams leftLinlayParams = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        containerLayout.setLayoutParams(leftLinlayParams);

        switch (direction) {
            case MENU_DIRECTION_LEFT:
                scrollLayout = new ScrollView(context);
                scrollLayout.setVerticalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(scrollWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                containerLayout.setOrientation(LinearLayout.VERTICAL);
                break;
            case MENU_DIRECTION_RIGHT:
                scrollLayout = new ScrollView(context);
                scrollLayout.setVerticalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(scrollWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                containerLayout.setOrientation(LinearLayout.VERTICAL);
                break;
            case MENU_DIRECTION_BOTTOM:
                scrollLayout = new HorizontalScrollView(context);
                scrollLayout.setHorizontalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, scrollHeight);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                containerLayout.setOrientation(LinearLayout.HORIZONTAL);
                break;
            default:
                scrollLayout = new ScrollView(context);
                scrollLayout.setVerticalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(scrollWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                containerLayout.setOrientation(LinearLayout.VERTICAL);


        }
        scrollLayout.setLayoutParams(rlayoutParams);
        scrollLayout.addView(containerLayout);
        return scrollLayout;
    }

}
