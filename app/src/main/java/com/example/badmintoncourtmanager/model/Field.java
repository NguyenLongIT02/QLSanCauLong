package com.example.badmintoncourtmanager.model;

public class Field {
    private int id;
    private String name;
    private String type; // Đơn, Đôi, VIP
    private double pricePerHour;
    private String status; // Hoạt động, Bảo trì
    private String description;

    public Field() {
    }

    public Field(int id, String name, String type, double pricePerHour, String status, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.description = description;
    }

    public Field(String name, String type, double pricePerHour, String status, String description) {
        this.name = name;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
