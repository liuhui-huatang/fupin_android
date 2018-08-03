package com.huatang.fupin.bean;

import java.io.Serializable;

/**
 * Created by forever on 2017/11/23.
 */

public class Basics implements Serializable{
    String id;
    String fname;
    String fphone;

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

    public String getFphone() {
        return fphone;
    }

    public void setFphone(String fphone) {
        this.fphone = fphone;
    }
}
