package com.hitomi.tmlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 缩略图菜单
 * Created by hitomi on 2016/8/19.
 */
public class ThumbnailMenu extends FrameLayout{

    static final float SCALE_RATIO = .618f;

    static final int THUM_MARGIN = 2;

    private ThumbnailFactory factory;

    private ThumbnailAnimator thumbnailAnimator;

    private ThumbnailMenuChooser thumbnailMenuChooser;

    private FrameLayout thumScrollLayout;

    private RelativeLayout backgroundLayout;

    private PagerAdapter pageAdapter;


    private List objects;

    private List<TransitionLayout> tranLayoutList;

    private int direction = ThumbnailFactory.MENU_DIRECTION_LEFT;

    private boolean init = true;

    /**
     * 菜单是否打开
     */
    private boolean isOpen;

    /**
     * 页面数量
     */
    private int pageCount;

    public ThumbnailMenu(Context context) {
        this(context, null);
    }

    public ThumbnailMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbnailMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbnailMenu);
        direction = typedArray.getInt(R.styleable.ThumbnailMenu_menu_direction, direction);
        typedArray.recycle();

        init();
    }

    private void init() {
        isOpen = false;
        objects = new ArrayList();
        tranLayoutList = new ArrayList<>();

        thumbnailMenuChooser = new ThumbnailMenuChooser();
        factory = new ThumbnailFactory();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (init) {
            backgroundLayout = (RelativeLayout) getChildAt(0);
            thumScrollLayout = factory.createMenuContainer(getContext(), right, bottom, direction);
            backgroundLayout.addView(thumScrollLayout);

            thumbnailAnimator = new ThumbnailAnimator(direction, backgroundLayout, tranLayoutList);
            buildingModels();

            init = false;
        }
    }

    /**
     * 绑定页面适配器
     * @param adapter
     */
    public void setAdapter(PagerAdapter adapter) {
        if (pageAdapter != null) {
            pageAdapter.startUpdate(this);
            for (int i = 0; i < adapter.getCount(); i++) {
                pageAdapter.destroyItem((ViewGroup) getChildAt(i + 1), i, objects.get(i));
            }
            pageAdapter.finishUpdate(this);
        }

        pageAdapter = adapter;
        pageCount = pageAdapter.getCount();

        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        for (int i = 0; i < pageCount; i++) {
            TransitionLayout frameLayout = new TransitionLayout(getContext());
            frameLayout.setId(i + 1);// id 不能为0
            frameLayout.setLayoutParams(layoutParams);
            frameLayout.setOnClickListener(thumbnailMenuChooser);
            addView(frameLayout);
            tranLayoutList.add(0, frameLayout); // 倒序添加
        }

        for (int i = 0; i < pageCount; i++) {
            Object object = pageAdapter.instantiateItem((ViewGroup) getChildAt(i + 1), i);
            objects.add(object);
        }
        pageAdapter.finishUpdate(this);
    }

    /**
     * 获取缩略图菜单容器中的 ScrollView 中唯一 ViewGroup [这里是LinearLayout]
     * @return
     */
    public ThumbnailContainer getThumContainner() {
        return (ThumbnailContainer) thumScrollLayout.getChildAt(0);
    }

    /**
     * 创建 modelCount 数量的缩略图模型添加到缩略图菜单容器中
     */
    private void buildingModels() {
        final ThumbnailContainer containerLayout = getThumContainner();
        containerLayout.removeAllViews();
        for (int i = 0; i < pageCount; i++) {
            ThumbnailContainer.LayoutParams containerLayoutParams = new ThumbnailContainer.LayoutParams(
                    (int) (getWidth() * SCALE_RATIO), (int) (getHeight() * SCALE_RATIO));

            if (i != 0) {
                if (direction == ThumbnailFactory.MENU_DIRECTION_BOTTOM) {
                    containerLayoutParams.leftMargin = THUM_MARGIN;
                } else {
                    containerLayoutParams.topMargin = THUM_MARGIN;
                }
            }

            FrameLayout modelLayout = new FrameLayout(getContext());
            modelLayout.setTag(i);
            containerLayout.addView(modelLayout, containerLayoutParams);
        }
    }

    private class ThumbnailMenuChooser implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (isOpen && view instanceof TransitionLayout) {
                TransitionLayout choosenLayout = (TransitionLayout) view;
                closeMenu(choosenLayout);
            }
        }
    }

    public void openMenu() {
        isOpen = true;
        thumbnailAnimator.openMenuAnimator();
    }

    private void closeMenu(TransitionLayout transitionLayout) {
        isOpen = false;
        thumbnailAnimator.closeMenuAnimator(transitionLayout);
    }
}
