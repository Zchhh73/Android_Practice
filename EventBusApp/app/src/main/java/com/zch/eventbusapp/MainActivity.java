package com.zch.eventbusapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private ImageView iv_pic;


    //事件的订阅方
    @Override
    protected void onStart() {
        super.onStart();
        //注册对象
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //回调处理函数
    @Subscribe
    public void onSuccessEvent(SuccessEvent event){
        setImageSrc(R.drawable.ic_happy);
    }

    @Subscribe
    public void onFailureEvent(FailureEvent event){
        setImageSrc(R.drawable.ic_sad);
    }

    @Subscribe(threadMode=ThreadMode.POSTING)
    public void onPostingEvent(final PostingEvent event){
        final String threadInfo = Thread.currentThread().toString();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadInfo);
            }
        });
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onMainEvent(MainEvent event){
        setPublisherThreadInfo(event.threadInfo);
        setSubscriberThreadInfo(Thread.currentThread().toString());
    }

    @Subscribe(threadMode=ThreadMode.MAIN_ORDERED)
    public void onMainOrderedEvent(MainOrderedEvent event){
        Log.d(TAG, "onMainOrderedEvent: Enter @" + SystemClock.uptimeMillis());
        setPublisherThreadInfo(event.threadInfo);
        setSubscriberThreadInfo(Thread.currentThread().toString());
        //休眠
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onMainOrderedEvent: Exit @"+SystemClock.uptimeMillis());
    }

    @Subscribe(threadMode=ThreadMode.BACKGROUND)
    public void onBackgroundEvent(final BackgroundEvent event){
        final String threadinfo = Thread.currentThread().toString();
        //后台运行
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadinfo);
            }
        });

    }

    @Subscribe(threadMode=ThreadMode.ASYNC)
    public void onAsyncEvent(final AsyncEvent event){
        final String threadinfo = Thread.currentThread().toString();
        //后台运行
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadinfo);
            }
        });

    }

    private void setPublisherThreadInfo(String threadInfo){
        setTextView(R.id.publisherThreadTextView,threadInfo);
    }

    private void setSubscriberThreadInfo(String threadInfo){
        setTextView(R.id.subscriberThreadTextView,threadInfo);
    }

    //运行在UI上
    private void setTextView(int resId,String text) {
        final TextView textView = findViewById(resId);
        textView.setText(text);
        textView.setAlpha(.5f);
        textView.animate().alpha(1).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Subscriber");
        Toolbar toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击显示
                final PublisherDialogFragment fragment=new PublisherDialogFragment();
                fragment.show(getSupportFragmentManager(),"Publisher");
            }
        });
    }

    /**
     * 更新图片资源
     * @param resId
     */
    private void setImageSrc(int resId){
        iv_pic= findViewById(R.id.emotionImageView);
        iv_pic.setImageResource(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id=item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sticky) {
            Intent intent = new Intent(this,StickyActivity.class);
            //粘性事件发布
            EventBus.getDefault().postSticky(new StickyMessageEvent("sticky-message-content"));
            //在启动
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
