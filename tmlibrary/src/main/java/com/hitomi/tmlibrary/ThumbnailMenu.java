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

    private PagerAdapter mAdapter;

    private List objects;

    private int direction = ThumbnailStyleFactory.MENU_DIRECTION_LEFT;

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
        ThumbnailLayout thumbnailLayout = new ThumbnailLayout(getContext(), attrs);
        addView(thumbnailLayout);

        if (!isInEditMode()) {
            setWillNotDraw(true);
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.startUpdate(this);
            for (int i = 0; i < adapter.getCount(); i++) {
                mAdapter.destroyItem((ViewGroup) getChildAt(i), i, objects.get(i));
            }
            mAdapter.finishUpdate(this);
        }

        this.mAdapter = adapter;
        int count = mAdapter.getCount();

        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        for (int i = 0; i < count; i++) {
            TransitionLayout frameLayout = new TransitionLayout(getContext());
            frameLayout.setTag(i);
            frameLayout.setId(i);
            frameLayout.setLayoutParams(layoutParams);
            addView(frameLayout);
        }

        for (int i = 0; i < count; i++) {
            Object object = mAdapter.instantiateItem((ViewGroup) getChildAt(i), i);
            objects.add(object);
        }
        mAdapter.finishUpdate(this);
    }
}
