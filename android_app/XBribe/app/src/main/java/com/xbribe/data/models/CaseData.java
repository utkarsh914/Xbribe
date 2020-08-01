package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CaseData {
    @SerializedName("picsArray")
    private ArrayList<String> picsArray;

    @SerializedName("audiosArray")
    private ArrayList<String> audiosArray;

    @SerializedName("videosArray")
    private ArrayList<String> videosArray;

    @SerializedName("status")
    private String status;

    @SerializedName("folder")
    private String folder;

    @SerializedName("priority")
    private String priority;

    @SerializedName("messages")
    private String[] messages;

    @SerializedName("_id")
    private String _id;

    @SerializedName("caseId")
    private String caseId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("email")
    private String email;

    @SerializedName("ministryId")
    private String orgId;

    @SerializedName("department")
    private String department;

    @SerializedName("officialName")
    private String officialName;

    @SerializedName("date")
    private String date;

    @SerializedName("name")
    private String name;

    @SerializedName("place")
    private String place;

    @SerializedName("location")
    private LocationDetails location;

    @SerializedName("description")
    private String description;

    @SerializedName("__v")
    private Integer __v;


    public CaseData(ArrayList<String>  picsArray,ArrayList<String> audiosArray,ArrayList<String> videosArray, String name, String place, LocationDetails location, String description, String orgId, String department, String officialName)
    {
         this.picsArray=picsArray;
         this.audiosArray = audiosArray;
         this.videosArray = videosArray;
         this.name = name;
         this.place = place;
         this.location = location;
         this.description = description;
         this.orgId=orgId;
         this.department=department;
         this.officialName=officialName;
    }

    public ArrayList<String> getAudiosArray() {
        return audiosArray;
    }

    public void setAudiosArray(ArrayList<String> audiosArray) {
        this.audiosArray = audiosArray;
    }

    public ArrayList<String> getVideosArray() {
        return videosArray;
    }

    public void setVideosArray(ArrayList<String> videosArray) {
        this.videosArray = videosArray;
    }

    public ArrayList<String> getPicsArray() {
        return picsArray;
    }

    public void setPicsArray(ArrayList<String> picsArray) {
        this.picsArray = picsArray;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPriority() {
        return priority;
    }


    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocationDetails getLocation() {
        return location;
    }

    public void setLocation(LocationDetails location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }
}