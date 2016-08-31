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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 创建 modelCount 数量的缩略图模型添加到缩略图菜单容器中
     * @param modelCount
     */
    public void buildingModels(final int modelCount) {
        final ThumbnailContainer containerLayout = getContainner();
        containerLayout.removeAllViews();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < modelCount; i++) {
                    ThumbnailContainer.LayoutParams containerLayoutParams = new ThumbnailContainer.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, (int) (getHeight() * ThumbnailMenu.SCALE_RATIO));
                    if (i != 0) containerLayoutParams.topMargin = ThumbnailMenu.THUM_TOP_MARGIN;
                    FrameLayout modelLayout = new FrameLayout(getContext());
                    modelLayout.setTag(i);
                    containerLayout.addView(modelLayout, containerLayoutParams);
                }
            }
        }, 20);
    }

    /**
     * 获取缩略图菜单容器中的 ScrollView 中唯一 ViewGroup [这里是LinearLayout]
     * @return
     */
    public ThumbnailContainer getContainner() {
        return (ThumbnailContainer) thumbnailContainner.getChildAt(0);
    }

    /**
     * 获取缩略图菜单容器中的ScrollView
     * @return
     */
    public FrameLayout getContainnerParent() {
        return thumbnailContainner;
    }
}
