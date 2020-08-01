package com.xbribe.data.api;

import android.content.Context;

import com.xbribe.data.models.CaseData;
import com.xbribe.data.models.NearbyCaseResponse;
import com.xbribe.data.models.OrganizationResponse;
import com.xbribe.data.models.TokenResponse;
import com.xbribe.data.models.User;
import com.xbribe.retrofit.RetrofitProvider;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApiHelper implements ApiService {


    private static ApiHelper instance;
    private ApiService api;
    public ApiHelper(Context context) {
        api = RetrofitProvider.getInstance(context).create(ApiService.class);
    }

    public static ApiHelper getInstance(Context context){
        if(instance == null){
            synchronized (ApiHelper.class){
                if(instance == null){
                    instance = new ApiHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public Call<User> executeLogin(User user) {
        return api.executeLogin(user);
    }

    @Override
    public Call<User> executeSignup(User user) {
        return api.executeSignup(user);
    }

    @Override
    public Call<TokenResponse> getDetails(String token) {
        return api.getDetails(token);
    }

    @Override
    public Call<OrganizationResponse> getOrganizations() {
        return api.getOrganizations();
    }

    @Override
    public Call<CaseData> reportCase(String token,String orgId, String department, String officialName, String name, String place, String address, String pin, String latitude, String longitude, String description, ArrayList<String> picsArray, ArrayList<String> audiosArray, ArrayList<String> videosArray) {
        return api.reportCase(token, orgId, department, officialName, name, place, address, pin, latitude, longitude, description, picsArray, audiosArray, videosArray);
    }

    public Call<List<NearbyCaseResponse>> getNearbyCases(String token, Double latitude, Double longitude, Integer radius) {
        return api.getNearbyCases(token, latitude, longitude, radius);
    }

    @Override
    public Call<ResponseBody> sendOtp(String email) {
        return api.sendOtp(email);
    }

    @Override
    public Call<ResponseBody> verifyOtp(String email, Integer otp) {
        return api.verifyOtp(email, otp);
    }
}
