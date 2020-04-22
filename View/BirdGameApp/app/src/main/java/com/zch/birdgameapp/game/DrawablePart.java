package com.zch.birdgameapp.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class DrawablePart {

    protected Context mContext;
    protected int mWidth;
    protected int mHeight;
    protected Bitmap mBitmap;


    public DrawablePart(Context context, int gameW, int gameH, Bitmap bitmap){
        mContext = context;
        mWidth = gameW;
        mHeight = gameH;
        mBitmap = bitmap;
    }


    public abstract void draw(Canvas canvas);

}
