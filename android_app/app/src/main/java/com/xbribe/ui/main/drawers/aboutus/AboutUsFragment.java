package com.xbribe.ui.main.drawers.aboutus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsFragment extends Fragment
{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<AboutUsModel> models;
    AboutUsAdapter aboutUsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent=inflater.inflate(R.layout.fragment_about_app,container,false);
        ButterKnife.bind(this,parent);


        models=new ArrayList<>();
        models.add(new AboutUsModel(R.drawable.rishiraj,"Rishiraj Paul Choudhury","Android Developer"));
        models.add(new AboutUsModel(R.drawable.nandini,"Nandini Agarwal","Android Developer"));
        models.add(new AboutUsModel(R.drawable.utkarsh,"Utkarsh Tiwari","Web developer"));
        models.add(new AboutUsModel(R.drawable.pratik,"Pratik Sharma","Web developer"));
        models.add(new AboutUsModel(R.drawable.monalisa,"Monalisa Nath","Graphic Designer"));
        models.add(new AboutUsModel(R.drawable.saranga,"Saranga Mahanta","Data Scientist"));

        initRecyclerView();
        return parent;
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        aboutUsAdapter = new AboutUsAdapter(getContext(),models);
        recyclerView.setAdapter(aboutUsAdapter);
    }

}
