package com.xbribe.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.function.SubmissionActivity;
import com.xbribe.ui.main.drawers.checkcase.CheckcaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportFragment extends Fragment {

    @BindView(R.id.slider_home_events)
    SliderView sliderView;

    @BindView(R.id.tv_email)
    TextView tvEmail;

    @BindView(R.id.report_bribe)
    MaterialCardView btnReport;

    @BindView(R.id.check_cases)
    MaterialCardView btnCheckCases;

    @BindView(R.id.secret_fragment)
    FloatingActionButton fbtnSecretFragment;

    private ReportViewModel reportViewModel;
    private AppDataManager appDataManager;
    private CheckcaseFragment checkcaseFragment;
    /*private NotificationFragment notificationFragment;
    private SecretFragment secretFragment;*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_report,container,false);
        ButterKnife.bind(this,parent);

        reportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        appDataManager = ((MyApplication) getActivity().getApplicationContext()).getDataManager();

        if(appDataManager.getEmail()=="")
        {
            reportViewModel.getEmailDetails();
        }
        else
        {
            tvEmail.setText(appDataManager.getEmail());
        }
        initSlider();

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        reportViewModel.getEmailResponse().observe(this,data->{
            if(data)
            {
                tvEmail.setText(appDataManager.getEmail());
            }
        });
    }

    @OnClick(R.id.report_bribe)
    void reportBribery() {
            startActivity(new Intent(getActivity(), SubmissionActivity.class));
    }

    @OnClick(R.id.check_cases)
    void checkCases()
    {
        checkcaseFragment = new CheckcaseFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame,checkcaseFragment)
                .addToBackStack("Report")
                .commit();
    }

    private void initSlider()
    {
        SliderAdapter adapter = new SliderAdapter(getActivity());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5);
        sliderView.startAutoCycle();
    }
}
