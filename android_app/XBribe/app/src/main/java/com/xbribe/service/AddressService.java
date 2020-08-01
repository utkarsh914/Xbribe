package com.xbribe.service;

import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.xbribe.Constants;

import java.util.List;
import java.util.Locale;

public class AddressService extends IntentService
{

    private ResultReceiver resultReceiver;
    private Location deviceLocation;


    public AddressService() {
        super("AddressService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        if(resultReceiver==null)
        {
            return;
        }

        deviceLocation = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        if(deviceLocation == null)
        {
            respondWithResult(Constants.FAILURE_RESULT, "Location unavailable1! Please reopen the screen.");
            return;
        }

        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> address = null;
        try
        {
            address=geocoder.getFromLocation(deviceLocation.getLatitude(),deviceLocation.getLongitude(),1);
            if(address == null && address.size() == 0)
            {
                respondWithResult(Constants.FAILURE_RESULT,"Location unavailable! Please reopen the screen.");
            }
            else
            {
                Address address1=address.get(0);
                StringBuilder stringBuilder=new StringBuilder("");

                for(int i=0;i<=address1.getMaxAddressLineIndex();i++)
                {
                    stringBuilder.append(address1.getAddressLine(i)+" ");
                }
                respondWithResult(Constants.SUCCESS_RESULT,stringBuilder.toString().trim());
            }
        }
        catch (Exception e )
        {
            respondWithResult(Constants.FAILURE_RESULT,"Location unavailable! Please reopen the screen.");
        }
    }

    private void respondWithResult(int resultCode, String resultMessage)
    {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY,resultMessage);
        resultReceiver.send(resultCode,bundle);
    }

}
