package com.xbribe.ui.main.drawers.drafts;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.CaptivePortal;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import com.xbribe.data.AppDataManager;
import com.xbribe.ui.MyApplication;
import com.xbribe.ui.function.Step_two_Fragment;
import com.xbribe.ui.function.SubmissionActivity;
import com.xbribe.ui.main.drawers.checkcase.CheckcaseModel;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DraftFragment extends Fragment
{
    @BindView(R.id.recycler_checkdrafts)
    RecyclerView recyclerView;

    @BindView(R.id.tv_no_drafts)
    TextView noDrafts;
    AppDataManager appDataManager;

    Cursor  cursor;
    List<DraftModel> draftModelList;
    DraftAdapter draftAdapter;
    DatabaseSaveDraft databaseSaveDraft;
    int flag=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent=inflater.inflate(R.layout.fragment_drafts,container,false);
        ButterKnife.bind(this,parent);
        databaseSaveDraft=new DatabaseSaveDraft(getActivity());
        databaseSaveDraft.getWritableDatabase();
        appDataManager = ((MyApplication) getActivity().getApplicationContext()).getDataManager();
        initrecycleradapter();
        return parent;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int id=Integer.parseInt(draftModelList.get(position).getId());
            goToSteptwo(id);
        }
    };

    private void goToSteptwo(int position)
    {
        Bundle bundle=new Bundle();
        cursor = databaseSaveDraft.getrowdetails(position);
        if (cursor.moveToFirst())
        {
            do {
                bundle.putString("MINISTRY", cursor.getString(1));
                bundle.putString("MINISTRYID", cursor.getString(12));
                bundle.putString("DEPARTMENT", cursor.getString(5));
                bundle.putString("ORGANISATION", cursor.getString(6));
                bundle.putString("OFFICIAL",cursor.getString(11));
                bundle.putString("CITY", cursor.getString(4));
                bundle.putString("PINCODE", cursor.getString(3));
                bundle.putString("DESCRIPTION", cursor.getString(7));
                bundle.putString("ADDRESS",cursor.getString(2));
                bundle.putString("LATITUDE",cursor.getString(9));
                bundle.putString("LONGITUDE",cursor.getString(10));

             }
            while (cursor.moveToNext());
        }

        Intent intent = new Intent(getActivity(),SubmissionActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("Fragment","Step 2");
        getActivity().startActivity(intent);
    }

    private void initrecycleradapter()
    {
        cursor=databaseSaveDraft.getAllDetails();

        if(cursor.getCount()==0)
        {
            noDrafts.setVisibility(View.VISIBLE);
        }
        else
        {
            draftAdapter =new DraftAdapter(uploadlist(),getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(draftAdapter);
            draftAdapter.setOnItemClickListener(onClickListener);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
                {
                    String s =draftModelList.get(viewHolder.getAdapterPosition()).getId();
                    databaseSaveDraft.deleteData(s);
                    Toast.makeText(getActivity(), "Draft Deleted.", Toast.LENGTH_SHORT).show();
                }
            });

            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }
    private  List<DraftModel> uploadlist()
    {
        draftModelList=new ArrayList<>();
       cursor.moveToPosition(cursor.getCount());
        while (cursor.moveToPrevious())
        {
            if(cursor.getString(8).equals(appDataManager.getEmail()))
            {
                draftModelList.add(new DraftModel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(11),cursor.getString(7),"delete_draft",cursor.getString(0),cursor.getString(12)));
                noDrafts.setVisibility(View.INVISIBLE);
                flag=1;
            }
        }
        if(flag!=1)
        {
            noDrafts.setVisibility(View.VISIBLE);
        }
        return draftModelList;
    }
}

