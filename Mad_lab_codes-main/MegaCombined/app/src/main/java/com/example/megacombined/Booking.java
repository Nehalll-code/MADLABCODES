package com.example.megacombined;

public class Booking {
    private int id;
    private String name;
    private String date;
    private String mainInfo;
    private String extraInfo;
    private String type; // VEHICLE, COURSE, PARKING

    public Booking(int id, String name, String date, String mainInfo, String extraInfo, String type) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.mainInfo = mainInfo;
        this.extraInfo = extraInfo;
        this.type = type;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getMainInfo() { return mainInfo; }
    public String getExtraInfo() { return extraInfo; }
    public String getType() { return type; }
}
