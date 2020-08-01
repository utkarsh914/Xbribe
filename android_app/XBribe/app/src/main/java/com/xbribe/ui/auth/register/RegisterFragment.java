package com.xbribe.ui.auth.register;

import android.content.Context;
import android.os.Bundle;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.xbribe.R;
import com.xbribe.data.models.User;
import com.xbribe.ui.auth.login.LoginFragment;
import com.xbribe.ui.auth.AuthenticationActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment
{
    @BindView(R.id.constraint)
    ConstraintLayout constraintLayout;

    @BindView(R.id.account_login)
    TextView login;

    @BindView(R.id.btn_register)
    MaterialButton btnRegister;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_confirmpassword)
    EditText etConfirmPassword;

    @BindView(R.id.pb_register)
    ProgressBar pbRegister;

    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private AuthenticationActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent=inflater.inflate(R.layout.fragment_register,container,false);
        ButterKnife.bind(this,parent);
        viewModel= ViewModelProviders.of(getActivity()).get(AuthenticationActivityViewModel.class);

        loginFragment= new LoginFragment();

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getRegisterResponse().observe(this, data->{
            if(data==null)
            {
                String msg="Error,Please try again";
                showSnackbar(msg);

            }
            else
            {
                String msg="User registered successfully";
                showSnackbar(msg);
                fragmentManager = getActivity().getSupportFragmentManager();
                initFrag(loginFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @OnClick(R.id.account_login)
    void existingUser()
    {
        fragmentManager = getActivity().getSupportFragmentManager();
        initFrag(loginFragment);
    }

    @OnClick(R.id.btn_register)
    void register()
    {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmpassword = etConfirmPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty())
        {
            String msg="Please enter all the details!";
            showSnackbar(msg);
        }
        else
        {
            if(password.equals(confirmpassword))
            {
                showProgress();
                User user= new User(email,password);
                viewModel.userSignup(user);
                viewModel.getRegisterResponse().observe(this, data->{
                    if(data==null)
                    {
                        hideProgress();
                        String msg="Error,Please try again";
                        showSnackbar(msg);
                    }
                    else
                    {
                        hideProgress();
                        String msg="User registered successfully";
                        showSnackbar(msg);
                        fragmentManager = getActivity().getSupportFragmentManager();
                        initFrag(loginFragment);
                    }
                });
            }
            else
            {
                String msg="Passwords don't match!";
                showSnackbar(msg);
            }
        }
    }

    private void initFrag(Fragment fragment) {

        FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack("Fragment2");
        ft.replace(R.id.frame_main, fragment);
        ft.commit();

    }
    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(constraintLayout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showProgress() {
        pbRegister.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbRegister.setVisibility(View.GONE);
    }
}
