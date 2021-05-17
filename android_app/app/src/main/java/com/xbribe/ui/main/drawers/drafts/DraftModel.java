package com.xbribe.ui.main.drawers.drafts;

public class DraftModel {
    String ministry;
    String address;
    String pincode;
    String city;
    String deparment;
    String organisation_name;
    String officialName;
    String description;
    String delete;
    String id;
    String ministryID;

    public DraftModel(String ministry, String address, String pincode, String city, String deparment, String organisation_name, String officialName, String description, String delete, String id, String ministryID) {
        this.ministry = ministry;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.deparment = deparment;
        this.organisation_name = organisation_name;
        this.officialName = officialName;
        this.description = description;
        this.delete = delete;
        this.id = id;
        this.ministryID=ministryID;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeparment() {
        return deparment;
    }

    public void setDeparment(String deparment) {
        this.deparment = deparment;
    }

    public String getOrganisation_name() {
        return organisation_name;
    }

    public void setOrganisation_name(String organisation_name) {
        this.organisation_name = organisation_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }
}