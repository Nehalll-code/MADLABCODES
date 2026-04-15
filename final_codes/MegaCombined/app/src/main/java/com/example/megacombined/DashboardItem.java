package com.example.megacombined;

public class DashboardItem {
    private String name;
    private int icon;

    public DashboardItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() { return name; }
    public int getIcon() { return icon; }
}
