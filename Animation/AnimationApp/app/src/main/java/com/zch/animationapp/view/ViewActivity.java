package com.zch.animationapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.zch.animationapp.R;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.renew,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.renew:
            recreate();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void myClick(View v){
        switch (v.getId()){
            case R.id.view_alpha:
                Animation alphaAnimation =AnimationUtils.loadAnimation(this,R.anim.alpha);
                v.startAnimation(alphaAnimation);
                break;
            case R.id.view_scale:
                Animation scaleAnimation =AnimationUtils.loadAnimation(this,R.anim.scale);
                v.startAnimation(scaleAnimation);
                break;
            case R.id.view_translate:
                Animation translateAnimation =AnimationUtils.loadAnimation(this,R.anim.translate);
                v.startAnimation(translateAnimation);
                break;
            case R.id.view_rotate:
                Animation rotateAnimation =AnimationUtils.loadAnimation(this,R.anim.rotate);
                v.startAnimation(rotateAnimation);
                break;
            case R.id.view_set:
                Animation setAnimation =AnimationUtils.loadAnimation(this,R.anim.set);
                v.startAnimation(setAnimation);
                break;
            case R.id.viewLinear:
            case R.id.viewAccelerate:
                View viewLinear = findViewById(R.id.viewLinear);
                View viewAccelerate = findViewById(R.id.viewAccelerate);

                Animation LinearAnimation = AnimationUtils.loadAnimation(this,R.anim.translate);
                Animation AccelerateAnimation = AnimationUtils.loadAnimation(this,R.anim.translate);

                LinearAnimation.setInterpolator(new LinearInterpolator());
                AccelerateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

                viewLinear.startAnimation(LinearAnimation);
                viewAccelerate.startAnimation(AccelerateAnimation);
                break;
        }
    }
}
