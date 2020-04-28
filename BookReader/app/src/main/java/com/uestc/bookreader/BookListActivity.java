package com.uestc.bookreader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BookListActivity extends AppCompatActivity {

    private static final String TAG="BookListActivity";

    private ListView mListView;
    private AsyncHttpClient mClient;
    private List<BookListResult.BookData> books = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booklist);

        //请求权限
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            //动态申请权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }



        mListView=findViewById(R.id.book_listview);


        String url = "http://www.imooc.com/api/teacher?type=10";

        mClient = new AsyncHttpClient();
        mClient.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.i(TAG, "onSuccess: "+new String(responseBody));
                String result = new String(responseBody);
                Gson gson = new Gson();
                BookListResult bookListResult = gson.fromJson(result,BookListResult.class);
                books=bookListResult.getData();
                mListView.setAdapter(new BookListAdapter());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }


    public static void start(Context context){
        Intent intent = new Intent(context,BookListActivity.class);
        context.startActivity(intent);

    }

    private class BookListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Object getItem(int position) {
            return books.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BookListResult.BookData book = books.get(position);
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView=getLayoutInflater().inflate(R.layout.item_book_listview, parent, false);
                viewHolder.mNameTextView = convertView.findViewById(R.id.tv_bookname);
                viewHolder.mButton = convertView.findViewById(R.id.btn_bookitem);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mNameTextView.setText(book.getBookname());
            final String filepath = Environment.getExternalStorageDirectory()+
                    "/Download/"+book.getBookname()+".txt";
            final File file = new File(filepath);
            viewHolder.mButton.setText(file.exists()?"点击打开":"点击下载");

            final ViewHolder finalViewHolder=viewHolder;
            viewHolder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //下载
                    if(file.exists()){
                        BookActivity.start(BookListActivity.this,filepath);

                    }else{
                        mClient.addHeader("Accept-Encoding","identity");
                        mClient.get(book.getBookfile(), new FileAsyncHttpResponseHandler(file) {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                                finalViewHolder.mButton.setText("下载失败");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, File file) {
                                finalViewHolder.mButton.setText("点击打开");
                            }

                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                super.onProgress(bytesWritten, totalSize);
                                finalViewHolder.mButton.setText(String.valueOf(bytesWritten*100/totalSize+"%"));
                            }

                        });
                    }


                }
            });
            return convertView;
        }
        class ViewHolder{
            public TextView mNameTextView;
            public Button mButton;
        }
    }
}
