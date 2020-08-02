package com.xbribe.ui.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.xbribe.Constants;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.service.AddressService;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.main.drawers.drafts.DatabaseSaveDraft;

import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class SubmissionActivity  extends AppCompatActivity
{

    private static final int UPDATE_INTERVAL = 2000;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private static final String TAG = "Rishi";
    private String locationAddress;
    private Boolean isGPSon=false;
    private AddressReceiver addressReceiver;
    private Boolean isShowSettings=false;
    private int ctr=1;

    private FragmentManager fragmentManager;
    private StepOneFragment step_one_fragment;
    private StepTwoFragment stepTwoFragment;
    private AppDataManager appDataManager;

    DatabaseSaveDraft databaseSaveDraft;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseSaveDraft=new DatabaseSaveDraft(this);
        databaseSaveDraft.getWritableDatabase();

        fragmentManager = getSupportFragmentManager();
        step_one_fragment=new StepOneFragment();
        stepTwoFragment = new StepTwoFragment();

        initFrag(step_one_fragment);
        appDataManager = ((MyApplication) getApplication()).getDataManager();

        final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i(TAG, "Location result is available");
                isGPSon=true;
                isShowSettings = true;
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if (locationAvailability.isLocationAvailable()) {
                    Log.i(TAG, "Location is available");
                    ctr++;
                    isGPSon=true;
                    isShowSettings = true;
                } else {
                    Log.i(TAG, "Location is unavailable");
                    isGPSon=false;
                    if(ctr==1)
                    {
                        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            showSettingsAlert();
                        }
                    }
                    else {
                        isShowSettings=false;
                        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        {
                            showSettingsAlert();
                        }
                        ctr++;
                    }
                }
            }
        };

        startGettingLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startGettingLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationRequests();
    }

    private void startGettingLocation() {
        if (checkPermission())
        {
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, this.getMainLooper());
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    currentLocation = location;
                    if (currentLocation != null) {
                        isGPSon = true;
                        isShowSettings = true;
                        getAddress(currentLocation);
                        appDataManager.saveLatitude(String.valueOf(currentLocation.getLatitude()));
                        appDataManager.saveLongitude(String.valueOf(currentLocation.getLongitude()));
                    }
                }
            });
            locationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    isGPSon = false;
                    Log.i(TAG, "Error while getting location!");
                }
            });
        }
    }

    private void stopLocationRequests() {
        locationProviderClient.removeLocationUpdates(locationCallback);
    }

    private class AddressReceiver extends ResultReceiver {
        public AddressReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                locationAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                appDataManager.saveAddress(locationAddress);
            } else {
                locationAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                appDataManager.saveAddress(locationAddress);
            }
        }
    }

    private void getAddress(Location location) {
        if (!Geocoder.isPresent())
        {
            Toast.makeText(this, "Network connectivity issues!", Toast.LENGTH_SHORT).show();
        } else {
            startAddressService(location);
        }
    }

    private void startAddressService(Location location) {
        Intent intent = new Intent(this, AddressService.class);
        addressReceiver = new AddressReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, addressReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    public void showSettingsAlert() {

        if(!isShowSettings) {
            isShowSettings = true;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS is not enabled!");
            alertDialog.setMessage("Please turn on the GPS");

            alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    isGPSon = true;
                    isShowSettings=true;
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    isGPSon = false;
                    isShowSettings = false;
                    showSettingsAlert();
                }
            });

            alertDialog.show();
        }
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void initFrag(Fragment fragment) {

       FragmentTransaction ft = fragmentManager.beginTransaction();
       ft.setCustomAnimations(android.R.anim.fade_in,
               android.R.anim.fade_out);
       ft.replace(R.id.main_frame_two, fragment);
       ft.commit();

   }
}
