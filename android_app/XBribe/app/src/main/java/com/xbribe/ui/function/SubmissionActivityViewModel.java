package com.xbribe.ui.function;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.CaseData;
import com.xbribe.data.models.CaseResponse;
import com.xbribe.data.models.LocationDetails;
import com.xbribe.data.models.OrganizationResponse;
import com.xbribe.ui.MyApplication;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmissionActivityViewModel extends AndroidViewModel {

    private AppDataManager appDataManager;
    private MutableLiveData<OrganizationResponse> organizationResponse;
    private MutableLiveData<CaseData> caseResponse;
    private MutableLiveData<ResponseBody> sendOtp;
    private MutableLiveData<ResponseBody> verifyOtp;

    public SubmissionActivityViewModel(@NonNull Application application)
    {
        super(application);
        appDataManager = ((MyApplication)application).getDataManager();
        organizationResponse = new MutableLiveData<>();
        caseResponse = new MutableLiveData<>();
        sendOtp = new MutableLiveData<>();
        verifyOtp = new MutableLiveData<>();
    }

    public MutableLiveData<OrganizationResponse> getOrganizationsResponse() { return organizationResponse; }
    public MutableLiveData<ResponseBody> getSendOtp() {return sendOtp;}
    public  MutableLiveData<CaseData> getCaseResponse()
    {
        return caseResponse;
    }
    public MutableLiveData<ResponseBody> getVerifyOtp() {return verifyOtp;}

  public  void reportCaseDetails(String token,String ministryId, String department, String officialName, String name, String place, String address, String pin, String latitude, String longitude, String description, ArrayList<String> picsArray, ArrayList<String> audiosArray, ArrayList<String> videosArray)
  {
      appDataManager.reportCase(token,ministryId,department,officialName,name,place,address,pin,latitude,longitude,description,picsArray,audiosArray,videosArray).enqueue(new Callback<CaseData>() {
          @Override
          public void onResponse(Call<CaseData> call, Response<CaseData> response)
          {
              if (response.code() < 300) {
                  Log.e("CASE","Reported");
                  caseResponse.postValue(response.body());
              }
              else {
                  Log.e("CASE","Not Reported");
                  String x =Integer.toString(response.code());
                  Log.e("VALUE",x);
                  caseResponse.postValue(null);
              }
          }
          @Override
          public void onFailure(Call<CaseData> call, Throwable t) {
              Log.e("CASE",t.getMessage());
              caseResponse.postValue(null);
          }
      });
  }

    public void getOrganizationsDetails()
    {
        appDataManager.getOrganizations().enqueue(new Callback<OrganizationResponse>()
        {
            @Override
            public void onResponse(Call<OrganizationResponse> call, Response<OrganizationResponse> response) {
                if(response.code()<300)
                {
                    organizationResponse.postValue(response.body());
                }
                else
                {
                    organizationResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<OrganizationResponse> call, Throwable t) {
                organizationResponse.postValue(null);
            }
        });
    }

    public void checkOTP(Integer otp)
    {
        appDataManager.verifyOtp(appDataManager.getEmail(),otp).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() < 300) {
                    Log.e("OTP","Verified");
                    verifyOtp.postValue(response.body());
                }
                else {
                    Log.e("OTP","Not Verified");
                    verifyOtp.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OTP",t.getMessage());
                verifyOtp.postValue(null);
            }
        });
    }

    public void setSendOtp()
    {
        appDataManager.sendOtp(appDataManager.getEmail()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()<300)
                {
                    sendOtp.postValue(response.body());
                }
                else
                {
                    sendOtp.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                sendOtp.postValue(null);
            }
        });
    }
}
