package com.xbribe.ui.main.drawers.nearby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.NearbyCaseResponse;
import com.xbribe.ui.MyApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NearbyFragment extends Fragment {

    @BindView(R.id.pb_nearbycases)
    ProgressBar progressBar;

    @BindView(R.id.list_nearby_cases)
    RecyclerView rViewNeabryCases;

    @BindView(R.id.btn_500m)
    MaterialButton button500;

    @BindView(R.id.btn_1000m)
    MaterialButton button1000;

    @BindView(R.id.btn_10000m)
    MaterialButton button10000;

    @BindView(R.id.btn_100000m)
    MaterialButton button100000;

    @BindView(R.id.tv_nearby_cases)
    TextView tvNoNearbyCases;

    private AppDataManager appDataManager;
    private NearbyViewModel nearbyViewModel;
    private NearbyAdapter nearbyAdapter;
    private List<NearbyCaseResponse> mNearbyCases;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_nearby_cases,container,false);
        ButterKnife.bind(this,parent);

        appDataManager = ((MyApplication) getActivity().getApplicationContext()).getDataManager();
        nearbyViewModel = ViewModelProviders.of(getActivity()).get(NearbyViewModel.class);

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();

        nearbyViewModel.getNearbyCaseResponse().observe(this,data->{
            if(data!=null)
            {
                tvNoNearbyCases.setVisibility(View.GONE);
                mNearbyCases = data;
                initRecyclerView(mNearbyCases);
            }
            else
            {
                tvNoNearbyCases.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.btn_500m)
    void click500()
    {
        progressBar.setVisibility(View.VISIBLE);
        nearbyViewModel.getNearbyCases(appDataManager.getToken(),Double.valueOf(appDataManager.getLatitude()),Double.valueOf(appDataManager.getLongitude()),500);
    }

    @OnClick(R.id.btn_1000m)
    void click1000()
    {
        progressBar.setVisibility(View.VISIBLE);
        nearbyViewModel.getNearbyCases(appDataManager.getToken(),Double.valueOf(appDataManager.getLatitude()),Double.valueOf(appDataManager.getLongitude()),1000);
    }

    @OnClick(R.id.btn_10000m)
    void click10000()
    {
        progressBar.setVisibility(View.VISIBLE);
        nearbyViewModel.getNearbyCases(appDataManager.getToken(),Double.valueOf(appDataManager.getLatitude()),Double.valueOf(appDataManager.getLongitude()),10000);
    }

    @OnClick(R.id.btn_100000m)
    void click100000()
    {
        progressBar.setVisibility(View.VISIBLE);
        nearbyViewModel.getNearbyCases(appDataManager.getToken(),Double.valueOf(appDataManager.getLatitude()),Double.valueOf(appDataManager.getLongitude()),50000);
    }

    private void initRecyclerView(List<NearbyCaseResponse> mNearbyCases)
    {
        progressBar.setVisibility(View.GONE);
        if(mNearbyCases!=null)
        {
            nearbyAdapter = new NearbyAdapter(mNearbyCases,getActivity());
            rViewNeabryCases.setLayoutManager(new LinearLayoutManager(getActivity()));
            rViewNeabryCases.setAdapter(nearbyAdapter);
        }
    }
}
