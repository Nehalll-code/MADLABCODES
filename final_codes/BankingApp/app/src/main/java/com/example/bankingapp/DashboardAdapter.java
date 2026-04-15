package com.example.bankingapp;

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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dashboard_item, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.dashboardIcon);
        TextView label = convertView.findViewById(R.id.dashboardLabel);

        icon.setImageResource(icons[position]);
        label.setText(labels[position]);

        return convertView;
    }
}
