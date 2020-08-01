package com.xbribe.ui.main.drawers.checkcase;

public class CheckcaseModel
{

    private String crimeimage;
    private String ministry;
    private String department;
    private String name_organization;
    private String officialName;
    private String case_desc;
    private String location;
    private String imagesno;
    private String audiono;
    private String videono;

    public CheckcaseModel(String crimeimage, String ministry, String department, String name_organization,String officialName, String case_desc, String location, String imagesno, String audiono, String videono) {
        this.crimeimage = crimeimage;
        this.ministry = ministry;
        this.department = department;
        this.name_organization = name_organization;
        this.officialName=officialName;
        this.case_desc = case_desc;
        this.location = location;
        this.imagesno = imagesno;
        this.audiono = audiono;
        this.videono = videono;
    }
    public String getCrimeimage() {
        return crimeimage;
    }

    public void setCrimeimage(String crimeimage) {
        this.crimeimage = crimeimage;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName_organization() {
        return name_organization;
    }

    public void setName_organization(String name_organization) {
        this.name_organization = name_organization;
    }

    public String getCase_desc() {
        return case_desc;
    }

    public void setCase_desc(String case_desc) {
        this.case_desc = case_desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImagesno() {
        return imagesno;
    }

    public void setImagesno(String imagesno) {
        this.imagesno = imagesno;
    }

    public String getAudiono() {
        return audiono;
    }

    public void setAudiono(String audiono) {
        this.audiono = audiono;
    }

    public String getVideono() {
        return videono;
    }

    public void setVideono(String videono) {
        this.videono = videono;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }
}