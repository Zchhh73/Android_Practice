package com.zch.birdgameapp.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Bird extends DrawablePart {

    private int x;
    private int y;

    private static final float RADIO_Y_POS=1 / 2f;
    //30dp
    private static final int BIRD_WIDTH=30;

    private int mWidth;
    private int mHeight;

    private RectF mRect=new RectF();


    public Bird(Context context, int gameW, int gameH, Bitmap bitmap) {
        super(context, gameW, gameH, bitmap);

        y=(int) (gameH * RADIO_Y_POS);
        mWidth=Utils.dp2px(context, BIRD_WIDTH);
        //等比例下高度
        mHeight=(int) (mWidth * 1.0f / bitmap.getWidth() * bitmap.getHeight());

        //鸟的位置
        x=gameW / 2 - mWidth / 2;

    }

    @Override
    public void draw(Canvas canvas) {
        mRect.set(x, y, x + mWidth, y + mHeight);
        canvas.drawBitmap(mBitmap, null, mRect, null);
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }

    public void reset() {
        y=(int) (mHeight * RADIO_Y_POS);
    }
}
