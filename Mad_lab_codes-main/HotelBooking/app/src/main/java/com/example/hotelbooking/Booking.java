package com.example.hotelbooking;

import java.io.Serializable;

public class Booking implements Serializable {
    private int id;
    private String name;
    private int age;
    private String email;
    private String roomType;
    private String branch;
    private String amenities;
    private String checkInDate;
    private String checkInTime;
    private String slot;

    public Booking() {}

    public Booking(int id, String name, int age, String email, String roomType, String branch, String amenities, String checkInDate, String checkInTime, String slot) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.roomType = roomType;
        this.branch = branch;
        this.amenities = amenities;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.slot = slot;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }
}
