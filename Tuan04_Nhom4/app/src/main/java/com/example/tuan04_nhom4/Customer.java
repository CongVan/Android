package com.example.tuan04_nhom4;

public class Customer {
    private String fullName;
    private String phoneNumber;

    public  Customer(String fullName,String phoneNumber){
        this.fullName=fullName;
        this.phoneNumber=phoneNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
