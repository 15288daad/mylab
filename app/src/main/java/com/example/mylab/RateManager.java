package com.example.mylab;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class RateManager {
    private DBHelper dbHelper;
    private String TBNAME;
    public RateManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }
    public void add(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CURNAME", item.getCname());
        values.put("CURRATE", item.getFval());
        db.insert(TBNAME, null, values);
        db.close();
    }
    public void addAll(List<RateItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null); // 先清空旧数据
        for (RateItem item : list) {
            ContentValues values = new ContentValues();
            values.put("CURNAME", item.getCname());
            values.put("CURRATE", item.getFval());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }
    public List<RateItem> getAll() {
        List<RateItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String cname = cursor.getString(cursor.getColumnIndexOrThrow("CURNAME"));
                float fval = cursor.getFloat(cursor.getColumnIndexOrThrow("CURRATE"));
                RateItem item = new RateItem(cname, fval);
                list.add(item);
            }
            cursor.close();
        }
        db.close();
        return list;
    }
}