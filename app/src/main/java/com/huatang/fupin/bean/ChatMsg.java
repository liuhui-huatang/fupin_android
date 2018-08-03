package com.huatang.fupin.bean;

/**
 * Created by forever on 2017/11/23.
 */

public class ChatMsg {


    /**
     * id : 1
     * chat_id : 1
     * text : 讨论扶贫问题
     * time : 123565476786
     * name : 张三
     * photo :
     * leader_id : null
     * phone : 18871584841
     */

    private String id;
    private String chat_id;
    private String text;
    private String time;
    private String name;
    private String photo;
    private Object leader_id;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Object getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(Object leader_id) {
        this.leader_id = leader_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
