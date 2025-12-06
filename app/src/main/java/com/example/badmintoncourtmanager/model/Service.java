package com.example.badmintoncourtmanager.model;

public class Service {
    private int id;
    private String name;
    private double price;
    private String unit; // Cái, Chai, Giờ
    private int quantity;
    private String description;

    public Service() {
    }

    public Service(int id, String name, double price, String unit, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
        this.description = description;
    }

    public Service(String name, double price, String unit, int quantity, String description) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
