package com.example.mylab;

import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebItemTask implements Runnable{
    private static final String TAG="WebItemTask";
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler=handler;
    }

    @Override
    public void run() {
        Document doc = null;
        ArrayList<RateItem> retlist = new ArrayList<RateItem>();

        try {
            Thread.sleep(1000);
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
                retlist.add(new RateItem(str1,rate));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Message msg = handler.obtainMessage(3,retlist);
        handler.sendMessage(msg);
        Log.i(TAG,"onCreate:handler.sendMessage(msg)");
    }
}
