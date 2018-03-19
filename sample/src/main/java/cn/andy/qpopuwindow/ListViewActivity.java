package cn.andy.qpopuwindow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Shuai.Li13 on 2018/3/16.
 */

public class ListViewActivity extends AppCompatActivity{

    private int rawX;
    private int rawY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
       final ListView listView= findViewById(R.id.listview);

       listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Datas.getDatas()));
       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

               QPopuWindow.getInstance(ListViewActivity.this).builder//-->通过单例模式获取builder对象
                       .bindView(view,position)//------------------------>绑定view,此方法必须调用,view必须是长按的那个view,position为view在listview的位置
                       .setPopupItemList(new String[]{"复制","粘贴","转发","更多...."})//->设置pop的数据源,此方法必须调用
                       .setPointers(rawX,rawY)//-------------------------->设置手指在屏幕触摸的绝对位置坐标,此方法必须调用
                       .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {//pop item的点击事件监听回调
                           /**
                            * @param anchorView 为pop的绑定view
                            * @param anchorViewPosition  pop绑定view在ListView的position
                            * @param position  pop点击item的position 第一个位置索引为0
                            */
                           @Override
                           public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                               Toast.makeText(ListViewActivity.this,anchorViewPosition+"---->"+position,Toast.LENGTH_SHORT).show();
                           }
                       }).show();//--------------------------------------->pop显示,此方法必须调用
               return true;
           }
       });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rawX= (int) ev.getRawX();
        rawY= (int) ev.getRawY();
        return super.dispatchTouchEvent(ev);
    }

}
