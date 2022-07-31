package com.Dekanenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable {
    private int id;
    private int userId;
    private String userLogin;
    private int passportId;
    private int carId;
    private boolean driverOption;
    private int statusId;
    private String firstDate;
    private String lastDate;
    private String returnDate;
    private double cost;
    private String message;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public int getPassportId() {
        return passportId;
    }

    public int getCarId() {
        return carId;
    }

    public boolean getDriverOption() {
        return driverOption;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public double getCost() {
        return cost;
    }

    public String getMessage() {
        return message;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setPassportId(int passportId) {
        this.passportId = passportId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setDriverOption(boolean driverOption) {
        this.driverOption = driverOption;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId() && getUserId() == order.getUserId() && getPassportId() == order.getPassportId()
                && getCarId() == order.getCarId() && getDriverOption() == order.getDriverOption()
                && getStatusId() == order.getStatusId() && Double.compare(order.getCost(), getCost()) == 0
                && getUserLogin().equals(order.getUserLogin()) && getFirstDate().equals(order.getFirstDate())
                && getLastDate().equals(order.getLastDate()) && getReturnDate().equals(order.getReturnDate())
                && getMessage().equals(order.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getUserLogin(), getPassportId(), getCarId(), getDriverOption(), getStatusId(), getFirstDate(), getLastDate(), getReturnDate(), getCost(), getMessage());
    }
}
