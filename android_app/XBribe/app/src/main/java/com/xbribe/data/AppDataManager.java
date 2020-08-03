package com.xbribe.data;

import android.content.Context;

import com.xbribe.data.api.ApiHelper;
import com.xbribe.data.models.CaseData;
import com.xbribe.data.models.CaseResponse;
import com.xbribe.data.models.NearbyCaseResponse;
import com.xbribe.data.models.OrganizationResponse;
import com.xbribe.data.models.TokenResponse;
import com.xbribe.data.models.User;
import com.xbribe.data.prefs.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AppDataManager implements AppDataManagerHelper {

    private Context context;
    private ApiHelper apiHelper;
    private AppPreferences appPreferences;

    public AppDataManager(Context context) {
        this.context = context;
        apiHelper = ApiHelper.getInstance(context);
        appPreferences = AppPreferences.getInstance(context);
    }

    @Override
    public Call<User> executeSignup(User user) {
        return apiHelper.executeSignup(user);
    }

    @Override
    public Call<User> executeLogin(User user) {
        return apiHelper.executeLogin(user);
    }

    @Override
    public Call<TokenResponse> getDetails(String token) {
        return apiHelper.getDetails(token);
    }

    @Override
    public Call<OrganizationResponse> getOrganizations() {
        return apiHelper.getOrganizations();
    }

    @Override
    public Call<CaseData> reportCase(String token,String orgId, String department, String officialName, String name, String place, String address, String pin, String latitude, String longitude, String description, ArrayList<String> picsArray, ArrayList<String> audiosArray, ArrayList<String> videosArray) {
        return apiHelper.reportCase(token, orgId, department, officialName, name, place, address, pin, latitude, longitude, description, picsArray, audiosArray, videosArray);
    }

    @Override
    public Call<List<NearbyCaseResponse>> getNearbyCases(String token, Double latitude, Double longitude, Integer radius) {
        return apiHelper.getNearbyCases(token, latitude, longitude, radius);
    }


    @Override
    public Call<ResponseBody> sendOtp(String email) {
        return apiHelper.sendOtp(email);
    }

    @Override
    public Call<ResponseBody> verifyOtp(String email, Integer otp) {
        return apiHelper.verifyOtp(email, otp);
    }

    @Override
    public void saveToken(String token) {
        appPreferences.saveToken(token);
    }

    @Override
    public void saveEmail(String email)
    {
        appPreferences.saveEmail(email);
    }

    @Override
    public void saveID(String ID)
    {
        appPreferences.saveID(ID);
    }

    @Override
    public void saveOrgID(String orgID) {
        appPreferences.saveOrgID(orgID);
    }

    @Override
    public void saveMinistry(String ministry) {
        appPreferences.saveMinistry(ministry);
    }

    @Override
    public void saveDepartment(String department) {
        appPreferences.saveDepartment(department);
    }

    @Override
    public void saveFCMToken(String FCMToken) {
        appPreferences.saveFCMToken(FCMToken);
    }

    @Override
    public void saveLatitude(String latitude) {
        appPreferences.saveLatitude(latitude);
    }

    @Override
    public void saveLongitude(String longitude) {
        appPreferences.saveLongitude(longitude);
    }

    @Override
    public void saveAddress(String address) {
        appPreferences.saveAddress(address);
    }

    @Override
    public String getToken() {
        return appPreferences.getToken();
    }

    @Override
    public String getEmail() {
        return appPreferences.getEmail();
    }

    @Override
    public String getID() {
        return appPreferences.getID();
    }

    @Override
    public String getOrgID() {
        return appPreferences.getOrgID();
    }

    @Override
    public String getMinistry() {
        return appPreferences.getMinistry();
    }

    @Override
    public String getDepartment() {
        return appPreferences.getDepartment();
    }

    @Override
    public String getFCMToken() {
        return appPreferences.getFCMToken();
    }

    @Override
    public String getLatitude() {
        return appPreferences.getLatitude();
    }

    @Override
    public String getLongitude() {
        return appPreferences.getLongitude();
    }

    @Override
    public String getAddress() {
        return appPreferences.getAddress();
    }

    @Override
    public Boolean removeToken() {
        return appPreferences.removeToken();
    }

    @Override
    public Boolean removeEmail() {
        return appPreferences.removeEmail();
    }

    @Override
    public Boolean removeID() {
        return appPreferences.removeID();
    }

    @Override
    public Boolean removeOrgID() {
        return appPreferences.removeOrgID();
    }

    @Override
    public Boolean removeMinistry() {
        return appPreferences.removeMinistry();
    }

    @Override
    public Boolean removeDepartment() {
        return appPreferences.removeDepartment();
    }

    @Override
    public Boolean removeFCMToken() {
        return appPreferences.removeFCMToken();
    }

    @Override
    public Boolean removeLatitude() {
        return appPreferences.removeLatitude();
    }

    @Override
    public Boolean removeLongitude() {
        return appPreferences.removeLongitude();
    }

    @Override
    public Boolean removeAddress() {
        return appPreferences.removeAddress();
    }
}
