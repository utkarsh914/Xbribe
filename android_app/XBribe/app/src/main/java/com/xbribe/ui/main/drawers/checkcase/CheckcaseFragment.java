package com.xbribe.ui.main.drawers.checkcase;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.data.models.CollecImages;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.function.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckcaseFragment extends  Fragment
{
    ArrayList<CollecImages> collecImages=new ArrayList<>();

    ArrayList<String>  imagelist=new ArrayList();

    private  AppDataManager appDataManager;

    Cursor cursor;

    int flag=0;

    DatabaseHelper databaseHelper;

    @BindView(R.id.tv_no_cases)
    TextView nocases;

    @BindView(R.id.recycler_checkcase)
    RecyclerView recyclerView;

    CheckCaseAdapter checkCaseAdapter;

    List<CheckcaseModel> caselist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_check_case, container, false);
        ButterKnife.bind(this, parent);
        nocases.setVisibility(View.INVISIBLE);
        databaseHelper=new DatabaseHelper(getActivity());
        databaseHelper.getWritableDatabase();
        appDataManager = ((MyApplication) getActivity().getApplicationContext()).getDataManager();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://5ee7467352bb0500161fd73a.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImageApiService imageApiService=retrofit.create(ImageApiService.class);
        Call<ArrayList<CollecImages>> call=imageApiService.getImages();
        call.enqueue(new Callback<ArrayList<CollecImages>>() {
            @Override
            public void onResponse(Call<ArrayList<CollecImages>> call, Response<ArrayList<CollecImages>> response)
            {

                if(response.code()==200)
                {
                    collecImages = response.body();
                }
                int i=0;
                for (CollecImages coll : collecImages)
                {
                    imagelist.add(i,coll.getImageUrl());
                    i++;
                }
                initreycycleradapter(imagelist);
            }
            @Override
            public void onFailure(Call<ArrayList<CollecImages>> call, Throwable t)
            {
                Toast.makeText(getActivity(),"Error"+t.toString(),Toast.LENGTH_LONG).show();
            }
        });

        return parent;

    }

    public void initreycycleradapter(ArrayList<String> imagelist)
    {
        cursor=databaseHelper.getAllDetails();

        if(cursor.getCount()==0)
        {
            nocases.setVisibility(View.VISIBLE);
           // showMessage("Error","Nothing found");
        }
        else
        {
            checkCaseAdapter = new CheckCaseAdapter(getContext(), uploadlist(imagelist));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(checkCaseAdapter);
        }
    }

    public List<CheckcaseModel> uploadlist(ArrayList<String> imag)
    {
            caselist = new ArrayList<>();
            int i=0;
            //cursor.moveToPosition(cursor.getCount());
            cursor.moveToPosition(cursor.getCount());
            while (cursor.moveToPrevious())
            {
             if(cursor.getString(13).equals(appDataManager.getEmail()))
             {
                 caselist.add(new CheckcaseModel(imag.get(i),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(14),cursor.getString(3),cursor.getString(2),cursor.getString(7),cursor.getString(8),cursor.getString(9)));
                 i++;
                 nocases.setVisibility(View.INVISIBLE);
                 flag=1;
             }

            }
            if(flag!=1)
            {
                nocases.setVisibility(View.VISIBLE);
            }
            return caselist;

    }

}

