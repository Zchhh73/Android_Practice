package com.zch.viewapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;

import androidx.annotation.Nullable;

public class RoundProgressBar extends View {

    private int mRadius;
    private int mColor;
    private int mLineWidth;
    private int mTextSize;
    private int mProgress;


    private Paint mPaints;

    private RectF mRectF;

    //用于动态new view
    public RoundProgressBar(Context context) {
        super(context);
    }

    //自定义view能在布局文件中使用，一定要复写
    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //View声明
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        mRadius=(int) ta.getDimension(R.styleable.RoundProgressBar_radius, dp2px(30));
        mColor=ta.getColor(R.styleable.RoundProgressBar_color, 0XFFFF0000);
        mLineWidth=(int) ta.getDimension(R.styleable.RoundProgressBar_line_width, dp2px(3));
        mTextSize=(int) ta.getDimension(R.styleable.RoundProgressBar_android_textSize, dp2px(16));
        mProgress=ta.getInt(R.styleable.RoundProgressBar_android_progress, 30);
        //回收
        ta.recycle();

        //
        initPaint();

    }

    private float dp2px(int i) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, i, getResources().getDisplayMetrics());
    }

    private void initPaint() {
        mPaints=new Paint();
        mPaints.setColor(mColor);
        //抗锯齿
        mPaints.setAntiAlias(true);
    }

    //有一些style的属性，两个参数方法中调用它。
    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);

        int width=0;
        //EXACTLY模式
        if (widthMode == MeasureSpec.EXACTLY) {
            width=widthSize;
        } else {
            int needWidth=measureWidth() + getPaddingLeft() + getPaddingRight();
            //AT_MOST模式 wrap_content
            if (widthMode == MeasureSpec.AT_MOST) {
                width=Math.min(widthSize, needWidth);
                //都不是
            } else {
                width=needWidth;
            }
        }

        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int height=0;

        if (heightMode == MeasureSpec.EXACTLY) {
            height=heightSize;
        } else {
            int needHeight=measureHeight() + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height=Math.min(needHeight, heightSize);
            } else {
                height=needHeight;
            }
        }
        width = Math.min(width,height);
        //为View设置测量后的宽高
        setMeasuredDimension(width, height);
    }

    //测量算法
    private int measureHeight() { return mRadius * 2; }


    private int measureWidth() { return mRadius * 2; }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0, 0, w - getPaddingLeft() * 2, h - getPaddingTop() * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaints.setStyle(Paint.Style.STROKE);
        mPaints.setStrokeWidth(mLineWidth * 1.0f / 4);

        int width=getWidth();
        int height=getHeight();
        //画圆
        canvas.drawCircle(width / 2, height / 2,
                width / 2 - getPaddingLeft() - mPaints.getStrokeWidth() / 2, mPaints);


        //圆弧
        mPaints.setStrokeWidth(mLineWidth);
        canvas.save();
        //移动坐标原点
        canvas.translate(getPaddingLeft(), getPaddingTop());
        //矩形
        float angle=mProgress * 1.0f / 100 * 360;
        canvas.drawArc(mRectF, 0, angle, false, mPaints);
        canvas.restore();

        String text=mProgress + "%";
        mPaints.setStrokeWidth(0);
        mPaints.setTextAlign(Paint.Align.CENTER);
        mPaints.setTextSize(mTextSize);

        int y=getHeight() / 2;
        //字的高度
        Rect bound=new Rect();
        mPaints.getTextBounds(text, 0, text.length(), bound);
        int textHeight=bound.height(); //中文：mPaints.descent()/2
        canvas.drawText(text, 0, text.length(), getWidth() / 2, y + textHeight / 2, mPaints);

    }

    private static final String INSTANCE="instance";
    private static final String KEY_PROGRESS="key_progress";


    //状态恢复
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle=new Bundle();
        //存自己的
        bundle.putInt(KEY_PROGRESS, mProgress);
        //父View存储内容
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle=(Bundle) state;
            Parcelable parcelable=bundle.getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);
            mProgress=bundle.getInt(KEY_PROGRESS);
            return;
        }

        super.onRestoreInstanceState(state);
    }

    public void setProgress(int progress) {
        mProgress=progress;
        invalidate();
    }

    public int getProgress() {
        return mProgress;
    }
}
