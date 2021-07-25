package com.xbribe.ui;

import android.app.Application;
import android.content.Context;

import com.xbribe.data.AppDataManager;
import com.xbribe.data.api.ApiHelper;
import com.xbribe.data.prefs.AppPreferences;

public class MyApplication extends Application
{

    private AppDataManager appDataManager;

    public  AppDataManager getDataManager(){
        if(appDataManager == null){
            synchronized (ApiHelper.class){
                if(appDataManager == null){
                    appDataManager = new AppDataManager(this);
                }
            }
        }
        return appDataManager;
    }
}
