package com.zch.viewpagerapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

public class MyViewPager extends ViewPager {

    private static final String TAG="MyViewPager";
    //子view的X中心点距离父布局X的中心点的绝对距离的存放集合
    private ArrayList<Integer> childCenterXAbs=new ArrayList<>();
    //子view中心点作为key,存放的view最初的索引位置
    private SparseArray<Integer> childIndex=new SparseArray<>();
    //最大的放大系数，建议是1.0
    private float mScaleMax=1.0f;
    //最小的缩小系数，建议不要和mScaleMax 相差太多
    private float mScaleMin=0.914f;
    //中间的view覆盖两侧的view的大小
    private float mCoverWidth=40f;


    public MyViewPager(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    /**
     * 设置中间放大系数
     *
     * @param mScaleMax
     */
    public void setmScaleMax(float mScaleMax) {
        this.mScaleMax=mScaleMax;
    }

    /**
     * 设置左右缩小系数
     *
     * @param mScaleMin
     */
    public void setmScaleMin(float mScaleMin) {
        this.mScaleMin=mScaleMin;
    }

    /**
     * 设置重叠大小
     *
     * @param mCoverWidth
     */
    public void setmCoverWidth(float mCoverWidth) {
        this.mCoverWidth=mCoverWidth;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MyViewPager);
            mScaleMax=typedArray.getFloat(R.styleable.MyViewPager_svp_maxScale, 1f);
            mScaleMin=typedArray.getFloat(R.styleable.MyViewPager_svp_minScale, 0.914f);
            mCoverWidth=typedArray.getDimension(R.styleable.MyViewPager_svp_coverWidth, 40f);
            typedArray.recycle();
        }
        setPageTransformer(true, new SPageTransformer());
        //clipToPadding如果为false，则view内部的padding区也可以显示view。
        setClipToPadding(false);
        //设置此模式，滑到边界后继续滑动也不会出现弧形光晕
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 改变子类的绘制顺序就要重写该方法
     *
     * @param childCount 子类个数
     * @param i          当前迭代顺序
     * @return 第n个位置的child的绘制索引
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == 0 || childIndex.size() != childCount) {
            childCenterXAbs.clear();
            childIndex.clear();
            //获得ViewPager的中心点在屏幕的位置
            int viewCenterX=getViewCenterX(this);
            for (int n=0; n < childCount; ++n) {
                //计算每个子View中心点的X值和ViewPager中心点的X值的距离
                int indexAbs=Math.abs(viewCenterX - getViewCenterX(getChildAt(n)));
                //两个距离相同，后来的那个自增，保持abs不同
                if (childIndex.get(indexAbs) != null) {
                    ++indexAbs;
                }
                //保存
                childCenterXAbs.add(indexAbs);
                childIndex.append(indexAbs, n);
            }
            //排序
            Collections.sort(childCenterXAbs);//1,0,2  0,1,2
        }
        //先绘制距离中心点远的item，然后绘制距离中心点近的item。
        return childIndex.get(childCenterXAbs.get(childCount - 1 - i));
    }

    //获取某个view的中心点在屏幕上的X值
    private int getViewCenterX(View view) {
        int[] array=new int[2];
        view.getLocationOnScreen(array);
        return array[0] + view.getWidth() / 2;
    }

    class SPageTransformer implements ViewPager.PageTransformer {

        private float reduceX=0.0f;
        private float itemWidth=0;
        private float offsetPosition=0f;

        @Override
        public void transformPage(@NonNull View view, float v) {
            if (offsetPosition == 0f) {
                float paddingLeft=MyViewPager.this.getPaddingLeft();
                float paddingRight=MyViewPager.this.getPaddingRight();
                float width=MyViewPager.this.getMeasuredWidth();
                offsetPosition=paddingLeft / (width - paddingLeft - paddingRight);
            }
            float currentPos=v - offsetPosition;
            if (itemWidth == 0) {
                itemWidth=view.getWidth();
                //由于左右边的缩小而减小x大小的一半
                reduceX=(2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f;
            }
            if (currentPos <= -1.0f) {
                view.setTranslationX(reduceX + mCoverWidth);
                view.setScaleX(mScaleMin);
                view.setScaleY(mScaleMin);
            } else if (currentPos <= 1.0) {
                float scale=(mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos));
                float translationX=currentPos * -reduceX;
                if (currentPos <= -0.5) {//两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
                    view.setTranslationX(translationX + mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
                } else if (currentPos <= 0.0f) {
                    view.setTranslationX(translationX);
                } else if (currentPos >= 0.5) { //两个view中间的临界，这时两个view在同一层
                    view.setTranslationX(translationX - mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
                } else {
                    view.setTranslationX(translationX);
                }
                view.setScaleX(scale + mScaleMin);
                view.setScaleY(scale + mScaleMin);
            } else {
                view.setScaleX(mScaleMin);
                view.setScaleY(mScaleMin);
                view.setTranslationX(-reduceX - mCoverWidth);
            }
        }
    }
}
