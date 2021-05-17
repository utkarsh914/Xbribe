package com.xbribe.ui.main.drawers.nearby;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.NearbyCaseResponse;
import com.xbribe.ui.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyViewModel extends AndroidViewModel {

    private AppDataManager appDataManager;
    private MutableLiveData<List<NearbyCaseResponse>> nearbyCaseResponse;

    public NearbyViewModel(@NonNull Application application) {
        super(application);
        appDataManager = ((MyApplication) application).getDataManager();
        nearbyCaseResponse = new MutableLiveData<>();
    }

    public MutableLiveData<List<NearbyCaseResponse>> getNearbyCaseResponse()
    {
        return nearbyCaseResponse;
    }

    public void getNearbyCases(String token, Double latitude, Double longitude, Integer radius)
    {
        appDataManager.getNearbyCases(token,latitude,longitude,radius).enqueue(new Callback<List<NearbyCaseResponse>>() {
            @Override
            public void onResponse(Call<List<NearbyCaseResponse>> call, Response<List<NearbyCaseResponse>> response) {
                if(response.code()<300)
                {
                    nearbyCaseResponse.postValue(response.body());
                }
                else if(response.code()>=400)
                {
                    nearbyCaseResponse.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<NearbyCaseResponse>> call, Throwable t)
            {
                nearbyCaseResponse.postValue(null);
            }
        });
    }
}
