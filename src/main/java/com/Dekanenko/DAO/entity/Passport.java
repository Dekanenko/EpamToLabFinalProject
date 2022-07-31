package com.Dekanenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;

public class Passport implements Serializable {
    private int id;
    private String name;
    private String surname;
    private String uniqueNum;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUniqueNum() {
        return uniqueNum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUniqueNum(String uniqueNum) {
        this.uniqueNum = uniqueNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passport)) return false;
        Passport passport = (Passport) o;
        return getId() == passport.getId() && getName().equals(passport.getName())
                && getSurname().equals(passport.getSurname()) && getUniqueNum().equals(passport.getUniqueNum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getUniqueNum());
    }
}
