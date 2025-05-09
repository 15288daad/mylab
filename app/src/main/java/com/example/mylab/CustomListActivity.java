package com.example.mylab;

import android.annotation.SuppressLint;
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

public class CustomListActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener{
    private static final String TAG="CustomListActivity";
    private ListView mylist;
    private  Handler handler;
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
                    ArrayList<HashMap<String,String>> list2 = (ArrayList<HashMap<String,String>>)msg.obj;
                    MyAdapter adapter2 = new MyAdapter(CustomListActivity.this,R.layout.list_item,list2);
                    mylist.setAdapter(adapter2);

                }
                super.handleMessage(msg);
            }
        };

        MyAdapter myAdapter = new MyAdapter(this,R.layout.list_item,listItems);
        mylist = findViewById(R.id.mylist2);
        mylist.setAdapter(myAdapter);
        mylist.setOnItemClickListener(this);
        //启动线程
//        new Thread(()->{
//            Document doc = null;
//            ArrayList<HashMap<String,String>> retlist = new ArrayList<HashMap<String,String>>();
//
//            try {
//                doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
//
//            Log.i(TAG,"run:title ="+doc.title());
//                Elements tables = doc.getElementsByTag("table");
//                Element table = tables.get(1);
//                Elements trs = table.getElementsByTag("tr");
//                trs.remove(0);
//                for(Element tr : trs){
//                    Elements tds = tr.children();
//                    Element td1 = tds.first();
//                    Element td2 = tds.get(5);
//                    String str1 = td1.text();
//                    String str2 = td2.text();
//
//                    Log.i(TAG, "run: " + str1 + " ==> " + str2);
//                    HashMap<String,String> map = new HashMap<String,String>();
//                    map.put("ItemTitle",str1);
//                    map.put("ItemDetail",str2);
//                    retlist.add(map);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//                Message msg = handler.obtainMessage(3,retlist);
//                handler.sendMessage(msg);
//                Log.i(TAG,"onCreate:handler.sendMessage(msg)");
//
//        }).start();
        new Thread(()->{
            Document doc = null;
            ArrayList<HashMap<String,String>> retlist = new ArrayList<HashMap<String,String>>();

            try {
                doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();

                Log.i(TAG,"run:title ="+doc.title());
                Elements tables = doc.getElementsByTag("table");
                Element table = tables.get(0);
                Elements trs = table.getElementsByTag("tr");
                trs.remove(0);
                trs.remove(0);
                trs.remove(0);
                for(Element tr : trs){
                    Elements tds = tr.children();
                    Element td1 = tds.first();
                    Element td2 = tds.get(1);
                    String str1 = td1.text();
                    String str2 = td2.text();
                    float rate = 100 / Float.parseFloat(str2);
                    str2 = Float.toString(rate);

                    Log.i(TAG, "run: " + str1 + " ==> " + str2);
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("ItemTitle",str1);
                    map.put("ItemDetail",str2);
                    retlist.add(map);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Message msg = handler.obtainMessage(3,retlist);
            handler.sendMessage(msg);
            Log.i(TAG,"onCreate:handler.sendMessage(msg)");

        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        Object itemAtPosition = mylist.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);
        //打开新的窗口

        Intent intent = new Intent(this, DetailRateShow.class);
        intent.putExtra("Nation", titleStr);
        intent.putExtra("Rate", detailStr);
        startActivity(intent);
    }
}