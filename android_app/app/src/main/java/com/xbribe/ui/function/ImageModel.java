package com.xbribe.ui.function;

import android.net.Uri;

public class ImageModel
{
    Uri image;

    public ImageModel(Uri image) {
        this.image = image;
    }

    public Uri getImage()
    {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
