package com.zhanglin.labelview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhanglin on 2018/4/20.
 */

public class LabelImageView extends ImageView {
    private LabelViewHelper helper;
    public LabelImageView(Context context) {
        this(context,null);
    }

    public LabelImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        helper.onMeasure(getMeasuredWidth(),getMeasuredHeight());
    }
    public void showOrHideLabel(boolean isShowLabel){
        helper.showOrHideLabel(isShowLabel);
        invalidate();
    }
}
