package com.example.mylab;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_recycler_veiw);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ItemData[] itemData = new ItemData[] {
                new ItemData("Email", android.R.drawable.ic_dialog_email),
                new ItemData("Info", android.R.drawable.ic_dialog_info),
                new ItemData("Delete", android.R.drawable.ic_delete),
                new ItemData("Dialer", android.R.drawable.ic_dialog_dialer),
                new ItemData("Alert", android.R.drawable.ic_dialog_alert),
                new ItemData("Map", android.R.drawable.ic_dialog_map),
                new ItemData("Email", android.R.drawable.ic_dialog_email),
                new ItemData("Info", android.R.drawable.ic_dialog_info),
                new ItemData("Delete", android.R.drawable.ic_delete),
                new ItemData("Dialer", android.R.drawable.ic_dialog_dialer),
                new ItemData("Alert", android.R.drawable.ic_dialog_alert),
                new ItemData("Map", android.R.drawable.ic_dialog_map),
        };

        Log.i("a", "begin");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Log.i("a", "begin");
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(itemData);
        Log.i("a", "begin");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.i("a", "begin");
        recyclerView.setAdapter(adapter);

        // recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        // recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }
}