package com.example.megacombined;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DashboardAdapter extends BaseAdapter {
    private Context context;
    private List<DashboardItem> items;

    public DashboardAdapter(Context context, List<DashboardItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.itemIcon);
        TextView name = convertView.findViewById(R.id.itemName);

        DashboardItem item = items.get(position);
        name.setText(item.getName());
        icon.setImageResource(item.getIcon());

        return convertView;
    }
}
