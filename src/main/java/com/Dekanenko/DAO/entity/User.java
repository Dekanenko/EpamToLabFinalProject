package com.Dekanenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private int id;
    private String login;
    private String email;
    private double cash;
    private boolean affordable;
    private int roleId;
    private int passportId;

    public User() {
        this.cash = 0;
        this.affordable = true;
        this.passportId = -1;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public double getCash() {
        return cash;
    }

    public boolean isAffordable() {
        return affordable;
    }

    public int getRoleId() {
        return roleId;
    }

    public int getPassportId() {
        return passportId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public void setAffordable(boolean affordable) {
        this.affordable = affordable;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setPassportId(int passportId) {
        this.passportId = passportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && Double.compare(user.getCash(), getCash()) == 0 && isAffordable() == user.isAffordable() && getRoleId() == user.getRoleId() && getPassportId() == user.getPassportId() && getLogin().equals(user.getLogin()) && getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getEmail(), getCash(), isAffordable(), getRoleId(), getPassportId());
    }
}
