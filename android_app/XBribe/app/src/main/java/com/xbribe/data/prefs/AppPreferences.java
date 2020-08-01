package com.xbribe.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xbribe.data.api.ApiHelper;

public class AppPreferences implements AppPreferencesHelper {

    private Context context;
    private  static AppPreferences instance;

    public static final String TOKEN_KEY = "token";
    public static final String EMAIL_KEY = "email";
    public static final String ID_KEY = "id";
    public static final String ORGID_KEY = "orgid";
    public static final String MINISTRY_KEY = "ministry";
    public static final String DEPARTMENT_KEY = "department";
    public static final String FCMTOKEN_KEY = "fcmtoken";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String ADDRESS_KEY = "address";

    private SharedPreferences sharedPrefs;

    public AppPreferences(Context context) {
        this.context=context;
        sharedPrefs= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static AppPreferences getInstance(Context context){
        if(instance == null){
            synchronized (ApiHelper.class){
                if(instance == null){
                    instance = new AppPreferences(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TOKEN_KEY , token);
        editor.apply();
    }

    @Override
    public void saveEmail(String email) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(EMAIL_KEY , email);
        editor.apply();
    }

    @Override
    public void saveID(String ID) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ID_KEY , ID);
        editor.apply();
    }

    @Override
    public void saveOrgID(String orgID) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ORGID_KEY , orgID);
        editor.apply();
    }

    @Override
    public void saveMinistry(String ministry) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(MINISTRY_KEY , ministry);
        editor.apply();
    }

    @Override
    public void saveDepartment(String department) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(DEPARTMENT_KEY , department);
        editor.apply();
    }

    @Override
    public void saveFCMToken(String FCMToken) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(FCMTOKEN_KEY , FCMToken);
        editor.apply();
    }

    @Override
    public void saveLatitude(String latitude) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LATITUDE_KEY , latitude);
        editor.apply();
    }

    @Override
    public void saveLongitude(String longitude) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LONGITUDE_KEY , longitude);
        editor.apply();
    }

    @Override
    public void saveAddress(String address) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ADDRESS_KEY , address);
        editor.apply();
    }

    @Override
    public Boolean removeToken() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(TOKEN_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeEmail() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(EMAIL_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeID() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(ID_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeOrgID() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(ORGID_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeMinistry() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(MINISTRY_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeDepartment() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(DEPARTMENT_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeFCMToken() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(FCMTOKEN_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeLatitude() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(LATITUDE_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeLongitude() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(LONGITUDE_KEY);
        return  editor.commit();
    }

    @Override
    public Boolean removeAddress() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(ADDRESS_KEY);
        return  editor.commit();
    }

    @Override
    public String getToken() {
        return  sharedPrefs.getString(TOKEN_KEY , "");
    }

    @Override
    public String getEmail() {
        return  sharedPrefs.getString(EMAIL_KEY , "");
    }

    @Override
    public String getID() {
        return sharedPrefs.getString(ID_KEY , "");
    }

    @Override
    public String getOrgID() {
        return sharedPrefs.getString(ORGID_KEY , "");
    }

    @Override
    public String getMinistry() {
        return sharedPrefs.getString(MINISTRY_KEY , "");
    }

    @Override
    public String getDepartment() {
        return sharedPrefs.getString(DEPARTMENT_KEY , "");
    }

    @Override
    public String getFCMToken() {
        return sharedPrefs.getString(FCMTOKEN_KEY , "");
    }

    @Override
    public String getLatitude() {
        return sharedPrefs.getString(LATITUDE_KEY , "");
    }

    @Override
    public String getLongitude() {
        return sharedPrefs.getString(LONGITUDE_KEY , "");
    }

    @Override
    public String getAddress() {
        return sharedPrefs.getString(ADDRESS_KEY , "");
    }
}

