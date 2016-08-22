package com.hitomi.tmlibrary;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.hitomi.tmlibrary.util.DensityUtil;

/**
 * Created by hitomi on 2016/8/19.
 */
class ThumbnailStyleFactory {

    static final int MENU_DIRECTION_LEFT = 1000;

    static final int MENU_DIRECTION_BOTTOM = 1001;

    static final int MENU_DIRECTION_RIGHT = 1002;

    FrameLayout createMenuContainer(Context context, int direction){
        FrameLayout scrollContainer;
        RelativeLayout.LayoutParams rlayoutParams;

        int scrollWidth = (int) (DensityUtil.getScreenWidth(context) * .618f);
        int scrollHeight = (int) (DensityUtil.getScreenHeight(context) * (1.0f - .618f));

        switch (direction) {
            case MENU_DIRECTION_LEFT:
                ScrollView leftScrollLayout = new ScrollView(context);
                leftScrollLayout.setVerticalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(scrollWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                scrollContainer = leftScrollLayout;
                break;
            case MENU_DIRECTION_RIGHT:
                ScrollView rightScrollLayout = new ScrollView(context);
                rightScrollLayout.setVerticalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(scrollWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                scrollContainer = rightScrollLayout;
                break;
            case MENU_DIRECTION_BOTTOM:
                HorizontalScrollView horScrollLayout = new HorizontalScrollView(context);
                horScrollLayout.setHorizontalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, scrollHeight);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                scrollContainer = horScrollLayout;
                break;
            default:
                ScrollView defaultScrollLayout = new ScrollView(context);
                defaultScrollLayout.setVerticalScrollBarEnabled(false);
                rlayoutParams = new RelativeLayout.LayoutParams(scrollWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                rlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                scrollContainer = defaultScrollLayout;
        }
        scrollContainer.setLayoutParams(rlayoutParams);
        return scrollContainer;
    }

}
