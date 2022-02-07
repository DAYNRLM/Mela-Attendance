package com.example.melaAttendance.model;

import java.io.Serializable;

public class SelectAttedenceType implements Serializable {
    String name;
    boolean inAttedence,outAttedence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInAttedence() {
        return inAttedence;
    }

    public void setInAttedence(boolean inAttedence) {
        this.inAttedence = inAttedence;
    }

    public boolean isOutAttedence() {
        return outAttedence;
    }

    public void setOutAttedence(boolean outAttedence) {
        this.outAttedence = outAttedence;
    }
}
