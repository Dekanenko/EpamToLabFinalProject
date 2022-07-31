package com.Dekanenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;

public class Car implements Serializable {

    private int id;
    private String brand;
    private String qualityClass;
    private String name;
    private double cost;
    boolean used;
    boolean damaged;

    public Car() {
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getQualityClass() {
        return qualityClass;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setQualityClass(String qualityClass) {
        this.qualityClass = qualityClass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return getId() == car.getId() && Double.compare(car.getCost(), getCost()) == 0 && isUsed() == car.isUsed()
                && isDamaged() == car.isDamaged() && getBrand().equals(car.getBrand())
                && getQualityClass().equals(car.getQualityClass()) && getName().equals(car.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBrand(), getQualityClass(), getName(), getCost(), isUsed(), isDamaged());
    }
}
