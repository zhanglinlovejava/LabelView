package com.zhanglin.labelview.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Rhomboid implements Cloneable {
    public MyPointF A;
    public MyPointF B;
    public MyPointF C;
    public MyPointF D;

    private int PADDING = 50;
    //20dp
    private int DELETE_RANGE = 20;

    public Rhomboid(MyPointF A, MyPointF B, MyPointF C, MyPointF D) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

    public Phasor getWidthDirectionPhasor() {
        Phasor p = new Phasor(A, B);
        return p.getDirectionPhasor();
    }

    public Phasor getHeightDirectionPhasor() {
        Phasor p = new Phasor(A, D);
        return p.getDirectionPhasor();
    }

    public boolean isInRhomboid(MyPointF M) {
        Rhomboid rect = getWallRect();
        if (isAcuteAngle(rect.A, rect.B, rect.D, M) && isAcuteAngle(rect.B, rect.A, rect.C, M) && isAcuteAngle(rect.C, rect.B, rect.D, M) && isAcuteAngle(rect.D, rect.A, rect.C, M)) {
            return true;
        }
        return false;
    }

    public boolean isInDeleteIcon(Context context, MyPointF M){
        Rhomboid rect = getWallRect();
        Phasor v = new Phasor(M,rect.B);
        if(v.length() <= ScreenUtil.dip2px(context,DELETE_RANGE)){
            return true;
        }
        else{
            return false;
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        drawRect(this,canvas,paint);
    }

    public void  drawRect(Rhomboid rect, Canvas canvas, Paint paint){
        Path path = new Path();
        path.moveTo(rect.A.x, rect.A.y);
        path.lineTo(rect.B.x, rect.B.y);
        path.lineTo(rect.C.x, rect.C.y);
        path.lineTo(rect.D.x, rect.D.y);
        path.close();
        canvas.drawPath(path, paint);
    }



    public Rhomboid getWallRect(){
        Rhomboid rect = this.clone();
        Phasor Vab = new Phasor(rect.A,rect.B).getDirectionPhasor();
        rect.B.moveTo(Vab,PADDING,true);
        rect.C.moveTo(Vab,PADDING,true);
        Vab.reverse();
        rect.A.moveTo(Vab,PADDING,true);
        rect.D.moveTo(Vab,PADDING,true);
        Phasor Vbc = new Phasor(rect.B,rect.C).getDirectionPhasor();
        rect.C.moveTo(Vbc,PADDING,true);
        rect.D.moveTo(Vbc,PADDING,true);
        Vbc.reverse();
        rect.A.moveTo(Vbc,PADDING,true);
        rect.B.moveTo(Vbc,PADDING,true);
        return rect;
    }


    //P1P2 P1P3 与 P1M 三个向量点积为正数
    private boolean isAcuteAngle(MyPointF P1, MyPointF P2, MyPointF P3, MyPointF M) {
        Phasor P1P2 = new Phasor(P1, P2);
        Phasor P1P3 = new Phasor(P1, P3);
        Phasor P1M = new Phasor(P1, M);
        if (P1P2.point(P1M) > 0 && P1P3.point(P1M) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void recalculateByAC(MyPointF A, MyPointF C, Double angle) {
        //Double angle = getAngle();
        double cost = Math.cos(angle);
        double sint = Math.sin(angle);
        Phasor v0 = new Phasor(A, C);
        //AB方向向量
        Phasor v = new Phasor();
        Phasor v1 = v0.getDirectionPhasor();
        v.x = (float) (v1.x * cost - v1.y * sint);
        v.y = (float) (v1.x * sint + v1.y * cost);
        //A沿着AB方向位移lenght(AB)
        double len = v0.length() * cost;
        B.x = (float) (A.x + v.x * len);
        B.y = (float) (A.y + v.y * len);
        //C沿着CD方向位移lenght(CD)
        v.reverse();
        D.x = (float) (C.x + v.x * len);
        D.y = (float) (C.y + v.y * len);
        this.A = A;
        this.C = C;
    }

    public void recalculateByBD(MyPointF B, MyPointF D, Double angle) {
        //Double angle = getAngle();
        double cost = Math.cos(angle);
        double sint = Math.sin(angle);
        Phasor v0 = new Phasor(B, D);
        //BC方向向量
        Phasor v = new Phasor();
        Phasor v1 = v0.getDirectionPhasor();
        v.x = (float) (v1.x * cost - v1.y * sint);
        v.y = (float) (v1.x * sint + v1.y * cost);
        //B沿着BC方向位移lenght(BC)
        double len = v0.length() * cost;
        this.C.x = (float) (B.x + v.x * len);
        this.C.y = (float) (B.y + v.y * len);
        //D沿着DA方向位移lenght(AD)
        v.reverse();
        this.A.x = (float) (D.x + v.x * len);
        this.A.y = (float) (D.y + v.y * len);
        this.B = B;
        this.D = D;
    }

    //获得AB AC夹角
    private Double getAngle() {
        Phasor AB = new Phasor(A, B);
        Phasor AC = new Phasor(A, C);
        //|AB|*|AC|*cosx
        return AB.angleBy(AC);
    }

    public void move(Phasor v) {
        A.x += v.x;
        A.y += v.y;
        B.x += v.x;
        B.y += v.y;
        C.x += v.x;
        C.y += v.y;
        D.x += v.x;
        D.y += v.y;
    }

    public Rhomboid clone() {
        MyPointF p1 = A.clone();
        MyPointF p2 = B.clone();
        MyPointF p3 = C.clone();
        MyPointF p4 = D.clone();
        return new Rhomboid(p1, p2, p3, p4);
    }

}