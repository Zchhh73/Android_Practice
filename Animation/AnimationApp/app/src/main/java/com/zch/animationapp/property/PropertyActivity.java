package com.zch.animationapp.property;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;

import com.zch.animationapp.R;

public class PropertyActivity extends AppCompatActivity {
    private static final String TAG="PropertyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
    }

    public void myClick(View v){
        switch (v.getId()){
//            case R.id.btn_value:
//                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f,1.0f);
//                //匀速运动
//                valueAnimator.setInterpolator(new LinearInterpolator());
//                valueAnimator.setDuration(100);
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        //0.0->1.0
//                        float animated = animation.getAnimatedFraction();
//
//                        float animatedValue=(float)animation.getAnimatedValue();
//                        Log.d(TAG, "onAnimationUpdate: "+String.format("%.3f  %d",animated,animatedValue));
//                    }
//                });
//                valueAnimator.start();
//                break;
            case R.id.view_alpha:
                Animator alphaAnimator=AnimatorInflater.loadAnimator(this, R.animator.alpha);
                alphaAnimator.setTarget(v);
                alphaAnimator.start();
                break;
            case R.id.view_scale:
//                ObjectAnimator.ofFloat(v,"scaleX",1.0f,3.0f).start();
                ObjectAnimator moveIn = ObjectAnimator.ofFloat(v, "translationX", -500f, 0f);
                ObjectAnimator rotate = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(rotate).with(fadeInOut).after(moveIn);
                animSet.setDuration(5000);
                animSet.start();
                break;
            case R.id.view_translate:
//                ViewPropertyAnimator translateAnimate=v.animate();
//                translateAnimate.translationX(500f);
//                translateAnimate.start();
                v.animate().translationX(500f).setDuration(1000).start();
                break;
            case R.id.view_rotate:
                v.animate().rotation(720).start();
                break;
            case R.id.view_set:
//                Animator rotateAnimator = ObjectAnimator.ofFloat(v,"rotation",0,720);
//                rotateAnimator.setDuration(1000);
//
//                Animator moveAnimator = ObjectAnimator.ofFloat(v,"x",0,500);
//                moveAnimator.setDuration(1000);
//
//                AnimatorSet set = new AnimatorSet();
////                set.playTogether(rotateAnimator,moveAnimator);
//                set.playSequentially(rotateAnimator,moveAnimator);
//                set.start();
                v.animate().rotation(720).setDuration(1000).start();
                v.animate().translationX(500).setDuration(1000).setStartDelay(1000).start();
                break;
        }
    }
}
