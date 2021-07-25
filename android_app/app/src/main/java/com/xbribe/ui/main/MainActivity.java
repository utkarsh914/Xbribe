package com.xbribe.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.xbribe.Constants;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.service.AddressService;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.auth.AuthenticationActivity;
import com.xbribe.ui.main.drawers.aboutus.AboutUsFragment;
import com.xbribe.ui.main.drawers.checkcase.CheckcaseFragment;
import com.xbribe.ui.main.drawers.contact.ContactFragment;
import com.xbribe.ui.main.drawers.drafts.DraftFragment;
import com.xbribe.ui.main.drawers.laws.LawsFragment;
import com.xbribe.ui.main.drawers.nearby.NearbyFragment;
import com.xbribe.ui.main.drawers.notification.NotificationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager fragmentManager;
    private AppDataManager appDataManager;
    private ReportFragment reportFragment;
    private CheckcaseFragment checkcaseFragment;
    private NearbyFragment nearbyFragment;
    private ContactFragment contactFragment;
    private DraftFragment draftFragment;
    private NotificationFragment notificationFragment;
    private LawsFragment lawsFragment;
    private AboutUsFragment aboutUsFragment;

    private static final int UPDATE_INTERVAL = 3000;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private static final String TAG = "Rishi";
    private String locationAddress;
    private Boolean isGPSon=false;
    private AddressReceiver addressReceiver;
    private Boolean isShowSettings=false;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private int ctr=1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        appDataManager = ((MyApplication) getApplication()).getDataManager();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        requestPermission();

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

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            startGettingLocation();
        }

        fragmentManager = getSupportFragmentManager();
        checkcaseFragment=new CheckcaseFragment();
        reportFragment = new ReportFragment();
        nearbyFragment=new NearbyFragment();
        contactFragment=new ContactFragment();
        notificationFragment=new NotificationFragment();
        draftFragment=new DraftFragment();
        lawsFragment=new LawsFragment();
        aboutUsFragment = new AboutUsFragment();

        initFrag(reportFragment);

        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        startGettingLocation();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        stopLocationRequests();
        super.onDestroy();
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
                && result5 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean storageReadAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean storageWriteAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean audioAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted && storageReadAccepted && storageWriteAccepted && audioAccepted) {
                        startGettingLocation();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to the permissions!",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                            else if(shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE))
                            {
                                showMessageOKCancel("You need to allow access to the permissions!",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
                        startAddressService(currentLocation);
                        appDataManager.saveLatitude(String.valueOf(currentLocation.getLatitude()));
                        appDataManager.saveLongitude(String.valueOf(currentLocation.getLongitude()));
                    }
                }
            });
            locationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    isGPSon = false;
                    Log.e(TAG, "Error while getting location!");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_submit)
        {
            initFrag(reportFragment);
        }
        else if (id == R.id.nav_check)
        {
        initFrag(checkcaseFragment);
        }
        else if (id == R.id.nav_notify)
        {
         initFrag(notificationFragment);
        }
        else if(id == R.id.nav_nearby)
        {
            initFrag(nearbyFragment);
        }
        else if (id == R.id.nav_contact)
        {
            initFrag(contactFragment);
        }
        else if (id == R.id.nav_about)
        {
            initFrag(aboutUsFragment);
        }
        else if(id== R.id.nav_draft)
        {
            initFrag(draftFragment);
        }
        else if(id==R.id.nav_laws)
        {
            initFrag(lawsFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_drawer)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @OnClick(R.id.logout)
    void Logout()
    {
        if(appDataManager.removeToken() && appDataManager.removeEmail() && appDataManager.removeID()
                && appDataManager.removeAddress() && appDataManager.removeLatitude() && appDataManager.removeLongitude()
                && appDataManager.removeOrgID() && appDataManager.removeMinistry() && appDataManager.removeDepartment())
        {
            Toast.makeText(MainActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,AuthenticationActivity.class));
        }
        else
        {
            Toast.makeText(MainActivity.this, "Error! Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFrag(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        ft.replace(R.id.main_frame, fragment);
        ft.commit();
    }
}
