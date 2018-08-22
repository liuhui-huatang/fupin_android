package com.huatang.fupin.bean;

import java.io.Serializable;

public class Policy implements Serializable {
    private String  id             ;
    private String poor_card     ;
    private String measure_big_id    ;
    private String measure_mid_id ;
    private String measure_sml_id ;
    private String enjoy_policy_time ;
    private String town              ;
    private String village           ;
    private String town_name         ;
    private String village_name      ;
    private String year              ;
    private String create_time       ;
    private String update_time       ;
    private boolean ishave;

    public boolean isIshave() {
        return ishave;
    }

    public void setIshave(boolean ishave) {
        this.ishave = ishave;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoor_card() {
        return poor_card;
    }

    public void setPoor_card(String poor_card) {
        this.poor_card = poor_card;
    }

    public String getMeasure_big_id() {
        return measure_big_id;
    }

    public void setMeasure_big_id(String measure_big_id) {
        this.measure_big_id = measure_big_id;
    }

    public String getMeasure_mid_id() {
        return measure_mid_id;
    }

    public void setMeasure_mid_id(String measure_mid_id) {
        this.measure_mid_id = measure_mid_id;
    }

    public String getMeasure_sml_id() {
        return measure_sml_id;
    }

    public void setMeasure_sml_id(String measure_sml_id) {
        this.measure_sml_id = measure_sml_id;
    }

    public String getEnjoy_policy_time() {
        return enjoy_policy_time;
    }

    public void setEnjoy_policy_time(String enjoy_policy_time) {
        this.enjoy_policy_time = enjoy_policy_time;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
}
