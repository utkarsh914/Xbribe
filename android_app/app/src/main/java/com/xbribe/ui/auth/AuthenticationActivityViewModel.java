package com.xbribe.ui.auth;

import android.app.Application;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.TokenResponse;
import com.xbribe.data.models.User;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivityViewModel extends AndroidViewModel{

    private AppDataManager appDataManager;
    private MutableLiveData<User> loginResponse;
    private MutableLiveData<User> registerResponse;
    private MutableLiveData<Boolean> tokenResponse;

    public AuthenticationActivityViewModel(@NonNull Application application) {
        super(application);

        appDataManager=((MyApplication) application).getDataManager();
        loginResponse = new MutableLiveData<>();
        registerResponse = new MutableLiveData<>();
        tokenResponse = new MutableLiveData<>();
    }

    public MutableLiveData<User> getLoginResponse()
    {
        return loginResponse;
    }
    public MutableLiveData<User> getRegisterResponse()
    {
        return registerResponse;
    }
    public MutableLiveData<Boolean> getTokenResponse() { return tokenResponse; }

    public void userLogin(User user)
    {
        appDataManager.executeLogin(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code()<300)
                {
                    loginResponse.postValue(response.body());
                    appDataManager.saveToken(response.body().getToken());
                }
                else if(response.code()==400)
                {
                    loginResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t)
            {
                loginResponse.postValue(null);
            }
        });
    }

    public void userSignup(User user)
    {
        appDataManager.executeSignup(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code()<300)
                {
                    //we have to send the  registered data signing up as a new user

                    registerResponse.postValue(response.body());
                }
                else if(response.code()>=400)
                {
                    registerResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t)
            {
                registerResponse.postValue(null);
            }
        });
    }

    public void getUserToken()
    {
        if(appDataManager.getToken() == "")
        {
            tokenResponse.postValue(false);
        }
        else
        {
            tokenResponse.postValue(true);
        }
    }
}

