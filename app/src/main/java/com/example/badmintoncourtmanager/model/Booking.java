package com.example.badmintoncourtmanager.model;

public class Booking {
    private int id;
    private int fieldId;
    private String fieldName;
    private String customerName;
    private String customerPhone;
    private String date;
    private String startTime;
    private String endTime;
    private double hours;
    private double totalPrice;
    private String status; // Đã đặt, Đã thanh toán, Đã hủy
    private String notes;

    public Booking() {
    }

    public Booking(int id, int fieldId, String fieldName, String customerName, String customerPhone,
            String date, String startTime, String endTime, double totalPrice, String status, String notes) {
        this.id = id;
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.notes = notes;
    }

    public Booking(int fieldId, String customerName, String customerPhone,
            String date, String startTime, String endTime, double totalPrice, String status, String notes) {
        this.fieldId = fieldId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
