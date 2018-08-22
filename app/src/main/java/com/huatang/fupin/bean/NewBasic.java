package com.huatang.fupin.bean;

import java.io.Serializable;
import java.util.List;

public class NewBasic implements Serializable {
    private String    id                         ;
    private String    fcard                      ;
    private String    year                       ;
    private String    out_poor_state             ;
    private String    change_time                ;
    private String    out_way                    ;
    private String    change_content             ;
    private String    is_out_policy              ;
    private String    family_state               ;
    private String    family_num                 ;
    private String    main_pcause                ;
    private String    main_pcause_two            ;
    private String    main_pcause_three          ;
    private List<String>    main_path                  ;
    private List<String>    secondary_pcause           ;
    private String    main_pcause_info           ;
    private String    secondary_pcause_info      ;
    private String    is_water_security          ;
    private String    is_water_difficulty        ;
    private String    is_life_electricity        ;
    private String    is_produced_electricity    ;
    private String    fuel_type                  ;
    private String    distance                   ;
    private String    is_poor_cooperation        ;
    private List<String>    is_member                  ;
    private String    is_relocation              ;
    private String    mine_state                 ;
    private String    placement_state            ;
    private String    settlement                 ;
    private List<String> mine_difficulty            ;
    private String    is_outschool               ;
    private String    town                       ;
    private String    town_name                  ;
    private String    village                    ;
    private String    village_name               ;
    private String    way_water_file             ;
    private String    way_water                  ;
    private String    determine_time             ;
    private String    create_time                ;
    private String    update_time                ;
    private String    pcauseName                 ;
    private boolean    ishave                    ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIs_member() {
        return is_member;
    }

    public List<String> getMine_difficulty() {
        return mine_difficulty;
    }

    public boolean isIshave() {
        return ishave;
    }

    public void setIshave(boolean ishave) {
        this.ishave = ishave;
    }

    public String getFcard() {
        return fcard;
    }

    public void setFcard(String fcard) {
        this.fcard = fcard;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOut_poor_state() {
        return out_poor_state;
    }

    public void setOut_poor_state(String out_poor_state) {
        this.out_poor_state = out_poor_state;
    }

    public String getChange_time() {
        return change_time;
    }

    public void setChange_time(String change_time) {
        this.change_time = change_time;
    }

    public String getOut_way() {
        return out_way;
    }

    public void setOut_way(String out_way) {
        this.out_way = out_way;
    }

    public String getChange_content() {
        return change_content;
    }

    public void setChange_content(String change_content) {
        this.change_content = change_content;
    }

    public String getIs_out_policy() {
        return is_out_policy;
    }

    public void setIs_out_policy(String is_out_policy) {
        this.is_out_policy = is_out_policy;
    }

    public String getFamily_state() {
        return family_state;
    }

    public void setFamily_state(String family_state) {
        this.family_state = family_state;
    }

    public String getFamily_num() {
        return family_num;
    }

    public void setFamily_num(String family_num) {
        this.family_num = family_num;
    }

    public String getMain_pcause() {
        return main_pcause;
    }

    public void setMain_pcause(String main_pcause) {
        this.main_pcause = main_pcause;
    }

    public String getMain_pcause_two() {
        return main_pcause_two;
    }

    public void setMain_pcause_two(String main_pcause_two) {
        this.main_pcause_two = main_pcause_two;
    }

    public String getMain_pcause_three() {
        return main_pcause_three;
    }

    public void setMain_pcause_three(String main_pcause_three) {
        this.main_pcause_three = main_pcause_three;
    }

    public List<String> getMain_path() {
        return main_path;
    }

    public void setMain_path(List<String> main_path) {
        this.main_path = main_path;
    }

    public List<String> getSecondary_pcause() {
        return secondary_pcause;
    }

    public void setSecondary_pcause(List<String> secondary_pcause) {
        this.secondary_pcause = secondary_pcause;
    }

    public void setIs_member(List<String> is_member) {
        this.is_member = is_member;
    }

    public void setMine_difficulty(List<String> mine_difficulty) {
        this.mine_difficulty = mine_difficulty;
    }

    public String getMain_pcause_info() {
        return main_pcause_info;
    }

    public void setMain_pcause_info(String main_pcause_info) {
        this.main_pcause_info = main_pcause_info;
    }

    public String getSecondary_pcause_info() {
        return secondary_pcause_info;
    }

    public void setSecondary_pcause_info(String secondary_pcause_info) {
        this.secondary_pcause_info = secondary_pcause_info;
    }

    public String getIs_water_security() {
        return is_water_security;
    }

    public void setIs_water_security(String is_water_security) {
        this.is_water_security = is_water_security;
    }

    public String getIs_water_difficulty() {
        return is_water_difficulty;
    }

    public void setIs_water_difficulty(String is_water_difficulty) {
        this.is_water_difficulty = is_water_difficulty;
    }

    public String getIs_life_electricity() {
        return is_life_electricity;
    }

    public void setIs_life_electricity(String is_life_electricity) {
        this.is_life_electricity = is_life_electricity;
    }

    public String getIs_produced_electricity() {
        return is_produced_electricity;
    }

    public void setIs_produced_electricity(String is_produced_electricity) {
        this.is_produced_electricity = is_produced_electricity;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIs_poor_cooperation() {
        return is_poor_cooperation;
    }

    public void setIs_poor_cooperation(String is_poor_cooperation) {
        this.is_poor_cooperation = is_poor_cooperation;
    }



    public String getIs_relocation() {
        return is_relocation;
    }

    public void setIs_relocation(String is_relocation) {
        this.is_relocation = is_relocation;
    }

    public String getMine_state() {
        return mine_state;
    }

    public void setMine_state(String mine_state) {
        this.mine_state = mine_state;
    }

    public String getPlacement_state() {
        return placement_state;
    }

    public void setPlacement_state(String placement_state) {
        this.placement_state = placement_state;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }



    public String getIs_outschool() {
        return is_outschool;
    }

    public void setIs_outschool(String is_outschool) {
        this.is_outschool = is_outschool;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown_name() {
        return town_name;
    }

    public void setTown_name(String town_name) {
        this.town_name = town_name;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getWay_water_file() {
        return way_water_file;
    }

    public void setWay_water_file(String way_water_file) {
        this.way_water_file = way_water_file;
    }

    public String getWay_water() {
        return way_water;
    }

    public void setWay_water(String way_water) {
        this.way_water = way_water;
    }

    public String getDetermine_time() {
        return determine_time;
    }

    public void setDetermine_time(String determine_time) {
        this.determine_time = determine_time;
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

    public String getPcauseName() {
        return pcauseName;
    }

    public void setPcauseName(String pcauseName) {
        this.pcauseName = pcauseName;
    }
}
