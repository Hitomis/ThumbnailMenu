package com.hitomi.tmlibrary;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by hitomi on 2016/8/19.
 */
class ThumbnailFactory {

    static final int MENU_DIRECTION_LEFT = 1000;

    static final int MENU_DIRECTION_BOTTOM = 1001;

    static final int MENU_DIRECTION_RIGHT = 1002;

    FrameLayout createMenuContainer(Context context, int direction) {
        FrameLayout scrollLayout;

        LinearLayout containerLayout = new ThumbnailContainer(context, direction);
        ScrollView.LayoutParams scrollParams = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        containerLayout.setLayoutParams(scrollParams);

        switch (direction) {
            case MENU_DIRECTION_LEFT:
                scrollLayout = new ScrollView(context);
                scrollLayout.setVerticalScrollBarEnabled(false);
                containerLayout.setOrientation(LinearLayout.VERTICAL);
                break;
            case MENU_DIRECTION_RIGHT:
                scrollLayout = new ScrollView(context);
                scrollLayout.setVerticalScrollBarEnabled(false);
                containerLayout.setOrientation(LinearLayout.VERTICAL);
                break;
            case MENU_DIRECTION_BOTTOM:
                scrollLayout = new HorizontalScrollView(context);
                scrollLayout.setHorizontalScrollBarEnabled(false);
                containerLayout.setOrientation(LinearLayout.HORIZONTAL);
                break;
            default:
                scrollLayout = new ScrollView(context);
                scrollLayout.setVerticalScrollBarEnabled(false);
                containerLayout.setOrientation(LinearLayout.VERTICAL);
        }
        scrollLayout.addView(containerLayout);
        return scrollLayout;
    }

}
