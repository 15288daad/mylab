package com.example.mylab;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;

public class Scoring extends AppCompatActivity {
    private static final String TAG = "Scoring";
    private TextView scoreTextViewTeamA;
    private TextView scoreTextViewTeamB;
    private int currentScoreTeamA = 0;
    private int currentScoreTeamB = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);
        scoreTextViewTeamA = findViewById(R.id.s1);
        scoreTextViewTeamB = findViewById(R.id.s2);
        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this::onResetButtonClick);
        updateScores();
    }
    public void onScoreButtonClick(View btn) {
        try {
            int id = btn.getId();
            if (id == R.id.btn1) {  // Team A +3
                currentScoreTeamA += 3;
            } else if (id == R.id.btn2) {  // Team A +2
                currentScoreTeamA += 2;
            }  else if (id == R.id.btn5) {  // Team B +3
                currentScoreTeamB += 3;
            } else if (id == R.id.btn6) {  // Team B +2
                currentScoreTeamB += 2;
            }
            updateScores();
        } catch (Exception e) {
            Log.e(TAG, "分数更新错误", e);
            Toast.makeText(this, "操作失败", Toast.LENGTH_SHORT).show();
        }
    }
    public void onResetButtonClick(View view) {
        currentScoreTeamA = 0;
        currentScoreTeamB = 0;
        updateScores();
        Toast.makeText(this, "分数已重置", Toast.LENGTH_SHORT).show();
    }
    private void updateScores() {
        scoreTextViewTeamA.setText(String.valueOf(currentScoreTeamA));
        scoreTextViewTeamB.setText(String.valueOf(currentScoreTeamB));
    }
  //  protected void onSaveInstanceState(@NonNull Bundle outState){
    //    super.onSaveInstanceState(outState);
      //  outState.putInt("key1",currentScoreTeamA);
        //outState.putInt("key1",currentScoreTeamB);
//    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        assert savedInstanceState != null;
        super.onRestoreInstanceState(savedInstanceState);
        currentScoreTeamA= savedInstanceState.getInt("key1");
        currentScoreTeamB= savedInstanceState.getInt("key2");
        scoreTextViewTeamA.setText(String.valueOf(currentScoreTeamA));
        scoreTextViewTeamB.setText(String.valueOf(currentScoreTeamB));

    }
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("key1",currentScoreTeamA);
        outState.putInt("key1",currentScoreTeamB);
    }
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentScoreTeamA=savedInstanceState.getInt("key1");
        currentScoreTeamB=savedInstanceState.getInt("key2");
        scoreTextViewTeamA.setText(String.valueOf(currentScoreTeamA));
        scoreTextViewTeamB.setText(String.valueOf(currentScoreTeamB));


    }
}
