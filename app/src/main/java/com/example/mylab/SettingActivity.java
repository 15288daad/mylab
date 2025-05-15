package com.example.mylab;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
public class SettingActivity extends AppCompatActivity implements Runnable{
    private static final String TAG = "ExchangeRate";private EditText inpDollar, inpEuro, inpWon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        inpDollar = findViewById(R.id.dr);
        inpEuro = findViewById(R.id.er);
        inpWon = findViewById(R.id.wr);
        Intent intent = getIntent();// 获取传入的汇率
        float dollar = intent.getFloatExtra("key_dollar", 0.1f);
        float euro = intent.getFloatExtra("key_euro", 0.1f);
        float won = intent.getFloatExtra("key_won", 0.1f);
        inpDollar.setText(String.valueOf(dollar)); // 显示在编辑框中
        inpEuro.setText(String.valueOf(euro));
        inpWon.setText(String.valueOf(won));
        Log.i(TAG, "onCreate: dollar=" + dollar);
        Log.i(TAG, "onCreate: euro=" + euro);
        Log.i(TAG, "onCreate: won=" + won);}

    public void save(View btn) {
        String dollarStr = inpDollar.getText().toString();
        String euroStr = inpEuro.getText().toString();
        String wonStr = inpWon.getText().toString();
        Log.i(TAG, "save: dollarStr=" + dollarStr);
        Log.i(TAG, "save: euroStr=" + euroStr);
        Log.i(TAG, "save: wonStr=" + wonStr);
        try {  float dollar = Float.parseFloat(dollarStr);
            float euro = Float.parseFloat(euroStr);
            float won = Float.parseFloat(wonStr);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ret_dollar", dollar);
            resultIntent.putExtra("ret_euro", euro);
            resultIntent.putExtra("ret_won", won);
            setResult(RESULT_OK, resultIntent); // 返回成功结果
            finish();
        } catch (NumberFormatException e) {
            Log.e(TAG, "save 出错", e);}}

    @Override
    public void run(){
    }
}
