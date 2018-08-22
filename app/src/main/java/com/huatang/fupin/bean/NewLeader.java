package com.huatang.fupin.bean;

import java.io.Serializable;

public class NewLeader implements Serializable {
    private static final long serialVersionUID = 6918184824955089983L;
    public String id;
    public String leader_name ;
    public String sex;
    public String leader_card;
    public String leader_unit ;
    public String leader_unit_id ;
    public String leader_duty ;
    public String leader_phone;
    public String leader_andscape ;
    public String unit_level ;
    public String help_town_id ;
    public String help_town ;
    public String leader_photo ;
    public String leader_assess ;
    public String password ;
    public String kandy_user  ;
    public String kandy_pwd ;
    public String year     ;
    public String kandy_login  ;
    public String create_time  ;
    public String update_time  ;
    public int hasbasic;

    public boolean isHasbasic() {
        return hasbasic >0 ? true : false;
    }

    public void setHasbasic(boolean hasbasic) {
        this.hasbasic = hasbasic ? 0: 1;
    }

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

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLeader_card() {
        return leader_card;
    }

    public void setLeader_card(String leader_card) {
        this.leader_card = leader_card;
    }

    public String getLeader_unit() {
        return leader_unit;
    }

    public void setLeader_unit(String leader_unit) {
        this.leader_unit = leader_unit;
    }

    public String getLeader_unit_id() {
        return leader_unit_id;
    }

    public void setLeader_unit_id(String leader_unit_id) {
        this.leader_unit_id = leader_unit_id;
    }

    public String getLeader_duty() {
        return leader_duty;
    }

    public void setLeader_duty(String leader_duty) {
        this.leader_duty = leader_duty;
    }

    public String getLeader_phone() {
        return leader_phone;
    }

    public void setLeader_phone(String leader_phone) {
        this.leader_phone = leader_phone;
    }

    public String getLeader_andscape() {
        return leader_andscape;
    }

    public void setLeader_andscape(String leader_andscape) {
        this.leader_andscape = leader_andscape;
    }

    public String getUnit_level() {
        return unit_level;
    }

    public void setUnit_level(String unit_level) {
        this.unit_level = unit_level;
    }

    public String getHelp_town_id() {
        return help_town_id;
    }

    public void setHelp_town_id(String help_town_id) {
        this.help_town_id = help_town_id;
    }

    public String getHelp_town() {
        return help_town;
    }

    public void setHelp_town(String help_town) {
        this.help_town = help_town;
    }

    public String getLeader_photo() {
        return leader_photo;
    }

    public void setLeader_photo(String leader_photo) {
        this.leader_photo = leader_photo;
    }

    public String getLeader_assess() {
        return leader_assess;
    }

    public void setLeader_assess(String leader_assess) {
        this.leader_assess = leader_assess;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKandy_user() {
        return kandy_user;
    }

    public void setKandy_user(String kandy_user) {
        this.kandy_user = kandy_user;
    }

    public String getKandy_pwd() {
        return kandy_pwd;
    }

    public void setKandy_pwd(String kandy_pwd) {
        this.kandy_pwd = kandy_pwd;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getKandy_login() {
        return kandy_login;
    }

    public void setKandy_login(String kandy_login) {
        this.kandy_login = kandy_login;
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
