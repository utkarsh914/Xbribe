package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

public class Organizations {

    @SerializedName("ministryId")
    private String ID;

    @SerializedName("ministryName")
    private String ministry;

    @SerializedName("departments")
    private String[] departments;

    public Organizations(String ID, String ministry, String[] departments) {
        this.ID = ID;
        this.ministry = ministry;
        this.departments = departments;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String[] getDepartments() {
        return departments;
    }

    public void setDepartments(String[] departments) {
        this.departments = departments;
    }
}
