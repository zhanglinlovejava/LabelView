package com.zhanglin.labelview.util;

import android.graphics.Point;

public class MyPointF extends android.graphics.PointF implements Cloneable {
    public float z;

    public MyPointF(float x, float y) {
        this(x, y, 0f);
    }

    public MyPointF(float x, float y, float z) {
        super(x, y);
        this.z = z;
    }

    public MyPointF moveTo(Phasor v, float len) {
        return moveTo(v, len, false);
    }

    public MyPointF moveTo(Phasor v, float len, boolean isDirectionPhasor) {
        if (isDirectionPhasor) {
            this.x = this.x + v.x * len;
            this.y = this.y + v.y * len;
        } else {
            Phasor v0 = v.getDirectionPhasor();
            this.x = this.x + v0.x * len;
            this.y = this.y + v0.y * len;
        }
        return this;
    }

    public MyPointF moveTo(Phasor v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        return this;
    }


    public Point toPoint() {
        return new Point(Math.round(this.x), Math.round(this.y));
    }

    public MyPointF clone() {
        return new MyPointF(x, y);
    }

    @Override
    public String toString() {
        String str = "(%f,%f,%f)";
        return String.format(str, x, y, z);
    }

}