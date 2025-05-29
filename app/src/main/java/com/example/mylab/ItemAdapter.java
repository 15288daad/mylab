package com.example.mylab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter {
    private int res;
    public ItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RateItem> list) {
        super(context, resource, list);
        this.res=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(this.res, parent, false);
        }

        RateItem item = (RateItem) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);

        title.setText("币种:" + item.getCname());
        detail.setText("汇率:" + item.getFval());
        return itemView;
    }
}
