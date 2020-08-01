package com.xbribe.ui.auth.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.User;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.auth.register.RegisterFragment;
import com.xbribe.ui.main.MainActivity;
import com.xbribe.ui.auth.AuthenticationActivityViewModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginFragment extends Fragment

{
    @BindView(R.id.scrollable)
    ConstraintLayout constraintLayout;

    @BindView(R.id.btn_login)
    MaterialButton login;

    @BindView(R.id.et_email)
    TextInputEditText email;

    @BindView(R.id.et_password)
    TextInputEditText password;

    @BindView(R.id.new_to_xcom)
    TextView register;

    @BindView(R.id.pb_login)
    ProgressBar pbLogin;

    private FragmentManager fragmentManager;
    private RegisterFragment registerFragment;
    private AuthenticationActivityViewModel viewModel;
    private AppDataManager appDataManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent=inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this,parent);
        viewModel= ViewModelProviders.of(getActivity()).get(AuthenticationActivityViewModel.class);

        appDataManager = ((MyApplication) getActivity().getApplicationContext()).getDataManager();

        registerFragment=new RegisterFragment();

        if(appDataManager.getToken()!="")
        {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getLoginResponse().observe(this,data-> {
            if(data==null)
            {
                String msg="Wrong Credentials";
                showSnackbar(msg);

            }
            else
            {

                String msg="Logged In Successfully";
                showSnackbar(msg);
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
    }

    @OnClick(R.id.new_to_xcom)
    void newUser()
    {
        fragmentManager = getActivity().getSupportFragmentManager();
        initFrag(registerFragment);
    }

    @OnClick(R.id.btn_login)
    void signin()
    {
        String etEmail = email.getText().toString();
        String etPassword = password.getText().toString();

        if(etEmail.isEmpty() || etPassword.isEmpty())
        {

            String msg="Please enter all the details";
            showSnackbar(msg);
        }
        else
        {
            if(appDataManager.getFCMToken().isEmpty())
            {
                String msg = "Please Wait!";
                showSnackbar(msg);
            }
            else {
                showProgress();
                User user = new User(email.getText().toString(), password.getText().toString(), appDataManager.getFCMToken());
                viewModel.userLogin(user);

                viewModel.getLoginResponse().observe(this, data -> {
                    if (data == null) {
                        hideProgress();
                        String msg = "Wrong Credentials";
                        showSnackbar(msg);
                    } else {
                        hideProgress();
                        String msg = "Logged In Successfully";
                        showSnackbar(msg);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                });
            }
        }
    }

    private void initFrag(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame_main, fragment);
        ft.commit();
    }
    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(constraintLayout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showProgress() {
        pbLogin.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbLogin.setVisibility(View.GONE);
    }
}