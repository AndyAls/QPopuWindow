package cn.andy.qpopuwindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shuai.Li13 on 2018/3/16.
 */

public class Datas {

    public static List<String> getDatas(){

        List<String> mDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDatas.add("中华人民共和国"+i);
        }
        return mDatas;
    }
}
