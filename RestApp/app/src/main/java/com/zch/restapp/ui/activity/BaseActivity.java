package com.zch.restapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.zch.restapp.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mLoadingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(0xff000000);
        }

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage("加载中...");

    }

    protected void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.id_toolbar);
        //传入toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void startLoadingProgress() {
        mLoadingDialog.show();
    }

    protected void stopLoadingProgress() {
        if(mLoadingDialog !=null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadingProgress();
        mLoadingDialog = null;
    }

    protected void toLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        //清除栈顶的东西，singleTask
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
