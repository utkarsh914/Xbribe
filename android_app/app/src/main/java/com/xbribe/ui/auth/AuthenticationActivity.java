package com.xbribe.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.auth.login.LoginFragment;

import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity
{

    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private String fcmToken;
    private AppDataManager appDataManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_authentication);
            ButterKnife.bind(this);

            appDataManager=((MyApplication) getApplicationContext()).getDataManager();

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    fcmToken = instanceIdResult.getToken();
                    appDataManager.saveFCMToken(fcmToken);
                    Log.e("My FCM Token",fcmToken);
                }
            });

            loginFragment=new LoginFragment();
            fragmentManager = getSupportFragmentManager();
            initFrag(loginFragment);
        }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                fcmToken = instanceIdResult.getToken();
                appDataManager.saveFCMToken(fcmToken);
                Log.e("My FCM Token",fcmToken);
            }
        });
    }

    private void initFrag(Fragment fragment) {

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frame_main, fragment);
            ft.commit();
        }
}