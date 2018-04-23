package com.zhanglin.labelview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by zhanglin on 2018/4/20.
 */

public class LabelButtonView extends Button {
    private LabelViewHelper helper;
    public LabelButtonView(Context context) {
        this(context,null);
    }

    public LabelButtonView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = new LabelViewHelper(context,attrs,defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        helper.onDraw(canvas);
    }


        @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        helper.onMeasure(getMeasuredWidth(),getMeasuredHeight());
    }
}
