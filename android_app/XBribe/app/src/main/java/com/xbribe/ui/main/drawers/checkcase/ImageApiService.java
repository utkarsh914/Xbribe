package com.xbribe.ui.main.drawers.checkcase;

import com.xbribe.data.models.CollecImages;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ImageApiService
{
    @GET("api/v1/images/image/")
    Call<ArrayList<CollecImages>> getImages();
}
