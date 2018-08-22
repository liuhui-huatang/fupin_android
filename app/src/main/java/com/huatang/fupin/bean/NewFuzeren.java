package com.huatang.fupin.bean;


import java.io.Serializable;

public class NewFuzeren implements Serializable {

    private static final long serialVersionUID = 7117023882867427106L;
    public String      id;
    public String      village;
    public String      town;
    public String      village_name;
    public String      town_name;
    public String      village_chief;
    public String      chief_duty;
    public String      chief_phone;
    public String      password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getTown_name() {
        return town_name;
    }

    public void setTown_name(String town_name) {
        this.town_name = town_name;
    }

    public String getVillage_chief() {
        return village_chief;
    }

    public void setVillage_chief(String village_chief) {
        this.village_chief = village_chief;
    }

    public String getChief_duty() {
        return chief_duty;
    }

    public void setChief_duty(String chief_duty) {
        this.chief_duty = chief_duty;
    }

    public String getChief_phone() {
        return chief_phone;
    }

    public void setChief_phone(String chief_phone) {
        this.chief_phone = chief_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
