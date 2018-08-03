package com.huatang.fupin.bean;

import java.io.Serializable;

/**
 * Created by forever on 2017/11/23.
 */

public class Chat implements Serializable {


    /**
     * id : 1
     * title : 扶贫问题讨论
     * text :
     * create_id : 1
     * create_name : 张三
     * num : 5
     * time : 20171122
     * photo :
     * phone : 18871584841
     */

    private String id;
    private String title;
    private String text;
    private String create_id;
    private String create_name;
    private String num;
    private String time;
    private String photo;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreate_id() {
        return create_id;
    }

    public void setCreate_id(String create_id) {
        this.create_id = create_id;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
