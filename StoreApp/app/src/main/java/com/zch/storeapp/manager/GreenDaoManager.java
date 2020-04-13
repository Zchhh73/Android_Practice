package com.zch.storeapp.manager;

import android.content.Context;
import android.widget.Toast;

import com.zch.storeapp.MyApplication;
import com.zch.storeapp.model.GoodsModel;
import com.zch.storeapp.model.GoodsModelDao;
import com.zch.storeapp.utils.DataUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GreenDaoManager {

    private Context mContext;
    private GoodsModelDao dao;


    public GreenDaoManager(Context context){
        mContext = context;
        //初始化
        dao = MyApplication.mySession.getGoodsModelDao();
    }

    /**
     * 添加所有
     */
    public void insertGoods(){
        String json = DataUtils.getJson("goods.json",mContext);
        List<GoodsModel> goodsModels = DataUtils.getGoodsModels(json);

        dao.insertInTx(goodsModels);
        Toast.makeText(mContext, "插入成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查询所有
     * @return
     */
    public List<GoodsModel> queryGoods(){
        QueryBuilder<GoodsModel> query = dao.queryBuilder();
        query=query.orderAsc(GoodsModelDao.Properties.GoodsId);

        return query.list();
    }

    /**
     * 筛选水果
     * @return
     */
    public List<GoodsModel> queryFruits(){
        QueryBuilder<GoodsModel> query = dao.queryBuilder();
        query = query.where(GoodsModelDao.Properties.Type.eq("0"))
                .orderAsc(GoodsModelDao.Properties.GoodsId);
        return query.list();
    }

    /**
     * 筛选零食
     * @return
     */
    public List<GoodsModel> querySnacks(){
        QueryBuilder<GoodsModel> query = dao.queryBuilder();
        query = query.where(GoodsModelDao.Properties.Type.eq("1"))
                .orderAsc(GoodsModelDao.Properties.GoodsId);
        return query.list();
    }

    /**
     * 删除指定数据
     * @param model
     */
    public void deleteGoodsInfo(GoodsModel model){

        dao.deleteByKey(model.getId());
    }


    /**
     * 修改数据
     * @param model
     */
    public void updateGoodsInfo(GoodsModel model){
        dao.update(model);
    }

}
