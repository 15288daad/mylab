package com.example.mylab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewPager viewPager = findViewById(R.id.viewpager);
        List<View> pages = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();

        // 示例：添加3个页面
        for (int i = 1; i <= 3; i++) {
            View page = inflater.inflate(android.R.layout.simple_list_item_1, null);
            TextView tv = page.findViewById(android.R.id.text1);
            tv.setText("新闻页面 " + i);
            pages.add(page);
        }

        viewPager.setAdapter(new SimplePagerAdapter(pages));
    }

    // 简单PagerAdapter
    static class SimplePagerAdapter extends PagerAdapter {
        private final List<View> views;

        SimplePagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}