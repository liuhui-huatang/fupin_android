package com.huatang.fupin.bean;

import java.io.Serializable;

/**
 * Created by forever on 2017/11/23.
 */

public class Leaders implements Serializable{
    String id;
    String leader_name;
    String leader_phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeader_phone() {
        return leader_phone;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public void setLeader_phone(String leader_phone) {
        this.leader_phone = leader_phone;
    }
}
