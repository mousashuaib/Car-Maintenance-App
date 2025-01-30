package com.example.car_main;
public class Car {
    private String id;
    private String carName;
    private String carModel;
    private String licensePlate;
    private String carImage;

    public Car(String id, String carName, String carModel, String licensePlate, String carImage) {
        this.id = id;
        this.carName = carName;
        this.carModel = carModel;
        this.licensePlate = licensePlate;
        this.carImage = carImage;
    }

    // Getter for ID
    public String getId() {
        return id;
    }

    // Other getters
    public String getCarName() {
        return carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public void setId(String id) {
        this.id = id;
    }
}
