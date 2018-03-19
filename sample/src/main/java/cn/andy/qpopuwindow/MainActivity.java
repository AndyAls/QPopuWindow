package cn.andy.qpopuwindow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.bt_commview:
                startActivity(new Intent(this,CommActivity.class));
                break;
            case R.id.bt_listview:
                startActivity(new Intent(this,ListViewActivity.class));
                break;
            case R.id.bt_recylerview:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
                default:
        }

    }
}
