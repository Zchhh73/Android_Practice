package com.uestc.bookreader.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import java.io.IOException;

/**
 * 贝塞尔曲线翻页效果
 */
public class BookPageView extends RelativeLayout {

    private int mViewWidth, mViewHeight;

    /**
     * mCornerX  拖拽点对应的页脚X坐标
     * mCornerY  拖拽点对应的页脚坐标
     */
    private int mCornerX, mCornerY;

    //拖拽点和拖拽点角的连线的垂直平分线交点
    private float mMiddleX, mMiddleY;
    //当前页和下一页画布
    private Bitmap mCurPageBitmap, mNextPageBitmap;

    /**
     * mCurrentPagePath  用于绘制当前页的曲线
     * mNextPagePath  用于绘制下一页的曲线
     */
    private Path mCurrentPagePath, mNextPagePath;

    private GradientDrawable mBackShadowDrawableLR;
    private GradientDrawable mBackShadowDrawableRL;
    private GradientDrawable mFolderShadowDrawableLR;
    private GradientDrawable mFolderShadowDrawableRL;

    private GradientDrawable mFrontShadowDrawableHBT;
    private GradientDrawable mFrontShadowDrawableHTB;
    private GradientDrawable mFrontShadowDrawableVLR;
    private GradientDrawable mFrontShadowDrawableVRL;

    private int[] mBackShadowColors;
    private int[] mFrontShadowColors;
    private int[] mFolderShadowColors;

    private PointF mTouchPoint;//拖拽点
    private Paint mPaint;
    private ColorMatrixColorFilter mColorMatrixFilter;
    private Matrix mMatrix;
    private Scroller mScroller;
    private float mMaxLength;
    private float mTouchToCornerDis;
    private float mDegrees;
    private float[] mMatrixArray = {0, 0, 0, 0, 0, 0, 0, 0, 1.0f};
    private boolean mIsRTAndLB; // 是否属于右上左下

    public boolean mIsTimeRefresh;// 判断是否是时间需要刷新
    public boolean mIsShowNoMore;// 到第一页和最后一页判断
    public boolean mIsUpData;

    private PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
    private PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
    private PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
    private PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点

    private PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线起始点
    private PointF mBezierControl2 = new PointF();//另一条贝塞尔曲线控制点
    private PointF mBeziervertex2 = new PointF();// 另一条贝塞尔曲线顶点
    private PointF mBezierEnd2 = new PointF();// 另一条贝塞尔曲线结束点

    BookPageBezierHelper mBookPageBezierHelper;
    private Context mContext;
    private Canvas mCurPageCanvas;
    private Canvas mNextPageCanvas;

    private static final String TAG = "BookPageView";

    /****************************************
     * 以下为方法
     *******************************************/
    public BookPageView(Context context) {
        super(context);
        init(context);
    }

    public BookPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BookPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        mCurPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ;
        mNextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ;

        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);


        mTouchPoint = new PointF();
        mCurrentPagePath = new Path();
        mNextPagePath = new Path();
        createDrawable();

        //设置画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        ColorMatrix cm = new ColorMatrix();
        float array[] = {0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
                0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0};
        cm.set(array);
        mColorMatrixFilter = new ColorMatrixColorFilter(cm);
        mMatrix = new Matrix();
        mScroller = new Scroller(getContext());

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                boolean isCenterOfX = event.getX() < mViewWidth / 2 + 100
                        && event.getX() > mViewWidth / 2 - 100;
                boolean isCenterOfY = event.getY() < mViewHeight / 2 + 150
                        && event.getY() > mViewHeight / 2 - 150;

                if(isCenterOfX && isCenterOfY){
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        Log.i(TAG, "onTouch: Center");
                        if(mOnUserNeedSettingListener != null){
                            mOnUserNeedSettingListener.onUserNeedSetting();
                        }
                    }
                    return true;
                }


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        abortAnimation();
                        mIsShowNoMore = false;
                        calcCornerXY(event.getX(), event.getY());
                        mBookPageBezierHelper.draw(mCurPageCanvas);
                        if (DragToRight()) {
                            try {
                                mBookPageBezierHelper.prePage();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (mBookPageBezierHelper.isFirstPage()) {
                                mIsShowNoMore = true;
                                invalidate();
                                return true;
                            }
                            mBookPageBezierHelper.draw(mNextPageCanvas);
                        } else {
                            try {
                                mBookPageBezierHelper.nextPage();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (mBookPageBezierHelper.isLastPage()) {
                                mIsShowNoMore = true;
                                invalidate();
                                return true;
                            }
                            mBookPageBezierHelper.draw(mNextPageCanvas);
                        }
                        setBitmaps(mCurPageBitmap, mNextPageBitmap);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mBookPageBezierHelper.isLastPage()) {
                            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                        } else if (mBookPageBezierHelper.isFirstPage()) {
                            Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 创建阴影的GradientDrawable
     */
    private void createDrawable() {
        mFolderShadowColors = new int[]{0xFFF5EE, 0xFFF5EE};
        mFolderShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFolderShadowColors);
        mFolderShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFolderShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFolderShadowColors);
        mFolderShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowColors = new int[]{0xff111111, 0x111111};
        mBackShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
        mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowColors = new int[]{0x80111111, 0x111111};
        mFrontShadowDrawableVLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
        mFrontShadowDrawableVLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableVRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
        mFrontShadowDrawableVRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHTB = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
        mFrontShadowDrawableHTB.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHBT = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
        mFrontShadowDrawableHBT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }


    /**
     * 计算拖拽点对应的拖拽脚
     */
    public void calcCornerXY(float x, float y) {
        mCornerX = x <= mViewWidth / 2 ? 0 : mViewWidth;
        mCornerY = y <= mViewHeight / 2 ? 0 : mViewHeight;
        mIsRTAndLB = (mCornerX == 0 && mCornerY == mViewHeight) || (mCornerX == mViewWidth && mCornerY == 0);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        mViewWidth = width;
        mViewHeight = height;
        mMaxLength = (float) Math.hypot(mViewWidth, mViewHeight);
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //手指按下
            case MotionEvent.ACTION_DOWN:
                mTouchPoint.x = event.getX();
                mTouchPoint.y = event.getY();
                break;
            //手指移动
            case MotionEvent.ACTION_MOVE:
                mTouchPoint.x = event.getX();
                mTouchPoint.y = event.getY();
                this.postInvalidate();
                break;
            //手指抬起
            case MotionEvent.ACTION_UP:
                if (canDragOver()) {
                    startAnimation(1200);
                } else {
                    mTouchPoint.x = mCornerX - 0.09f;
                    mTouchPoint.y = mCornerY - 0.09f;
                }
                this.postInvalidate();
                break;
        }
        return true;
    }


    /**
     * 求解直线P1P2和直线P3P4的交点坐标
     */
    public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
        PointF CrossP = new PointF();
        // 二元函数通式： y=ax+b
        float a1 = (P2.y - P1.y) / (P2.x - P1.x);
        float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

        float a2 = (P4.y - P3.y) / (P4.x - P3.x);
        float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
        CrossP.x = (b2 - b1) / (a1 - a2);
        CrossP.y = a1 * CrossP.x + b1;
        return CrossP;
    }


    private void calcPoints() {
        mMiddleX = (mTouchPoint.x + mCornerX) / 2;
        mMiddleY = (mTouchPoint.y + mCornerY) / 2;
        mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
                * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
        mBezierControl1.y = mCornerY;
        mBezierControl2.x = mCornerX;
        mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);


        mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
        mBezierStart1.y = mCornerY;

        // 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
        // 如果继续翻页，会出现BUG故在此限制
        if (mTouchPoint.x > 0 && mTouchPoint.x < mViewWidth) {
            if (mBezierStart1.x < 0 || mBezierStart1.x > mViewWidth) {
                if (mBezierStart1.x < 0)
                    mBezierStart1.x = mViewWidth - mBezierStart1.x;

                float f1 = Math.abs(mCornerX - mTouchPoint.x);
                float f2 = mViewWidth * f1 / mBezierStart1.x;
                mTouchPoint.x = Math.abs(mCornerX - f2);

                float f3 = Math.abs(mCornerX - mTouchPoint.x)
                        * Math.abs(mCornerY - mTouchPoint.y) / f1;
                mTouchPoint.y = Math.abs(mCornerY - f3);

                mMiddleX = (mTouchPoint.x + mCornerX) / 2;
                mMiddleY = (mTouchPoint.y + mCornerY) / 2;

                mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
                        * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
                mBezierControl1.y = mCornerY;

                mBezierControl2.x = mCornerX;
                mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                        * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
                mBezierStart1.x = mBezierControl1.x
                        - (mCornerX - mBezierControl1.x) / 2;
            }
        }
        mBezierStart2.x = mCornerX;
        mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
                / 2;

        mTouchToCornerDis = (float) Math.hypot((mTouchPoint.x - mCornerX),
                (mTouchPoint.y - mCornerY));

        mBezierEnd1 = getCross(mTouchPoint, mBezierControl1, mBezierStart1,
                mBezierStart2);
        mBezierEnd2 = getCross(mTouchPoint, mBezierControl2, mBezierStart1,
                mBezierStart2);
        /*
         * mBeziervertex1.x 推导
		 * ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于
		 * (mBezierStart1.x+ 2*mBezierControl1.x+mBezierEnd1.x) / 4
		 */
        mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
        mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
        mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
        mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
    }

    /**
     * 绘制翻起页的折角曲线
     */
    private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
        mCurrentPagePath.reset();
        mCurrentPagePath.moveTo(mBezierStart1.x, mBezierStart1.y);
        mCurrentPagePath.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
                mBezierEnd1.y);
        mCurrentPagePath.lineTo(mTouchPoint.x, mTouchPoint.y);
        mCurrentPagePath.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        mCurrentPagePath.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
                mBezierStart2.y);
        mCurrentPagePath.lineTo(mCornerX, mCornerY);
        mCurrentPagePath.close();

        canvas.save();
        canvas.clipPath(path, Region.Op.XOR);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();
    }


    /**
     * 绘制翻起页的下一页的阴影
     */
    private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
        mNextPagePath.reset();
        mNextPagePath.moveTo(mBezierStart1.x, mBezierStart1.y);
        mNextPagePath.lineTo(mBeziervertex1.x, mBeziervertex1.y);
        mNextPagePath.lineTo(mBeziervertex2.x, mBeziervertex2.y);
        mNextPagePath.lineTo(mBezierStart2.x, mBezierStart2.y);
        mNextPagePath.lineTo(mCornerX, mCornerY);
        mNextPagePath.close();

        mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x
                - mCornerX, mBezierControl2.y - mCornerY));
        int leftx;
        int rightx;
        GradientDrawable mBackShadowDrawable;
        if (mIsRTAndLB) {
            leftx = (int) (mBezierStart1.x);
            rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
            mBackShadowDrawable = mBackShadowDrawableLR;
        } else {
            leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
            rightx = (int) mBezierStart1.x;
            mBackShadowDrawable = mBackShadowDrawableRL;
        }
        canvas.save();
        canvas.clipPath(mCurrentPagePath);
        canvas.clipPath(mNextPagePath, Region.Op.INTERSECT);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
        mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
                (int) (mMaxLength + mBezierStart1.y));
        mBackShadowDrawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 设置画布
     *
     * @param bm1 当前页画布
     * @param bm2 下一页画布
     */
    public void setBitmaps(Bitmap bm1, Bitmap bm2) {
        mCurPageBitmap = bm1;
        mNextPageBitmap = bm2;

        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
    }


    @SuppressLint("WrongCall")
    @Override
    protected void dispatchDraw(Canvas canvas) {
        onDraw(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsTimeRefresh) {
            canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
            mIsTimeRefresh = false;
            return;
        }

        if (!mIsShowNoMore && !mIsUpData) {
            calcPoints();
            drawCurrentPageArea(canvas, mCurPageBitmap, mCurrentPagePath);
            drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
            drawCurrentPageShadow(canvas);
            drawCurrentBackArea(canvas, mCurPageBitmap);

        } else {
            canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
            mIsUpData = false;

        }
    }


    /**
     * 绘制翻起页的阴影
     */
    public void drawCurrentPageShadow(Canvas canvas) {
        double degree;
        if (mIsRTAndLB) {
            degree = Math.PI
                    / 4
                    - Math.atan2(mBezierControl1.y - mTouchPoint.y, mTouchPoint.x
                    - mBezierControl1.x);
        } else {
            degree = Math.PI
                    / 4
                    - Math.atan2(mTouchPoint.y - mBezierControl1.y, mTouchPoint.x
                    - mBezierControl1.x);
        }
        // 翻起页阴影顶点与touch点的距离
        double d1 = (float) 25 * 1.414 * Math.cos(degree);
        double d2 = (float) 25 * 1.414 * Math.sin(degree);
        float x = (float) (mTouchPoint.x + d1);
        float y;
        if (mIsRTAndLB) {
            y = (float) (mTouchPoint.y + d2);
        } else {
            y = (float) (mTouchPoint.y - d2);
        }
        mNextPagePath.reset();
        mNextPagePath.moveTo(x, y);
        mNextPagePath.lineTo(mTouchPoint.x, mTouchPoint.y);
        mNextPagePath.lineTo(mBezierControl1.x, mBezierControl1.y);
        mNextPagePath.lineTo(mBezierStart1.x, mBezierStart1.y);
        mNextPagePath.close();
        float rotateDegrees;
        canvas.save();

        canvas.clipPath(mCurrentPagePath, Region.Op.XOR);
        canvas.clipPath(mNextPagePath, Region.Op.INTERSECT);
        int leftX;
        int rightX;
        GradientDrawable mCurrentPageShadow;
        if (mIsRTAndLB) {
            leftX = (int) (mBezierControl1.x);
            rightX = (int) mBezierControl1.x + 25;
            mCurrentPageShadow = mFrontShadowDrawableVLR;
        } else {
            leftX = (int) (mBezierControl1.x - 25);
            rightX = (int) mBezierControl1.x + 1;
            mCurrentPageShadow = mFrontShadowDrawableVRL;
        }

        rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouchPoint.x
                - mBezierControl1.x, mBezierControl1.y - mTouchPoint.y));
        canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
        mCurrentPageShadow.setBounds(leftX,
                (int) (mBezierControl1.y - mMaxLength), rightX,
                (int) (mBezierControl1.y));
        mCurrentPageShadow.draw(canvas);
        canvas.restore();

        mNextPagePath.reset();
        mNextPagePath.moveTo(x, y);
        mNextPagePath.lineTo(mTouchPoint.x, mTouchPoint.y);
        mNextPagePath.lineTo(mBezierControl2.x, mBezierControl2.y);
        mNextPagePath.lineTo(mBezierStart2.x, mBezierStart2.y);
        mNextPagePath.close();
        canvas.save();
        canvas.clipPath(mCurrentPagePath, Region.Op.XOR);
        canvas.clipPath(mNextPagePath, Region.Op.INTERSECT);
        if (mIsRTAndLB) {
            leftX = (int) (mBezierControl2.y);
            rightX = (int) (mBezierControl2.y + 25);
            mCurrentPageShadow = mFrontShadowDrawableHTB;
        } else {
            leftX = (int) (mBezierControl2.y - 25);
            rightX = (int) (mBezierControl2.y + 1);
            mCurrentPageShadow = mFrontShadowDrawableHBT;
        }
        rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y
                - mTouchPoint.y, mBezierControl2.x - mTouchPoint.x));
        canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
        float temp;
        if (mBezierControl2.y < 0)
            temp = mBezierControl2.y - mViewHeight;
        else
            temp = mBezierControl2.y;

        int hmg = (int) Math.hypot(mBezierControl2.x, temp);
        if (hmg > mMaxLength)
            mCurrentPageShadow
                    .setBounds((int) (mBezierControl2.x - 25) - hmg, leftX,
                            (int) (mBezierControl2.x + mMaxLength) - hmg,
                            rightX);
        else
            mCurrentPageShadow.setBounds(
                    (int) (mBezierControl2.x - mMaxLength), leftX,
                    (int) (mBezierControl2.x), rightX);
        mCurrentPageShadow.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制翻起页背面
     */
    private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
        int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
        float f1 = Math.abs(i - mBezierControl1.x);
        int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
        float f2 = Math.abs(i1 - mBezierControl2.y);
        float f3 = Math.min(f1, f2);
        mNextPagePath.reset();
        mNextPagePath.moveTo(mBeziervertex2.x, mBeziervertex2.y);
        mNextPagePath.lineTo(mBeziervertex1.x, mBeziervertex1.y);
        mNextPagePath.lineTo(mBezierEnd1.x, mBezierEnd1.y);
        mNextPagePath.lineTo(mTouchPoint.x, mTouchPoint.y);
        mNextPagePath.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        mNextPagePath.close();
        GradientDrawable mFolderShadowDrawable;
        int left;
        int right;
        if (mIsRTAndLB) {
            left = (int) (mBezierStart1.x - 1);
            right = (int) (mBezierStart1.x + f3 + 1);
            mFolderShadowDrawable = mFolderShadowDrawableLR;
        } else {
            left = (int) (mBezierStart1.x - f3 - 1);
            right = (int) (mBezierStart1.x + 1);
            mFolderShadowDrawable = mFolderShadowDrawableRL;
        }
        canvas.save();
        canvas.clipPath(mCurrentPagePath);
        canvas.clipPath(mNextPagePath, Region.Op.INTERSECT);

        mPaint.setColorFilter(mColorMatrixFilter);

        float dis = (float) Math.hypot(mCornerX - mBezierControl1.x,
                mBezierControl2.y - mCornerY);
        float f8 = (mCornerX - mBezierControl1.x) / dis;
        float f9 = (mBezierControl2.y - mCornerY) / dis;
        mMatrixArray[0] = 1 - 2 * f9 * f9;
        mMatrixArray[1] = 2 * f8 * f9;
        mMatrixArray[3] = mMatrixArray[1];
        mMatrixArray[4] = 1 - 2 * f8 * f8;
        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);
        mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
        mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
        canvas.drawBitmap(bitmap, mMatrix, mPaint);
        mPaint.setColorFilter(null);
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
        mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
                (int) (mBezierStart1.y + mMaxLength));
        mFolderShadowDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            mTouchPoint.x = x;
            mTouchPoint.y = y;
            postInvalidate();
        }
    }


    public void startAnimation(int delayMillis) {
        int dx, dy;
        // dx 水平方向滑动的距离，负值会使滚动向左滚动
        // dy 垂直方向滑动的距离，负值会使滚动向上滚动
        if (mCornerX > 0) {
            dx = -(int) (mViewWidth + mTouchPoint.x);
        } else {
            dx = (int) (mViewWidth - mTouchPoint.x + mViewWidth);
        }
        if (mCornerY > 0) {
            dy = (int) (mViewHeight - mTouchPoint.y);
        } else {
            dy = (int) (1 - mTouchPoint.y); // 防止mTouch.y最终变为0
        }
        mScroller.startScroll((int) mTouchPoint.x, (int) mTouchPoint.y, dx, dy,
                delayMillis);
    }

    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    public boolean canDragOver() {
        if (mTouchToCornerDis > mViewWidth / 10)
            return true;
        return false;
    }

    /**
     * 是否从左边翻向右边
     */
    public boolean DragToRight() {
        if (mCornerX > 0)
            return false;
        return true;
    }

    public void setBookPageBezierHelper(BookPageBezierHelper bookPageBezierHelper) {
        mBookPageBezierHelper = bookPageBezierHelper;
    }

    private OnUserNeedSettingListener mOnUserNeedSettingListener;

    public OnUserNeedSettingListener getOnUserNeedSettingListener() {
        return mOnUserNeedSettingListener;
    }

    public void setOnUserNeedSettingListener(OnUserNeedSettingListener onUserNeedSettingListener) {
        mOnUserNeedSettingListener = onUserNeedSettingListener;
    }

    public interface OnUserNeedSettingListener{
        void onUserNeedSetting();
    }
}
