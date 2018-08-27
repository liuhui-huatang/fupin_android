package com.huatang.fupin.bean;

import java.io.Serializable;
import java.util.List;

public class Archive implements Serializable {

    private NewPoor poor;
    private NewBasic basic;
    private Info info;
    private NewRevenue reve;
    private List<Policy> policy;
    private List<Fund> funds;
    private List<NewFamily> family;

    public NewPoor getPoor() {
        return poor;
    }

    public void setPoor(NewPoor poor) {
        this.poor = poor;
    }

    public NewBasic getBasic() {
        return basic;
    }

    public void setBasic(NewBasic basic) {
        this.basic = basic;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public NewRevenue getReve() {
        return reve;
    }

    public void setReve(NewRevenue reve) {
        this.reve = reve;
    }

    public List<Policy> getPolicy() {
        return policy;
    }

    public void setPolicy(List<Policy> policy) {
        this.policy = policy;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public List<NewFamily> getFamily() {
        return family;
    }

    public void setFamily(List<NewFamily> family) {
        this.family = family;
    }
}
