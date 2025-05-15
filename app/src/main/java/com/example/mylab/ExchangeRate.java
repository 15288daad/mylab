package com.example.mylab;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExchangeRate extends AppCompatActivity implements  Runnable{
    private static final String TAG="RateActivity";
    private EditText inputRmb;
    private TextView tvResult;
    float dollarhl=0.13931f;
    float eurohl=0.12711f;
    float wonhl=201.0477f;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);
        inputRmb = findViewById(R.id.input_rmb);
        tvResult = findViewById(R.id.result);
        // 这里设置按钮点击事件
        findViewById(R.id.btn1).setOnClickListener(v -> myclick(v));
        findViewById(R.id.btn2).setOnClickListener(v -> myclick(v));
        findViewById(R.id.btn3).setOnClickListener(v -> myclick(v));
        findViewById(R.id.btn4).setOnClickListener(v -> openConfig(v));
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==7){
                    Bundle bdl2 = (Bundle) msg.obj;
                    dollarhl = bdl2.getFloat("web_dollar");
                    eurohl = bdl2.getFloat("web_euro");
                    wonhl = bdl2.getFloat("web_won");
                    Log.i(TAG,"handleMessage:ret dollar="+dollarhl);
                    Log.i(TAG,"handleMessage:ret euro="+eurohl);
                    Log.i(TAG,"handleMessage:ret won="+wonhl);
                }
                super.handleMessage(msg);
            }
        };
        Log.i(TAG,"onCreate:启动线程");
        Thread t = new Thread(this);
        t.start();
    }
    public void myclick(View btn) {
        Log.i(TAG, "myclick: Button clicked");
        String strinput = inputRmb.getText().toString();
        try {
            float inputhf = Float.parseFloat(strinput);
            float result = 0;
            // 根据按钮ID选择不同的汇率计算
            if (btn.getId() == R.id.btn1) {
                result = inputhf * dollarhl;
            } else if (btn.getId() == R.id.btn2) {
                result = inputhf * eurohl;
            } else {
                result = inputhf * wonhl;
            }
            // 显示计算结果
            tvResult.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的数据", Toast.LENGTH_SHORT).show();
        }
    }
    public void openConfig(View btn) {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("key_dollar", dollarhl);
        intent.putExtra("key_euro", eurohl);
        intent.putExtra("key_won", wonhl);
        startActivityForResult(intent, 6); // 需要使用 startActivityForResult 以便获取结果
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 6 && resultCode == RESULT_OK) {
            // 获取从 SettingActivity 返回的更新汇率
            if (data != null) {
                dollarhl = data.getFloatExtra("ret_dollar", dollarhl);
                eurohl = data.getFloatExtra("ret_euro", eurohl);
                wonhl = data.getFloatExtra("ret_won", wonhl);

                Log.i(TAG, "onActivityResult: dollarhl=" + dollarhl);
                Log.i(TAG, "onActivityResult: eurohl=" + eurohl);
                Log.i(TAG, "onActivityResult: wonhl=" + wonhl);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        //忙了很久1
        Log.i(TAG,"run:running......");
        Bundle retbdl = new Bundle();
            try{
                // 获取网络数据
                Thread.sleep(5000);
                URL url = null;
                try {
                    //url = new URL ("https://www.boc.cn/sourcedb/whpj/");
                    //HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    //InputStream in = http.getInputStream();
                    //String html = inputStream2String(in);
                    //Log.i(TAG, "run: html=" + html);
                     Document doc =  Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
                     Log.i(TAG,"run:title="+doc.title());
                     Elements tables=doc.getElementsByTag("table");
                     Element table =tables.get(1);
                     Log.i(TAG,"run:table2=" +table);
                     Elements trs = table.getElementsByTag("tr");
                     trs.remove(0);
                     for(Element tr :trs){
                        Elements tds = tr.children();
                        Element td1 = tds.first();
                        Element td2 = tds.get(5);
                        String str1 = td1.text();
                        String str2 = td2.text();
                        Log.i(TAG,"run:"+str1+"==>"+str2);
                        float r = 100/Float.parseFloat(str2);
                        if("美元".equals(str1)){
                            retbdl.putFloat("web_dollar",r);}
                        else if("欧元".equals(str1)){
                            retbdl.putFloat("web_euro",r);}
                        else if("韩国元".equals(str1)){
                            retbdl.putFloat("web_won",r);}

                     }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        Message msg = handler.obtainMessage(7,retbdl);
        handler.sendMessage(msg);
        Log.i(TAG,"run:消息发送完毕");
    }
    private String inputStream2String(InputStream inputStream)
            throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}