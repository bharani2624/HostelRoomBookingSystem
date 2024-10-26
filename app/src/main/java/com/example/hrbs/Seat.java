package com.example.hrbs;

public class Seat {
    private String seatNumber;
    private boolean isAvailable;
    private boolean isSelected;
    private String UserEmail;

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public String getUserEmail() {
        return UserEmail;
    }

    public int getCount() {
        return count;
    }

    public void setUserEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }


    // Default no-argument constructor (required by Firebase)
    public Seat() {
    }

    public Seat(String seatNumber,boolean isAvailable,boolean isSelected,String UserEmail,int count)
    {
        this.seatNumber=seatNumber;
        this.isAvailable=isAvailable;
        this.isSelected=isSelected;
        this.UserEmail=UserEmail;
        this.count=count;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
