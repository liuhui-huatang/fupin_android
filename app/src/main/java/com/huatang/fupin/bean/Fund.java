package com.huatang.fupin.bean;

import java.io.Serializable;

public class Fund implements Serializable {
    private String    id           ;
    private String    year         ;
    private String    fcard        ;
    private String    town         ;
    private String    village      ;
    private String    town_name    ;
    private String    village_name ;
    private String    big_cateid   ;
    private String    second_cateid;
    private String    smail_cateid ;
    private String    money        ;
    private String    fund_time    ;
    private String    create_time  ;
    private String    update_time  ;
    private boolean ishave;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFcard() {
        return fcard;
    }

    public void setFcard(String fcard) {
        this.fcard = fcard;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTown_name() {
        return town_name;
    }

    public void setTown_name(String town_name) {
        this.town_name = town_name;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getBig_cateid() {
        return big_cateid;
    }

    public void setBig_cateid(String big_cateid) {
        this.big_cateid = big_cateid;
    }

    public String getSecond_cateid() {
        return second_cateid;
    }

    public void setSecond_cateid(String second_cateid) {
        this.second_cateid = second_cateid;
    }

    public String getSmail_cateid() {
        return smail_cateid;
    }

    public void setSmail_cateid(String smail_cateid) {
        this.smail_cateid = smail_cateid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getFund_time() {
        return fund_time;
    }

    public void setFund_time(String fund_time) {
        this.fund_time = fund_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public boolean isIshave() {
        return ishave;
    }

    public void setIshave(boolean ishave) {
        this.ishave = ishave;
    }
}
