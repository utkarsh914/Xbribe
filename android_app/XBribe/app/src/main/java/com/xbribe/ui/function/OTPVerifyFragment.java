package com.xbribe.ui.function;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.goodiebag.pinview.Pinview;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.main.MainActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OTPVerifyFragment extends Fragment{

    @BindView(R.id.pinview)
    Pinview otpValue;

    @BindView(R.id.btn_submit)
    MaterialButton btnSubmit;

    @BindView(R.id.tv_resend)
    TextView tvResend;

    @BindView(R.id.otp_constraint_layout)
    ConstraintLayout otponstraintLayout;

    @BindView(R.id.pb_submit)
    ProgressBar pbSubmit;

    private int imageCount,audioCount,videoCount;
    private  String name,city,pincode,ministryId,department,description,address,latitude,longitude,officialName,ministry,date ;

    private DatabaseHelper databaseHelper;
    private ArrayList<String> imageURL = new ArrayList<String>();
    private ArrayList<String> audioURL = new ArrayList<String>();
    private ArrayList<String> videoURL = new ArrayList<String>();

    private AppDataManager appDataManager;
    private SubmissionActivityViewModel submissionActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_otp,container,false);
        ButterKnife.bind(this,parent);
        submissionActivityViewModel = ViewModelProviders.of(getActivity()).get(SubmissionActivityViewModel.class);
        appDataManager = ((MyApplication) getActivity().getApplication()).getDataManager();
        return parent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        databaseHelper=new DatabaseHelper(getActivity());
        databaseHelper.getWritableDatabase();
        ministry = getArguments().getString("MINISTRY");
        ministryId=getArguments().getString("MINISTRYID");
        department=getArguments().getString("DEPARTMENT");
        name=getArguments().getString("ORGANISATION");
        officialName=getArguments().getString("OFFICIAL");
        city=getArguments().getString("CITY");
        pincode=getArguments().getString("PINCODE");
        description=getArguments().getString("DESCRIPTION");
        address=getArguments().getString("ADDRESS");
        latitude=getArguments().getString("LATITUDE");
        longitude=getArguments().getString("LONGITUDE");
        imageURL=getArguments().getStringArrayList("IMGARRAY");
        audioURL=getArguments().getStringArrayList("AUDARRAY");
        videoURL=getArguments().getStringArrayList("VIDARRAY");

        imageCount=Integer.valueOf(imageURL.size());
        audioCount=Integer.valueOf(audioURL.size());
        videoCount=Integer.valueOf(videoURL.size());
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE , dd-MMM-yyyy hh:mm:ss a");
        date=simpleDateFormat.format(calendar.getTime());

    }

    @OnClick(R.id.btn_submit)
    void setBtnSubmit()
    {
        showProgress();
        Integer otp = Integer.valueOf(otpValue.getValue());
        submissionActivityViewModel.checkOTP(otp);
        submissionActivityViewModel.getVerifyOtp().observe(this, data->{
            if(data==null)
            {
                Toast.makeText(getActivity(), "Incorrect OTP!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                submissionActivityViewModel.reportCaseDetails(appDataManager.getToken(),ministryId,department,officialName,name,city,address,pincode,latitude,longitude,description,imageURL,audioURL,videoURL);
                submissionActivityViewModel.getCaseResponse().observe(this, res ->
                {
                    if(res == null)
                    {
                        hideProgress();
                        String msg="Error. Please try again!";
                        showSnackbar(msg);
                    }
                    else
                    {
                        hideProgress();
                        boolean ifInserted= databaseHelper.insertData(appDataManager.getToken(),address,description,ministry,department,name,imageCount,audioCount,videoCount,"STATUS","CASEID",appDataManager.getID(),appDataManager.getEmail(),officialName,date);
                        if(ifInserted==true)
                        {
                            Log.e("Cases Reported Table","Data inserted");
                        }
                        String msg = "Reported Successfully";
                        showSnackbar(msg);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                });
            }
        });
    }
    @OnClick(R.id.tv_resend)
    void setTvResend()
    {
        submissionActivityViewModel.setSendOtp();
        submissionActivityViewModel.getSendOtp().observe(this, data->{
            if(data!=null)
            {
                String msg = "OTP Sent!";
                showSnackbar(msg);
            }
            else
            {
                Toast.makeText(getActivity(), "Please resend again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(otponstraintLayout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showProgress() {
        pbSubmit.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbSubmit.setVisibility(View.GONE);
    }
}
