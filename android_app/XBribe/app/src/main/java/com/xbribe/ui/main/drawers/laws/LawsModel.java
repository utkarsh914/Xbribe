package com.xbribe.ui.main.drawers.laws;

public class LawsModel
{
    String section;
    String desc;

    public LawsModel(String section, String desc)
    {
        this.section = section;
        this.desc = desc;
    }

    public String getSection()
    {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
