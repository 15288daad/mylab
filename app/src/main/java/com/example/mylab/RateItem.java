package com.example.mylab;

public class RateItem {
    private int id;
    private String cname;
    private float fval;

    public RateItem() {
        super();
        cname = "";
        fval = 0.0f;
    }
    public RateItem(String cname, float fval) {
        super();
        this.cname = cname;
        this.fval = fval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public float getFval() {
        return fval;
    }

    public void setFval(float fval) {
        this.fval = fval;
    }
}