package com.zch.eventbusapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PublisherDialogFragment extends DialogFragment {
    private static final String TAG="PublisherDialogFragment";


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Publisher");

        final String[] items={"Success", "Failure", "Posting",
                "Main","MainOrdered","Background","Async"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //success
                        //EventBus发布
                        postSuccessEvent();
                        break;
                    case 1:
                        //fail
                        postFailureEvent();
                        break;
                    case 2:
                        //Posting
                        postPostingEvent();
                        break;
                    case 3:
                        //Main Thread
                        postMainEvent();
                        break;
                    case 4:
                        postMainOrderedEvent();
                        break;
                    case 5:
                        //background
                        postBackgroundEvent();
                        break;
                    case 6:
                        //async
                        postAsyncEvent();
                        break;
                }
            }

            private void postAsyncEvent() {
                //非UI线程
                if(Math.random()>.5){
                    ExecutorService pool = Executors.newFixedThreadPool(1);
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new AsyncEvent(Thread.currentThread().toString()));
                        }
                    });
                    pool.shutdown();
                }else{
                    //UI线程发布
                    EventBus.getDefault().post(new BackgroundEvent(Thread.currentThread().toString()));
                }
            }

            private void postBackgroundEvent() {
                //非UI线程
                if(Math.random()>.5){
                    ExecutorService pool = Executors.newFixedThreadPool(1);
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new BackgroundEvent(Thread.currentThread().toString()));
                        }
                    });
                    pool.shutdown();
                }else{
                    //UI线程发布
                    EventBus.getDefault().post(new BackgroundEvent(Thread.currentThread().toString()));

                }
            }

            private void postMainOrderedEvent() {
                Log.d(TAG, "onClick: before @"+ SystemClock.uptimeMillis());
                EventBus.getDefault().post(new MainOrderedEvent(Thread.currentThread().toString()));
                Log.d(TAG, "onClick: after @"+ SystemClock.uptimeMillis());
            }

            private void postMainEvent() {
                if (Math.random() > .5) {
                    new Thread("working-Thread") {
                        @Override
                        public void run() {
                            super.run();
                            EventBus.getDefault().post(new MainEvent(Thread.currentThread().toString()));
                        }
                    }.start();
                } else {
                    EventBus.getDefault().post(new MainEvent(Thread.currentThread().toString()));
                }
            }

            private void postPostingEvent() {
                if (Math.random() > .5) {
                    new Thread("posting-002") {
                        @Override
                        public void run() {
                            super.run();
                            EventBus.getDefault().post(new PostingEvent(Thread.currentThread().toString()));
                        }
                    }.start();
                } else {
                    EventBus.getDefault().post(new PostingEvent(Thread.currentThread().toString()));
                }
            }

            private void postFailureEvent() {
                EventBus.getDefault().post(new FailureEvent());
            }

            private void postSuccessEvent() {
                EventBus.getDefault().post(new SuccessEvent());
            }
        });

        return builder.create();
    }
}
