package com.huatang.fupin.bean;

import java.io.Serializable;
import java.util.List;

public class Info implements Serializable {
    private String id;
    private String fcard;
    private String year;
    private String town;
    private String town_name;
    private String village;
    private String village_name;
    private String is_have_house;
    private String house_structure;
    private String house_area;

    private String house_security;
    private String effective_irrigation_area;
    private String woodland_area;
    private String ploughing_area;
    private String reforestation_area;
    private String fruit_area;
    private String grass_area;
    private String water_surface_area;
    //private String transport;
  //  private String vehicle;
  //  private String electric;
  //  private String breeding;
    private List<String> house_photo;
    private List<String> resource_photo;
    private List<String> transport_photo;
    private List<String> vehicle_photo;
    private List<String> electric_photo;
    private List<String> breeding_photo;
    private String create_time;
    private String update_time;
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

    public String getIs_have_house() {
        return is_have_house;
    }

    public void setIs_have_house(String is_have_house) {
        this.is_have_house = is_have_house;
    }

    public String getHouse_structure() {
        return house_structure;
    }

    public void setHouse_structure(String house_structure) {
        this.house_structure = house_structure;
    }

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
    }



    public String getHouse_security() {
        return house_security;
    }

    public void setHouse_security(String house_security) {
        this.house_security = house_security;
    }

    public String getEffective_irrigation_area() {
        return effective_irrigation_area;
    }

    public void setEffective_irrigation_area(String effective_irrigation_area) {
        this.effective_irrigation_area = effective_irrigation_area;
    }

    public String getWoodland_area() {
        return woodland_area;
    }

    public void setWoodland_area(String woodland_area) {
        this.woodland_area = woodland_area;
    }

    public String getPloughing_area() {
        return ploughing_area;
    }

    public void setPloughing_area(String ploughing_area) {
        this.ploughing_area = ploughing_area;
    }

    public String getReforestation_area() {
        return reforestation_area;
    }

    public void setReforestation_area(String reforestation_area) {
        this.reforestation_area = reforestation_area;
    }

    public String getFruit_area() {
        return fruit_area;
    }

    public void setFruit_area(String fruit_area) {
        this.fruit_area = fruit_area;
    }

    public String getGrass_area() {
        return grass_area;
    }

    public void setGrass_area(String grass_area) {
        this.grass_area = grass_area;
    }

    public String getWater_surface_area() {
        return water_surface_area;
    }

    public void setWater_surface_area(String water_surface_area) {
        this.water_surface_area = water_surface_area;
    }

    public List<String> getHouse_photo() {
        return house_photo;
    }

    public void setHouse_photo(List<String> house_photo) {
        this.house_photo = house_photo;
    }

    public List<String> getResource_photo() {
        return resource_photo;
    }

    public void setResource_photo(List<String> resource_photo) {
        this.resource_photo = resource_photo;
    }




    public List<String> getTransport_photo() {
        return transport_photo;
    }

    public void setTransport_photo(List<String> transport_photo) {
        this.transport_photo = transport_photo;
    }

    public List<String> getVehicle_photo() {
        return vehicle_photo;
    }

    public void setVehicle_photo(List<String> vehicle_photo) {
        this.vehicle_photo = vehicle_photo;
    }

    public List<String> getElectric_photo() {
        return electric_photo;
    }

    public void setElectric_photo(List<String> electric_photo) {
        this.electric_photo = electric_photo;
    }



    public List<String> getBreeding_photo() {
        return breeding_photo;
    }

    public void setBreeding_photo(List<String> breeding_photo) {
        this.breeding_photo = breeding_photo;
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
