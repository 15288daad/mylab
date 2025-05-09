package com.example.mylab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailRateShow extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="DetailRateShow";
    private float exchangeRate;
    private String nation;
    private Button exchangeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_rate_show);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        TextView title = findViewById(R.id.textView2);

        // 检查Intent是否包含所需数据
        if (intent != null && intent.hasExtra("Nation")) {
            // 获取传递的数据
            String nation = intent.getStringExtra("Nation");
            String number = intent.getStringExtra("Rate"); // 0是默认值

            // 显示接收到的数据

            this.exchangeRate = Float.parseFloat(number);
            this.nation = nation;

            title.setText(nation);
        }

        EditText editText = findViewById(R.id.input);

        exchangeButton = findViewById(R.id.button3);
        exchangeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText amountInput = findViewById(R.id.input);
        TextView resultText = findViewById(R.id.textView2);

        String inputStr = amountInput.getText().toString().trim();
        if (inputStr.isEmpty()) {
            resultText.setText("请输入金额");
            return;
        }

        try {
            double amount = Double.parseDouble(inputStr);
            if (amount <= 0) {
                resultText.setText("金额必须大于0");
                return;
            }

            double result = amount * exchangeRate;
            String nation = this.nation;

            String resultStr = String.format(
                    "%.2f 人民币 = %.2f %s",
                    amount,
                    result,
                    nation
            );
            resultText.setText(resultStr);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        } catch (NumberFormatException e) {
            resultText.setText("请输入有效数字");
        }
    }
}