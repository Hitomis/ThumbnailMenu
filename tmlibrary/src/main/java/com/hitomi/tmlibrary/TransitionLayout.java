package com.hitomi.tmlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by hitomi on 2016/8/22.
 */
public class TransitionLayout extends FrameLayout implements View.OnClickListener {

    public TransitionLayout(Context context) {
        this(context, null);
    }

    public TransitionLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransitionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View view) {

    }
}
