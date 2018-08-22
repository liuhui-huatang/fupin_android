package com.huatang.fupin.bean;

import java.io.Serializable;

public class NewChat implements Serializable {

    private String id;
    private String title;
    private String content;
    private String push_leader_id;
    private String push_leader_name;
    private String push_leader_phone;
    private String push_leader_photo;
    private String push_photo;
    private String leader_num;
    private String create_time;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPush_leader_id() {
        return push_leader_id;
    }

    public void setPush_leader_id(String push_leader_id) {
        this.push_leader_id = push_leader_id;
    }

    public String getPush_leader_name() {
        return push_leader_name;
    }

    public void setPush_leader_name(String push_leader_name) {
        this.push_leader_name = push_leader_name;
    }

    public String getPush_leader_phone() {
        return push_leader_phone;
    }

    public void setPush_leader_phone(String push_leader_phone) {
        this.push_leader_phone = push_leader_phone;
    }

    public String getPush_leader_photo() {
        return push_leader_photo;
    }

    public void setPush_leader_photo(String push_leader_photo) {
        this.push_leader_photo = push_leader_photo;
    }

    public String getPush_photo() {
        return push_photo;
    }

    public void setPush_photo(String push_photo) {
        this.push_photo = push_photo;
    }

    public String getLeader_num() {
        return leader_num;
    }

    public void setLeader_num(String leader_num) {
        this.leader_num = leader_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
