package com.zch.birdgameapp.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

public class Floor extends DrawablePart {

    //坐标
    private int x;
    private int y;

    private static final float RADIO_Y_POS=4 / 5f;

    private Paint mPaint;
    private BitmapShader mBitmapShader;


    public Floor(Context context, int gameW, int gameH, Bitmap bitmap) {
        super(context, gameW, gameH, bitmap);

        y=(int) (gameH * RADIO_Y_POS);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        //横向repeat，纵向拉伸
        mBitmapShader=new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);


    }

    @Override
    public void draw(Canvas canvas) {

        canvas.save();

        canvas.translate(x, y);
        mPaint.setShader(mBitmapShader);
        canvas.drawRect(x, 0, -x + mWidth, mHeight-y,mPaint);
        canvas.restore();
        mPaint.setShader(null);
    }

    public int getX(){
        return x;
    }

    public void setX(int x){

        this.x = x;
        if(-x > mWidth){
            this.x = x % mWidth;
        }
    }
}
