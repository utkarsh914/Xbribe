package com.xbribe.ui.main.drawers.notification;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.function.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment
{
    private AppDataManager appDataManager;

    @BindView(R.id.recycler_notify)
    RecyclerView recyclerView;
    DatabaseHelperNotice databaseHelperNotice;
    SQLiteDatabase sqLiteDatabase;


    @BindView(R.id.tv_no_notification)
    TextView notification;

    NotificationAdapter notificationAdapter;

    RecyclerView.LayoutManager layoutManager;

    List<NotificationModel> nlist;
    int flag=0;

    Cursor cursor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent=inflater.inflate(R.layout.fragment_notification,container,false);
        ButterKnife.bind(this,parent);
        databaseHelperNotice=new DatabaseHelperNotice(getContext());
        databaseHelperNotice.getWritableDatabase();
        appDataManager = ((MyApplication) getActivity().getApplicationContext()).getDataManager();
        initreycycleradapter();
        return parent;

    }
    private void initreycycleradapter()
    {
        cursor=databaseHelperNotice.getAllDetails();
        if(cursor.getCount()==0)
        {
            notification.setVisibility(View.VISIBLE);
            // showMessage("Error","Nothing found");

        }
        else
        {
            notificationAdapter = new NotificationAdapter(getContext(),uploadlist());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(notificationAdapter);
        }


    }
        private List<NotificationModel> uploadlist()
        {
            nlist=new ArrayList<>();
            while (cursor.moveToNext())
            {
                if(cursor.getString(1).equals(appDataManager.getEmail()))
                {
                nlist.add(new NotificationModel(cursor.getString(3), cursor.getString(2)));
                notification.setVisibility(View.INVISIBLE);
                flag=1;
                }
            }
            if(flag!=1)
            {
                notification.setVisibility(View.VISIBLE);
            }

        return  nlist;
    }
    private void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

