package com.example.hrbs;

public class Seat {
    private String seatNumber;
    private boolean isAvailable;
    private boolean isSelected;
    private String UserEmail1;
    private String UserEmail2;

    public String getUserEmail3() {
        return UserEmail3;
    }

    public void setUserEmail3(String userEmail3) {
        UserEmail3 = userEmail3;
    }

    public String getUserEmail4() {
        return UserEmail4;
    }

    public void setUserEmail4(String userEmail4) {
        UserEmail4 = userEmail4;
    }

    public String getUserEmail2() {
        return UserEmail2;
    }

    public void setUserEmail2(String userEmail2) {
        UserEmail2 = userEmail2;
    }

    private String UserEmail3;
    private String UserEmail4;

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public String getUserEmail1() {
        return UserEmail1;
    }

    public int getCount() {
        return count;
    }

    public void setUserEmail(String UserEmail1) {
        this.UserEmail1 = UserEmail1;
    }


    // Default no-argument constructor (required by Firebase)
    public Seat() {
    }

    public Seat(String seatNumber,boolean isAvailable,boolean isSelected,String UserEmail1,String UserEmail2,String UserEmail3,String UserEmail4,int count)
    {
        this.seatNumber=seatNumber;
        this.isAvailable=isAvailable;
        this.isSelected=isSelected;
        this.UserEmail1=UserEmail1;
        this.UserEmail2=UserEmail2;
        this.UserEmail3=UserEmail3;
        this.UserEmail4=UserEmail4;
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
