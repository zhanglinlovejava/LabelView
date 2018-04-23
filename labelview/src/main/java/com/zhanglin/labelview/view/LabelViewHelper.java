package com.zhanglin.labelview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zhanglin.labelview.R;
import com.zhanglin.labelview.util.MyPointF;
import com.zhanglin.labelview.util.Phasor;
import com.zhanglin.labelview.util.Rhomboid;

/**
 * Created by zhanglin on 2018/4/20.
 */

public class LabelViewHelper {
    private static final int LEFT_TOP = 1;
    private static final int RIGHT_TOP = 2;
    private static final int LEFT_BOTTOM = 3;
    private static final int RIGHT_BOTTOM = 4;
    private static final int DEFAULT_STROKE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_BACKGROUND_COLOR = 0x9F27CDC0;
    private static final int DEFAULT_ORIENTATION = LEFT_TOP;
    private int viewWidth, viewHeight;
    private Paint bgPaint;
    private Paint textPaint;
    private Paint strokePaint;
    private Rhomboid parentRhomboid;
    private MyPointF pointE, pointF, pointG, pointH, pointI, pointJ;
    private int distance = 80;
    private int labelHeight = 80;
    private Phasor AB;
    private Phasor AD;
    private Phasor DA;
    private Phasor BA;
    private Path labelPath, textPath;

    private String labelText = " ";
    private int labelTextColor;
    private int labelTextSize = 16;
    private int labelBgColor;
    private int strokeColor;
    private Rect textBound;
    private int orientation = 1;
    private int labelMiddleLength = 0;
    private boolean isShowLabel = true;

    public LabelViewHelper(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0);
        labelText = attributes.getString(R.styleable.LabelView_label_text);
        labelTextColor = attributes.getColor(R.styleable.LabelView_label_textColor, DEFAULT_TEXT_COLOR);
        labelTextSize = attributes.getDimensionPixelSize(R.styleable.LabelView_label_textSize, DEFAULT_TEXT_SIZE);
        labelBgColor = attributes.getColor(R.styleable.LabelView_label_bgColor, DEFAULT_BACKGROUND_COLOR);
        strokeColor = attributes.getColor(R.styleable.LabelView_label_storkeColor, DEFAULT_STROKE_COLOR);
        orientation = attributes.getInteger(R.styleable.LabelView_label_orientation, DEFAULT_ORIENTATION);
        distance = attributes.getDimensionPixelOffset(R.styleable.LabelView_label_distance, distance);
        labelHeight = attributes.getDimensionPixelOffset(R.styleable.LabelView_label_height, labelHeight);
        attributes.recycle();
        init();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(labelBgColor);

        strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(2);

        textPaint = new Paint();
        textPaint.setColor(labelTextColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(labelTextSize);

        textBound = new Rect();
        textPaint.getTextBounds(labelText, 0, labelText.length(), textBound);

        labelPath = new Path();
        textPath = new Path();
    }

    private void calculateData() {
        switch (orientation) {
            case LEFT_TOP:
                pointE = parentRhomboid.A.clone().moveTo(AB, distance);
                pointF = pointE.clone().moveTo(AB, labelHeight);
                pointH = parentRhomboid.A.clone().moveTo(AD, distance);
                pointG = pointH.clone().moveTo(AD, labelHeight);
                pointI = pointE.clone().moveTo(AB, labelHeight / 2);
                pointJ = pointH.clone().moveTo(AD, labelHeight / 2);
                break;
            case RIGHT_TOP:
                pointE = parentRhomboid.A.clone().moveTo(AD, viewWidth - distance);
                pointF = pointE.clone().moveTo(DA, labelHeight);
                pointH = parentRhomboid.D.clone().moveTo(AB, distance);
                pointG = pointH.clone().moveTo(AB, labelHeight);
                pointI = pointE.clone().moveTo(DA, labelHeight / 2);
                pointJ = pointH.clone().moveTo(AB, labelHeight / 2);
                break;
            case LEFT_BOTTOM:
                pointE = parentRhomboid.B.clone().moveTo(BA, distance + labelHeight);
                pointF = pointE.clone().moveTo(AB, labelHeight);
                pointG = parentRhomboid.B.clone().moveTo(AD, distance);
                pointH = pointG.clone().moveTo(AD, labelHeight);
                pointI = pointE.clone().moveTo(AB, labelHeight / 2);
                pointJ = pointG.clone().moveTo(AD, labelHeight / 2);
                break;
            case RIGHT_BOTTOM:
                pointE = parentRhomboid.C.clone().moveTo(DA, distance + labelHeight);
                pointF = parentRhomboid.C.clone().moveTo(DA, distance);
                pointG = parentRhomboid.C.clone().moveTo(BA, distance);
                pointH = pointG.clone().moveTo(BA, labelHeight);
                pointI = pointF.clone().moveTo(DA, labelHeight / 2);
                pointJ = pointG.clone().moveTo(BA, labelHeight / 2);
                break;
        }
        Phasor EH = new Phasor(pointE, pointH);
        labelMiddleLength = (int) new Phasor(pointI, pointJ).point(EH.getDirectionPhasor());
        labelPath.moveTo(pointE.x, pointE.y);
        labelPath.lineTo(pointF.x, pointF.y);
        labelPath.lineTo(pointG.x, pointG.y);
        labelPath.lineTo(pointH.x, pointH.y);
        labelPath.close();
        textPath.moveTo(pointI.x, pointI.y);
        textPath.lineTo(pointJ.x, pointJ.y);
        textPath.close();
    }

    private void initLocation() {
        MyPointF pointA = new MyPointF(0, 0);
        MyPointF pointB = new MyPointF(0, viewHeight);
        MyPointF pointC = new MyPointF(viewWidth, viewHeight);
        MyPointF pointD = new MyPointF(viewWidth, 0);
        parentRhomboid = new Rhomboid(pointA, pointB, pointC, pointD);
        AB = new Phasor(parentRhomboid.A, parentRhomboid.B);
        BA = new Phasor(parentRhomboid.B, parentRhomboid.A);
        AD = new Phasor(parentRhomboid.A, parentRhomboid.D);
        DA = new Phasor(parentRhomboid.D, parentRhomboid.A);
        calculateData();
    }

    public void onDraw(Canvas canvas) {
        if (isShowLabel) {
            canvas.drawPath(labelPath, bgPaint);
            canvas.drawPath(labelPath, strokePaint);
            canvas.drawTextOnPath(labelText, textPath, (labelMiddleLength - textBound.width()) / 2, textBound.height() / 2, textPaint);
        }
    }

    public void onMeasure(int viewWidth, int viewHeight) {
        if (parentRhomboid == null) {
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;
            initLocation();
        }
    }

    public void showOrHideLabel(boolean isShowLabel) {
        this.isShowLabel = isShowLabel;
    }
}
