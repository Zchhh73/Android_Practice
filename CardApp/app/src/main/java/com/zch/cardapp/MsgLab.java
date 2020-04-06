package com.zch.cardapp;

import java.util.ArrayList;
import java.util.List;

public class MsgLab {

    public static List<Msg> generateMockList(){
        List<Msg> msgList = new ArrayList<>();

        Msg msg = new Msg(1,
                R.drawable.img01,
                "如何才能不错过人工智能的时代？",
                "下一个时代就是机器学习的时代，慕课网发大招，与你一起预见未来！");
        msgList.add(msg);

        msg = new Msg(2,
                R.drawable.img02,
                "关于你的面试、实习心路历程",
                "奖品丰富，更设有参与奖，随机抽取5名幸运用户，获得慕课网付费面试课程中的任意一门！");
        msgList.add(msg);

        msg = new Msg(3,
                R.drawable.img03,
                "狗粮不是你想吃，就能吃的！",
                "你的朋友圈开始了吗？一半秀恩爱，一半扮感伤！不怕，还有慕课网陪你坚强地走下去！！");
        msgList.add(msg);

        msg = new Msg(4,
                R.drawable.img04,
                "前端跳槽面试那些事儿",
                "工作有几年了，项目偏简单有点拿不出手怎么办？ 目前还没毕业，正在自学前端，请问可以找到一份前端工作吗，我该怎么办？");
        msgList.add(msg);

        msg = new Msg(5,
                R.drawable.img05,
                "图解程序员怎么过七夕？",
                "哈哈哈哈，活该单身25年！");
        msgList.add(msg);

        return msgList;
    };
}
