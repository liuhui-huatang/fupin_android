package com.huatang.fupin.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**

 *
 *
 *
 * **/

public class NewPoor implements Serializable {
    private static final long serialVersionUID = -3531350798739171329L;
    private String  id;
    private String  fname;
    private String  sex;
    private String  age;
    private String  fcard;
    private String  fphone;
    private String  city;
    private String  village;
    private String  town;
    private String  village_name;
    private String  town_name;
    private String  home_number;
    private String  longitude;
    private String  dimension;
    private String  password;
    private String  war_zone;
    private String  bank_name;
    private String  bank_car;
    private String  is_trong_family;
    private String  village_group;
    private String  is_mine;
    private String  qr_code;
    private String  create_time;
    private String  update_time;
    private String  token;
    private boolean ishave;
    private String  photo;

    public boolean isIshave() {
        return ishave;
    }

    public void setIshave(boolean ishave) {
        this.ishave = ishave;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFcard() {
        return fcard;
    }

    public void setFcard(String fcard) {
        this.fcard = fcard;
    }

    public String getFphone() {
        return fphone;
    }

    public void setFphone(String fphone) {
        this.fphone = fphone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getHome_number() {
        return home_number;
    }

    public void setHome_number(String home_number) {
        this.home_number = home_number;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWar_zone() {
        return war_zone;
    }

    public void setWar_zone(String war_zone) {
        this.war_zone = war_zone;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_car() {
        return bank_car;
    }

    public void setBank_car(String bank_car) {
        this.bank_car = bank_car;
    }

    public String getIs_trong_family() {
        return is_trong_family;
    }

    public void setIs_trong_family(String is_trong_family) {
        this.is_trong_family = is_trong_family;
    }

    public String getVillage_group() {
        return village_group;
    }

    public void setVillage_group(String village_group) {
        this.village_group = village_group;
    }

    public String getIs_mine() {
        return is_mine;
    }

    public void setIs_mine(String is_mine) {
        this.is_mine = is_mine;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
