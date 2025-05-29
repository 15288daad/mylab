package com.example.mylab;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomListItemActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private static final String TAG="CustomListItemActivity";
    private ListView mylist;
    private  Handler handler;
    private  View progressBar;
    private ItemAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mylist2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ArrayList<HashMap<String,String>>listItems= new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate: " + i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            listItems.add(map);
        }

// 生成适配器的 Item 和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,
                listItems, // listItems 数据源
                R.layout.list_item, // ListItem 的 XML 布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
        handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(@NonNull Message msg){
                if(msg.what==3){
                    Log.i(TAG,"handleMessage:获得网络数据");
                    ArrayList<RateItem> item = (ArrayList<RateItem>)msg.obj;
                    adapter = new ItemAdapter(CustomListItemActivity.this,R.layout.list_item,item);
                    mylist.setAdapter(adapter);
                    //隐藏进度条
                    progressBar.setVisibility(View.GONE);
                }
                super.handleMessage(msg);
            }
        };

        //MyAdapter myAdapter = new MyAdapter(this,R.layout.list_item,listItems);
        progressBar = findViewById(R.id.progressBar);
        mylist = findViewById(R.id.mylist2);
        //mylist.setAdapter(myAdapter);
        mylist.setOnItemClickListener(this);
        mylist.setOnItemLongClickListener(this);

        //启动线程
        WebItemTask task = new WebItemTask();
        task.setHandler(handler);
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        Object itemAtPosition = mylist.getItemAtPosition(position);
        // HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        RateItem item = (RateItem) itemAtPosition;

        String titleStr = item.getCname();
        String detailStr = Float.toString(item.getFval());
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);

//        //点击列表行，打开改行汇率换算页面，（打开新窗口）
//        Intent intent = new Intent(this, DetailRateShow.class);
//        intent.putExtra("Nation", titleStr);
//        intent.putExtra("Rate", detailStr);
//        startActivity(intent);

        //点击，弹出对话框，选择是否删除点击的信息
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: 对话框事件处理");
                        //删除数据项
                        adapter.remove(mylist.getItemAtPosition(position));
                    }
                }).setNegativeButton("否", null);
        builder.create().show();

    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG,"onItemLongClick");
        //长按住则删除信息，这个放在长按则长按删除，放在上面的单击，则单击删除
        //adapter.remove(mylist.getItemAtPosition(position));

        return true;
    }
}