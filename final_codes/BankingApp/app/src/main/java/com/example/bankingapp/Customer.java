package com.example.bankingapp;

import java.io.Serializable;

public class Customer implements Serializable {
    private int id;
    private String name;
    private int age;
    private String email;
    private String accountType;
    private boolean isKycVerified;
    private String branch;
    private String services;
    private String openingDate;
    private String openingTime;
    private String accountMode;

    public Customer() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public boolean isKycVerified() { return isKycVerified; }
    public void setKycVerified(boolean kycVerified) { isKycVerified = kycVerified; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getServices() { return services; }
    public void setServices(String services) { this.services = services; }

    public String getOpeningDate() { return openingDate; }
    public void setOpeningDate(String openingDate) { this.openingDate = openingDate; }

    public String getOpeningTime() { return openingTime; }
    public void setOpeningTime(String openingTime) { this.openingTime = openingTime; }

    public String getAccountMode() { return accountMode; }
    public void setAccountMode(String accountMode) { this.accountMode = accountMode; }
}
