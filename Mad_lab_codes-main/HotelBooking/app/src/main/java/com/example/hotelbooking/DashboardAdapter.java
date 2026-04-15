package com.example.hotelbooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardAdapter extends BaseAdapter {
    private Context context;
    private String[] labels;
    private int[] icons;

    public DashboardAdapter(Context context, String[] labels, int[] icons) {
        this.context = context;
        this.labels = labels;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return labels.length;
    }

    @Override
    public Object getItem(int position) {
        return labels[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.itemIcon);
        TextView label = convertView.findViewById(R.id.itemLabel);

        icon.setImageResource(icons[position]);
        label.setText(labels[position]);

        return convertView;
    }
}
