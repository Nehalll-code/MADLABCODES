package com.example.q1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private Context context;
    private List<AppInfo> appList;
    private OnAppLongClickListener onAppLongClickListener;

    public interface OnAppLongClickListener {
        void onAppLongClick(AppInfo appInfo);
    }

    public AppAdapter(Context context, List<AppInfo> appList, OnAppLongClickListener onAppLongClickListener) {
        this.context = context;
        this.appList = appList;
        this.onAppLongClickListener = onAppLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo appInfo = appList.get(position);
        holder.appName.setText(appInfo.getAppName());
        holder.appPackage.setText(appInfo.getPackageName());
        holder.appIcon.setImageDrawable(appInfo.getIcon());

        holder.itemView.setOnLongClickListener(v -> {
            if (onAppLongClickListener != null) {
                onAppLongClickListener.onAppLongClick(appInfo);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName, appPackage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            appPackage = itemView.findViewById(R.id.packageName);
        }
    }
}