package com.example.car_main;

public class Booking {
    private String partName;
    private String repairDate;

    public Booking(String partName, String repairDate) {
        this.partName = partName;
        this.repairDate = repairDate;
    }

    public String getPartName() {
        return partName;
    }

    public String getRepairDate() {
        return repairDate;
    }
}
