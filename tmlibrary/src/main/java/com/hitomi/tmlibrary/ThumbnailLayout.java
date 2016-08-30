package com.hitomi.tmlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 用于包裹缩略图菜单容器的容器
 * <p>
 * Created by hitomi on 2016/8/19.
 */
public class ThumbnailLayout extends RelativeLayout {

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
        menuDirection = typedArray.getInt(R.styleable.ThumbnailMenu_menu_direction, ThumbnailFactory.MENU_DIRECTION_LEFT);
        typedArray.recycle();

        initLayout();
    }

    private void initLayout() {
        ThumbnailFactory factory = new ThumbnailFactory();
        thumbnailContainner = factory.createMenuContainer(getContext(), menuDirection);
        addView(thumbnailContainner);
    }

    public void buildingModels(int modelCount) {
        ThumbnailContainer containerLayout = getContainner();

        for (int i = 0; i < modelCount; i++) {
            ThumbnailContainer.LayoutParams containerLayoutParams = new ThumbnailContainer.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (int) (1230 * ThumbnailMenu.scaleRatio));
            if (i != 0) containerLayoutParams.topMargin = 2;
            FrameLayout modelLayout = new FrameLayout(getContext());
            modelLayout.setTag(i);
            containerLayout.addView(modelLayout, containerLayoutParams);
        }
    }

    public ThumbnailContainer getContainner() {
        return (ThumbnailContainer) thumbnailContainner.getChildAt(0);
    }

    public FrameLayout getContainnerParent() {
        return thumbnailContainner;
    }
}
