package com.xbribe.ui.main.drawers.aboutus;

public class AboutUsModel
{
    int  image;
    String member_name;
    String member_work;

    public AboutUsModel(int image, String member_name, String member_work)
    {
        this.image = image;
        this.member_name = member_name;
        this.member_work = member_work;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_work() {
        return member_work;
    }

    public void setMember_work(String member_work) {
        this.member_work = member_work;
    }
}
