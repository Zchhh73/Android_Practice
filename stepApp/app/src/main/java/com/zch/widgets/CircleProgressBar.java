package com.zch.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleProgressBar extends View {

    private int progress = 0;
    private int maxProgress = 100;

    //绘图Paint
    private Paint pathPaint;
    private Paint fillPaint;
    //绘制的矩形区域
    private RectF oval;
    private int[] arcColors={0xFF02C016,0xFF3DF346,0xFF40F1D5,0xFF02C016};
    //背景灰色
    private int pathColor = 0xFFF0EEDF;
    //边框灰色
    private int borderColor = 0xFFD2D1C4;
    private int pathWidth=35;
    private int width;
    private int height;
    //圆半径
    private int radius = 120;
    //梯度渲染
    private SweepGradient mSweepGradient;
    //重置
    private boolean reset = false;

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //初始化绘制
        pathPaint = new Paint();
        //反锯齿
        pathPaint.setAntiAlias(true);
        pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setDither(true);
        //线条
        pathPaint.setStrokeJoin(Paint.Join.ROUND);

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setDither(true);
        fillPaint.setStrokeJoin(Paint.Join.ROUND);
        oval = new RectF();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress=progress;
        invalidate();
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress=maxProgress;
        invalidate();
    }

    public int getPathColor() {
        return pathColor;
    }

    public void setPathColor(int pathColor) {
        this.pathColor=pathColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor=borderColor;
    }

    public int getPathWidth() {
        return pathWidth;
    }

    public void setPathWidth(int pathWidth) {
        this.pathWidth=pathWidth;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius=radius;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset=reset;
        if(reset){
            progress=0;
            //UI区域无效，刷新方法
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(reset){
            canvas.drawColor(0xFFFFFFFF);
            reset = false;
        }
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        radius = getMeasuredWidth()/2- pathWidth;
        //设置背景颜色
        pathPaint.setColor(pathColor);
        //设置画笔宽度
        pathPaint.setStrokeWidth(pathWidth);
        //绘制背景
        canvas.drawCircle(width/2,height/2,radius,pathPaint);
        pathPaint.setStrokeWidth(0.5f);
        pathPaint.setColor(borderColor);
        canvas.drawCircle(width/2,height/2,(float)(radius+pathWidth/2)+0.5f,pathPaint);
        canvas.drawCircle(width/2,height/2,(float)(radius-pathWidth/2)-0.5f,pathPaint);
        mSweepGradient = new SweepGradient((float)(width/2),(float)(height/2),arcColors,null);
        fillPaint.setShader(mSweepGradient);
        //设置线帽
        fillPaint.setStrokeCap(Paint.Cap.ROUND);
        fillPaint.setStrokeWidth(pathWidth);
        oval.set(width/2-radius,height/2-radius,width/2+radius,height/2+radius);
        canvas.drawArc(oval,-90.0F,(float)progress/(float)maxProgress*360.0f,false,fillPaint);
    }
}
