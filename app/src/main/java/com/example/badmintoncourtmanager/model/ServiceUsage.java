package com.example.badmintoncourtmanager.model;

public class ServiceUsage {
    private int id;
    private int bookingId;
    private int serviceId;
    private String serviceName;
    private int quantity;
    private double price;
    private double totalPrice;

    public ServiceUsage() {
    }

    public ServiceUsage(int id, int bookingId, int serviceId, String serviceName, int quantity, double price,
            double totalPrice) {
        this.id = id;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public ServiceUsage(int bookingId, int serviceId, String serviceName, int quantity, double price,
            double totalPrice) {
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
