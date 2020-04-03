package com.zch.networkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONActivity extends AppCompatActivity {
    private TextView txt1, txt2;
    private ListView listView;
    private List<Map<String, Object>> lists=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        //主线程
        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);
        listView = findViewById(R.id.listview);


        findViewById(R.id.jsondata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                parseByJSONObject();
                parseByGSON();
            }
        });
    }

    public void parseByGSON(){
        //添加依赖
        //实例化GSON对象
        final Gson gson = new Gson();
        //3.toJson 将对象变为字符串
        Book b = new Book("Android","Zchhh","android开发");
        String str = gson.toJson(b);
        Log.e("TAG", str);
        //4.fromJson
        Book b2 = gson.fromJson(str,Book.class);
        Log.e("TAG", b2+"===");
        Log.e("TAG", "content:"+b2.getContent() );

        new Thread(){
            @Override
            public void run() {
                super.run();
                String msg = get();
                Test t = gson.fromJson(msg,Test.class);
                Log.e("TAG", t+" ");
                Log.e("TAG", t.getMsg()+"--"+t.getStatus()+"--"+t.getData().getContent());
            }
        }.start();

    }

    public void parseByJSONObject() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str=get();
                //解析
                //JSONObject json对象 JSONArray json数组
                //参数满足json格式
                try {
                    JSONObject jo=new JSONObject(str);
                    int status=jo.getInt("status");
                    final String msg=jo.getString("msg");
                    Log.e("TAG", status + " " + "msg");
//                    JSONObject
//                    JSONObject data = jo.getJSONObject("data");
//                    final String title=data.getString("title");


                    //JSONArray
                    JSONArray arr=jo.getJSONArray("data");
                    for (int i=0; i < arr.length(); i++) {
                        //取出对象
                        JSONObject obj=arr.getJSONObject(i);
                        String name=obj.getString("name");
                        String id=obj.getString("id");
                        Log.e("TAG", "id=" + id + ",name=" + name);

                        Map<String, Object> map=new HashMap<>();
                        map.put("name", name);
                        map.put("id", id);
                        lists.add(map);

                    }
                    //SimpleAdapter
                    String[] from={"name", "id"};
                    int[] to={R.id.item_name, R.id.item_id};
                    final SimpleAdapter adapter=new SimpleAdapter(JSONActivity.this, lists, R.layout.item, from, to);


                    //显示到界面上
                    //在子线程中调用可以在内部处理界面的显示问题
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            txt1.setText(msg);
//                            txt2.setText(title);
                            listView.setAdapter(adapter);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }


    private String get() {
        //HttpURLConnection
        //1.实例化URL对象
        try {
            URL url=new URL("http://www.imooc.com/api/teacher?type=3&cid=1");

            //2.获取HttpURLConnetion实例
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            //3.设置请求相关属性
            //请求方式
            conn.setRequestMethod("GET");
            //请求超时时长
            conn.setConnectTimeout(6000);

            //4.获取响应码（发送请求）  200 404未请求到 500服务器异常
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //5.判断响应码并获取响应数据(响应正文)
                //获取响应的流
                InputStream in=conn.getInputStream();
                //在循环中读取输入流
                byte[] b=new byte[1024];//1G
                int len=0;
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                while ((len=in.read(b)) > -1) {
                    //将字节数组存入缓存流,len实际读到的长度。
                    baos.write(b, 0, len);
                }
                String msg=new String(baos.toByteArray());
                return msg;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
