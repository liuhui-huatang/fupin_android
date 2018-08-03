package com.huatang.fupin.bean;

/**
 * Created by Administrator on 2017/12/4.
 */

public class Area {
//    {"id":"17","area_name":"\u5317\u5927\u8425\u5b50\u6751","pid":"6","level":"2","longitude":"121.05457255886","dimension":"42.535826251351"}
    String id;
    String name;
    String area_name;
    String longitude;
    String dimension;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
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
}
