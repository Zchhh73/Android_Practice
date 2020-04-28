package com.uestc.bookreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.uestc.bookreader.view.BookPageBezierHelper;
import com.uestc.bookreader.view.BookPageView;

import org.w3c.dom.Text;

import java.io.IOException;

public class BookActivity extends AppCompatActivity {
    private BookPageView mBookPageView;
    private TextView mProgressTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

        setContentView(R.layout.activity_book);


        mBookPageView=findViewById(R.id.book_pageview);
        mProgressTextView = findViewById(R.id.progress);

        //get size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //set book helper
        final BookPageBezierHelper helper = new BookPageBezierHelper(width,height);
        mBookPageView.setBookPageBezierHelper(helper);

        //current page, next page
        Bitmap currentPageBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Bitmap nextPageBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);

        mBookPageView.setBitmaps(currentPageBitmap,nextPageBitmap);

        if(getIntent() != null){
            String filepath = getIntent().getStringExtra("file_path");
            if(!TextUtils.isEmpty(filepath)){
                try {
                    helper.openBook(filepath);
                    helper.draw(new Canvas(currentPageBitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                    //
                }
            }else{
                //

            }
        }else{
            //
        }

    }

    public static void start(Context context,String filePath){
        Intent intent = new Intent(context,BookActivity.class);
        intent.putExtra("file_path",filePath);
        context.startActivity(intent);
    }
}
