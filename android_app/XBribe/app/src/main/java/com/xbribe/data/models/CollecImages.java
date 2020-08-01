package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

public class CollecImages
{

    @SerializedName("id")
    String id;

    @SerializedName("image")
    String imageUrl;

    public CollecImages(String id, String imageUrl)
    {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
