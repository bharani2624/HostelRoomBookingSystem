package com.example.hrbs;

public class Seat {
    private final String seatNumber;
    private final boolean isAvailable;
    private boolean isSelected;

    public Seat(String seatNumber, boolean isAvailable, boolean isSelected)
     {
         this.seatNumber=seatNumber;
         this.isAvailable=isAvailable;
         this.isSelected=isSelected;
     }
    public String getSeatNumber() {
        return seatNumber;
    }
    public boolean isAvailable()
    {
        return isAvailable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
