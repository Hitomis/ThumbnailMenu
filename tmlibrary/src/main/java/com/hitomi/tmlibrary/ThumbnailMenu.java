package com.hitomi.tmlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 缩略图菜单
 * Created by hitomi on 2016/8/19.
 */
public class ThumbnailMenu extends FrameLayout{

    private ThumbnailLayout thumbnailLayout;

    private PagerAdapter mAdapter;

    private ThumbnailAnimator thumbnailAnimator;

    private List objects;

    private int direction = ThumbnailStyleFactory.MENU_DIRECTION_LEFT;

    private List<TransitionLayout> tranLayoutList;

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
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        objects = new ArrayList();
        tranLayoutList = new ArrayList<>();

        thumbnailLayout = new ThumbnailLayout(getContext(), attrs);
        addView(thumbnailLayout);
    }

    public void setAdapter(PagerAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.startUpdate(this);
            for (int i = 0; i < adapter.getCount(); i++) {
                mAdapter.destroyItem((ViewGroup) getChildAt(i + 1), i, objects.get(i));
            }
            mAdapter.finishUpdate(this);
        }

        this.mAdapter = adapter;
        int count = mAdapter.getCount();

        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        for (int i = 0; i < count; i++) {
            TransitionLayout frameLayout = new TransitionLayout(getContext());
            frameLayout.setTag(i);
            frameLayout.setId(i + 1);
            frameLayout.setLayoutParams(layoutParams);
            addView(frameLayout);
            tranLayoutList.add(frameLayout);
        }

        for (int i = 0; i < count; i++) {
            Object object = mAdapter.instantiateItem((ViewGroup) getChildAt(i + 1), i);
            objects.add(object);
        }
        mAdapter.finishUpdate(this);

        thumbnailAnimator = new ThumbnailAnimator(direction);
    }

    public void openMenu() {
        thumbnailAnimator.createMenuAnimtor(thumbnailLayout, tranLayoutList);
    }
}
