package com.example.q1;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String appName;
    private String packageName;
    private Drawable icon;
    private boolean isSystemApp;
    private String version;
    private long size;

    public AppInfo(String appName, String packageName, Drawable icon, boolean isSystemApp, String version, long size) {
        this.appName = appName;
        this.packageName = packageName;
        this.icon = icon;
        this.isSystemApp = isSystemApp;
        this.version = version;
        this.size = size;
    }

    public String getAppName() { return appName; }
    public String getPackageName() { return packageName; }
    public Drawable getIcon() { return icon; }
    public boolean isSystemApp() { return isSystemApp; }
    public String getVersion() { return version; }
    public long getSize() { return size; }
}