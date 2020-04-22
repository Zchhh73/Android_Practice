package com.zch.viewapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TestView extends View {

    private String mText="hello";

    private Paint mPaints;

    //用于动态new view
    public TestView(Context context) {
        super(context);
    }

    //自定义view能在布局文件中使用，一定要复写
    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //
        initPaint();

        //View声明
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.TestView);

        //直接获取
        boolean booleanTest=ta.getBoolean(R.styleable.TestView_test_boolean, false);
        int integerTest=ta.getInteger(R.styleable.TestView_test_integer, 10);
        float dimensionTest=ta.getDimension(R.styleable.TestView_test_dimension, 0);
        int enumTest=ta.getInt(R.styleable.TestView_test_enum, 1);
        //造成定义的值的丢失
//        mText=ta.getString(R.styleable.TestView_test_string);


        //会获取类内设置的值，不会被覆盖
        int count=ta.getIndexCount();
        for (int i=0; i < count; i++) {
            int index = ta.getIndex(i);
            switch (index){
                case R.styleable.TestView_test_string:
                    mText = ta.getString(R.styleable.TestView_test_string);
                    break;
            }
        }


        Log.e("TAG", booleanTest + ","
                + integerTest + ","
                + dimensionTest + ","
                + enumTest + ","
                + mText);


        //回收
        ta.recycle();

    }

    private void initPaint() {
        mPaints = new Paint();
        mPaints.setStyle(Paint.Style.STROKE);
        mPaints.setStrokeWidth(6);
        mPaints.setColor(0xFFFF0000);
        mPaints.setAntiAlias(true);
    }

    //有一些style的属性，两个参数方法中调用它。
    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width=0;
        //EXACTLY模式
        if(widthMode==MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            int needWidth = measureWidth() + getPaddingLeft() + getPaddingRight();
            //AT_MOST模式
            if(widthMode == MeasureSpec.AT_MOST){
                needWidth = Math.min(widthMeasureSpec,needWidth);
            //都不是
            }else{
                width = needWidth;
            }
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;

        if(heightMode==MeasureSpec.EXACTLY){
            height = heightSize;
        }else{
            int needHeight = measureHeight() + getPaddingTop() + getPaddingBottom();
            if(heightMode == MeasureSpec.AT_MOST){
                height = Math.min(needHeight,heightSize);
            }else{
                height = needHeight;
            }
        }
        //为View设置测量后的宽高
        setMeasuredDimension(width,height);
    }

    //测量算法
    private int measureHeight() {
        return 0;
    }


    private int measureWidth() {
        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2-mPaints.getStrokeWidth()/2,mPaints);
//
//        mPaints.setStrokeWidth(1);
//        canvas.drawLine(0,getHeight()/2,getWidth(),getHeight()/2,mPaints);
//        canvas.drawLine(getWidth()/2,0,getWidth()/2,getHeight(),mPaints);

        mPaints.setTextSize(80);
        mPaints.setStyle(Paint.Style.FILL);
        mPaints.setStrokeWidth(0);
        canvas.drawText(mText,0,mText.length(),0,getHeight(),mPaints);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mText = "8888";
        // View（非容器类）调用invalidate方法只会重绘自身，
        // ViewGroup调用则会重绘整个View树
        invalidate();
        return true;
    }


    private static final String INSTANCE = "instance";
    private static final String KEY_TEXT = "key_text";


    //状态恢复
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        //存自己的
        bundle.putString(KEY_TEXT,mText);
        //父View存储内容
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            Parcelable parcelable=bundle.getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);
            mText = bundle.getString(KEY_TEXT);
            return;
        }

        super.onRestoreInstanceState(state);
    }
}
