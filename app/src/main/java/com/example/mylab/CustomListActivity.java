package com.example.mylab;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CustomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "CustomListActivity";
    private ListView mylist;
    private Handler handler;
    private View progressBar;
    private MyAdapter adapter;

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

        ArrayList<HashMap<String, String>> listItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ItemTitle", "Rate: " + i);
            map.put("ItemDetail", "detail" + i);
            listItems.add(map);
        }

        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 3) {
                    Log.i(TAG, "handleMessage:获得网络数据");
                    ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) msg.obj;
                    adapter = new MyAdapter(CustomListActivity.this, R.layout.list_item, list2);
                    mylist.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
                super.handleMessage(msg);
            }
        };

        progressBar = findViewById(R.id.progressBar);
        mylist = findViewById(R.id.mylist2);
        mylist.setOnItemClickListener(this);
        mylist.setOnItemLongClickListener(this);
        if(needUpdateToday()){
            //需要联网更新
        new Thread(() -> {
            Document doc = null;
            ArrayList<HashMap<String, String>> retlist = new ArrayList<>();
            List<RateItem> rateItems = new ArrayList<>();
            try {
                Thread.sleep(1000);
                doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
                Log.i(TAG, "run:title =" + doc.title());
                Elements tables = doc.getElementsByTag("table");
                Element table = tables.get(0);
                Elements trs = table.getElementsByTag("tr");
                trs.remove(0);
                trs.remove(0);
                trs.remove(0);
                for (Element tr : trs) {
                    Elements tds = tr.children();
                    Element td1 = tds.first();
                    Element td2 = tds.get(1);
                    String str1 = td1.text();
                    String str2 = td2.text();
                    float rate = 100 / Float.parseFloat(str2);
                    String rateStr = Float.toString(rate);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("ItemTitle", str1);
                    map.put("ItemDetail", rateStr);
                    retlist.add(map);

                    rateItems.add(new RateItem(str1, rate));
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 写入数据库
            RateManager manager = new RateManager(CustomListActivity.this);
            manager.addAll(rateItems);
            saveUpdateDate(); // 保存今天已更新
            // 通知主线程刷新 UI
            Message msg = handler.obtainMessage(3, retlist);
            handler.sendMessage(msg);
            Log.i(TAG, "onCreate:handler.sendMessage(msg)");
        }).start();
    }else {
    // 直接从数据库读取
    RateManager manager = new RateManager(this);
    List<RateItem> dbList = manager.getAll();
    ArrayList<HashMap<String, String>> retlist = new ArrayList<>();
    for (RateItem item : dbList) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ItemTitle", item.getCname());
        map.put("ItemDetail", String.valueOf(item.getFval()));
        retlist.add(map);
    }
    // 刷新UI
    adapter = new MyAdapter(this, R.layout.list_item, retlist);
    mylist.setAdapter(adapter);
    progressBar.setVisibility(View.GONE);
}
    }
    //判断是否需要更新
    private boolean needUpdateToday() {
        SharedPreferences prefs = getSharedPreferences("rate_update", MODE_PRIVATE);
        String lastDate = prefs.getString("last_update_date", "");
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return !today.equals(lastDate);
    }

    private void saveUpdateDate() {
        SharedPreferences prefs = getSharedPreferences("rate_update", MODE_PRIVATE);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        prefs.edit().putString("last_update_date", today).apply();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        HashMap<String, String> item = (HashMap<String, String>) mylist.getItemAtPosition(position);
        String titleStr = item.get("ItemTitle");
        String detailStr = item.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: 对话框事件处理");
                        adapter.remove(mylist.getItemAtPosition(position));
                    }
                }).setNegativeButton("否", null);
        builder.create().show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemLongClick");
        return true;
    }
}