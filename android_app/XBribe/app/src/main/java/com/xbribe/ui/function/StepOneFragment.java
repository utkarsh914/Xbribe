package com.xbribe.ui.function;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.Organizations;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.main.MainActivity;
import com.xbribe.ui.main.ReportFragment;
import com.xbribe.ui.main.drawers.drafts.DatabaseSaveDraft;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepOneFragment extends Fragment
{

    private SubmissionActivityViewModel submissionActivityViewModel;
    private ArrayList<Organizations> organizationsData;
    private ArrayList<String> departmentData;
    private SpinnerAdapter1 spinnerAdapter1;
    private SpinnerAdapter2 spinnerAdapter2;
    private StepTwoFragment step2Fragment;
    private ReportFragment reportFragment;
    private AppDataManager appDataManager;

    public String name_oraganisation,city,pincode,description,department,ministryId,official,ministry;

    private Organizations organizations;

    DatabaseSaveDraft databaseSaveDraft;

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @BindView(R.id.btn_savedrf)
    Button savedraft;

    @BindView(R.id.btn_proceed)
    Button proceed;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_ministry_loading)
    TextView tvMinistryLoading;

    @BindView(R.id.tv_department_loading)
    TextView tvDepartmentLoading;

    @BindView(R.id.et_name_organization)
    EditText etName;

    @BindView(R.id.et_city)
    EditText etCity;

    @BindView(R.id.et_pincode)
    EditText etPincode;

    @BindView(R.id.et_desc)
    EditText etDescription;

    @BindView(R.id.et_official_name)
    TextInputEditText etOfficialName;

    @BindView(R.id.spinner_ministry)
    Spinner spinnerMinistry;

    @BindView(R.id.spinner_department)
    Spinner spinnerDepartment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_step_one, container, false);
        ButterKnife.bind(this, parent);
        tvMinistryLoading.setVisibility(View.VISIBLE);
        tvDepartmentLoading.setVisibility(View.VISIBLE);

        databaseSaveDraft=new DatabaseSaveDraft(getActivity());
        databaseSaveDraft.getWritableDatabase();

        step2Fragment = new StepTwoFragment();
        reportFragment = new ReportFragment();

        Bundle bundleDraft = getActivity().getIntent().getExtras();
        if(bundleDraft!=null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("MINISTRY",bundleDraft.getString("MINISTRY"));
            bundle.putString("MINISTRYID",bundleDraft.getString("MINISTRYID"));
            bundle.putString("DEPARTMENT",bundleDraft.getString("DEPARTMENT"));
            bundle.putString("ORGANISATION",bundleDraft.getString("ORGANISATION"));
            bundle.putString("OFFICIAL",bundleDraft.getString("OFFICIAL"));
            bundle.putString("CITY",bundleDraft.getString("CITY"));
            bundle.putString("PINCODE",bundleDraft.getString("PINCODE"));
            bundle.putString("DESCRIPTION",bundleDraft.getString("DESCRIPTION"));
            bundle.putString("ADDRESS",bundleDraft.getString("ADDRESS"));
            bundle.putString("LATITUDE",bundleDraft.getString("LATITUDE"));
            bundle.putString("LONGITUDE",bundleDraft.getString("LONGITUDE"));
            step2Fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_two,step2Fragment)
                    .commit();
        }
        submissionActivityViewModel = ViewModelProviders.of(getActivity()).get(SubmissionActivityViewModel.class);
        submissionActivityViewModel.getOrganizationsDetails();
        appDataManager = ((MyApplication)getActivity().getApplication()).getDataManager();
        tvAddress.setText(appDataManager.getAddress().trim());

        etCity.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        etName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        etPincode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        etDescription.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_star_24,0);
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();

        submissionActivityViewModel.getOrganizationsDetails();
        submissionActivityViewModel.getOrganizationsResponse().observe(this, data -> {
            if (data != null)
            {
                tvMinistryLoading.setVisibility(View.GONE);
                tvDepartmentLoading.setVisibility(View.GONE);
                organizationsData = new ArrayList<>(Arrays.asList(data.getData()));
                spinnerAdapter1 = new SpinnerAdapter1(getActivity(), organizationsData);
                spinnerMinistry.setAdapter(spinnerAdapter1);
                spinnerMinistry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                         organizations = (Organizations)parent.getSelectedItem();
                         ministry=organizations.getMinistry();
                         appDataManager.saveMinistry(ministry);
                         ministryId=organizations.getID();
                         appDataManager.saveOrgID(ministryId);
                         departmentData = new ArrayList<>(Arrays.asList(organizations.getDepartments()));
                         spinnerAdapter2 = new SpinnerAdapter2(getActivity(),departmentData);
                         spinnerDepartment.setAdapter(spinnerAdapter2);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        department = (String)parent.getSelectedItem();
                        appDataManager.saveDepartment(department);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                tvMinistryLoading.setText("Ministry Details not available!");
                tvMinistryLoading.setText("Department Details not available!");
                Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

     @OnClick(R.id.btn_proceed)
     public void sendDetails()
     {
         name_oraganisation = etName.getText().toString();
         city = etCity.getText().toString();
         pincode = etPincode.getText().toString();
         description = etDescription.getText().toString();
         official = etOfficialName.getText().toString();

         if(name_oraganisation.isEmpty()==true || city.isEmpty()==true ||
                 pincode.isEmpty()==true  ||  description.isEmpty()==true)
         {
              String msg="Please fill in the details";
              showSnackbar(msg);
         }
         else
         {
             Bundle bundle = new Bundle();
             bundle.putString("MINISTRY",ministry);
             bundle.putString("MINISTRYID",ministryId);
             bundle.putString("DEPARTMENT",department);
             bundle.putString("ORGANISATION",name_oraganisation);
             if(official.isEmpty())
                 bundle.putString("OFFICIAL","Not Specified");
             else
                 bundle.putString("OFFICIAL",official);
             bundle.putString("CITY",city);
             bundle.putString("PINCODE",pincode);
             bundle.putString("DESCRIPTION",description);
             bundle.putString("ADDRESS",appDataManager.getAddress());
             bundle.putString("LATITUDE",appDataManager.getLatitude());
             bundle.putString("LONGITUDE",appDataManager.getLongitude());
             step2Fragment.setArguments(bundle);
             getActivity().getSupportFragmentManager().beginTransaction()
                     .replace(R.id.main_frame_two,step2Fragment)
                     .addToBackStack("Step 1")
                     .commit();
         }
     }
    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(relativeLayout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    @OnClick(R.id.btn_savedrf)
    void saveDraft()
    {
        name_oraganisation = etName.getText().toString();
        city = etCity.getText().toString();
        pincode = etPincode.getText().toString();
        description = etDescription.getText().toString();
        official=etOfficialName.getText().toString();
        if(name_oraganisation.isEmpty()==true || city.isEmpty()==true ||
                pincode.isEmpty()==true  ||  description.isEmpty()==true)
        {
            String msg="Please fill in the details";
            showSnackbar(msg);
        }
        else
        {
            if(official.isEmpty())
            {
                boolean ifInserted= databaseSaveDraft.insertData(ministry,appDataManager.getAddress(),pincode,city,department,name_oraganisation,description,appDataManager.getEmail(),appDataManager.getLatitude(),appDataManager.getLongitude(),"Not Specified",ministryId);
                if(ifInserted==true)
                {
                    String msg="Draft Saved";
                    showSnackbar(msg);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else
                {
                    String msg="Not saved. Please try again!";
                    showSnackbar(msg);
                }
            }
            else
            {
                boolean ifInserted= databaseSaveDraft.insertData(ministry,appDataManager.getAddress(),pincode,city,department,name_oraganisation,description,appDataManager.getEmail(),appDataManager.getLatitude(),appDataManager.getLongitude(),official,ministryId);
                if(ifInserted==true)
                {
                    String msg="Draft Saved";
                    showSnackbar(msg);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else
                {
                    String msg="Not saved. Please try again!";
                    showSnackbar(msg);
                }
            }
        }
    }

}
