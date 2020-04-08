package com.zch.expandablelistview.biz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zch.expandablelistview.MainActivity;
import com.zch.expandablelistview.bean.Chapter;
import com.zch.expandablelistview.bean.ChapterItem;
import com.zch.expandablelistview.dao.ChapterDao;
import com.zch.expandablelistview.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChapterBiz {

    private ChapterDao mChapterDao=new ChapterDao();

    public void loadDatas(final Context context, final CallBack callBack, boolean useCache) {

        AsyncTask<Boolean, Void, List<Chapter>> asyncTask=new AsyncTask<Boolean, Void, List<Chapter>>() {
            private Exception ex;

            @Override
            protected List<Chapter> doInBackground(Boolean... booleans) {
                //取出是否用缓存
                boolean isUseCache=booleans[0];
                List<Chapter> chapterList=new ArrayList<>();

                try {
                    //缓存加载
                    if (isUseCache) {
                        List<Chapter> chapterListFromDb=mChapterDao.loadFromDb(context);
                        Log.d(MainActivity.TAG, "chapterListFromDb= " + chapterListFromDb);
                        chapterList.addAll(chapterListFromDb);
                    }
                    //从网络加载
                    if (chapterList.isEmpty()) {
                        List<Chapter> chapterListFromNet=loadFromNet(context);
                        //cache to db
                        mChapterDao.insert2Db(context,chapterListFromNet);
                        chapterList.addAll(chapterListFromNet);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.ex=ex;
                }

                return chapterList;
            }

            @Override
            protected void onPostExecute(List<Chapter> chapters) {
                if (ex != null) {
                    callBack.onFailed(ex);
                    return;
                }
                callBack.onSuccess(chapters);
            }
        };

        asyncTask.execute(useCache);
    }

    public List<Chapter> loadFromNet(Context context) {
        List<Chapter> chapterList=new ArrayList<>();
        String url="http://www.imooc.com/api/expandablelistview";
        //1.请求
        String content=HttpUtils.doGet(url);
        Log.d(MainActivity.TAG, "content = " + content);

        //2.转换数据
        if (content != null) {
            chapterList=parseContent(content);
            Log.d(MainActivity.TAG, "parse finish chapterlist= " + chapterList);

        }
        return chapterList;

    }

    private List<Chapter> parseContent(String content) {
        List<Chapter> chapterList=new ArrayList<>();

        try {
            JSONObject root=new JSONObject(content);
            int errorCode=root.optInt("errorCode");
            if (errorCode == 0) {
                JSONArray dataJsonArray=root.optJSONArray("data");
                for (int i=0; i < dataJsonArray.length(); i++) {
                    //chapter
                    JSONObject chapterJsonObj=dataJsonArray.getJSONObject(i);
                    int id=chapterJsonObj.optInt("id");
                    String name=chapterJsonObj.optString("name");
                    Chapter chapter=new Chapter(id, name);
                    chapterList.add(chapter);

                    //parse chapter items
                    JSONArray childrenJsonArr=chapterJsonObj.optJSONArray("children");
                    if (childrenJsonArr != null) {
                        for (int j=0; j < childrenJsonArr.length(); j++) {
                            JSONObject chapterItemJsonObj=childrenJsonArr.getJSONObject(j);
                            int cid=chapterItemJsonObj.optInt("id");
                            String cname=chapterItemJsonObj.optString("name");
                            ChapterItem chapterItem=new ChapterItem(cid, cname);
                            chapter.addChild(chapterItem);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return chapterList;
    }


    public static interface CallBack {
        void onSuccess(List<Chapter> chapterList);

        void onFailed(Exception ex);

    }
}
