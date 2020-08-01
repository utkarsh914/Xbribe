package com.xbribe.data.prefs;

public interface AppPreferencesHelper {

    void saveToken(String token);
    void saveEmail(String email);
    void saveID(String ID);
    void saveOrgID(String orgID);
    void saveMinistry(String ministry);
    void saveDepartment(String department);
    void saveFCMToken(String FCMToken);
    void saveLatitude(String latitude);
    void saveLongitude(String longitude);
    void saveAddress(String address);

    Boolean removeToken();
    Boolean removeEmail();
    Boolean removeID();
    Boolean removeOrgID();
    Boolean removeMinistry();
    Boolean removeDepartment();
    Boolean removeFCMToken();
    Boolean removeLatitude();
    Boolean removeLongitude();
    Boolean removeAddress();

    String getToken();
    String getEmail();
    String getID();
    String getOrgID();
    String getMinistry();
    String getDepartment();
    String getFCMToken();
    String getLatitude();
    String getLongitude();
    String getAddress();
}
