package com.xbribe.ui.main.drawers.checkcase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xbribe.R;
import com.xbribe.data.models.CollecImages;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckCaseAdapter extends RecyclerView.Adapter<CheckCaseAdapter.ViewHolder>
{
    @BindView(R.id.tv_no_cases)
    TextView no_cases;
    Context context;
    List<CheckcaseModel>  checklist;

    public CheckCaseAdapter(Context context, List<CheckcaseModel> checklist) {
        this.context = context;
        this.checklist = checklist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkdesc_case,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Glide.with(context).load(checklist.get(position).getCrimeimage()).into(holder.imageView);
        holder.ministry.setText(checklist.get(position).getMinistry());
        holder.department.setText(checklist.get(position).getDepartment());
        holder.organization_name.setText(checklist.get(position).getName_organization());
        holder.oranization_desc.setText(checklist.get(position).getCase_desc());
        holder.location.setText(checklist.get(position).getLocation());
        holder.imageno.setText(checklist.get(position).getImagesno());
        holder.audiono.setText(checklist.get(position).getAudiono());
        holder.videono.setText(checklist.get(position).getVideono());
        holder.officialName.setText(checklist.get(position).getOfficialName());
    }

    @Override
    public int getItemCount()
    {

        return checklist.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tv_ministry_name)
        TextView ministry;
        @BindView(R.id.tv_department)
        TextView department;
        @BindView(R.id.tv_organisation_name)
        TextView organization_name;
        @BindView(R.id.tv_official_name)
        TextView officialName;
        @BindView(R.id.tv_location)
        TextView location;
        @BindView(R.id.image_crime)
        ImageView imageView;
        @BindView(R.id.tv_case_description)
        TextView oranization_desc;
        @BindView(R.id.tv_images_no)
        TextView imageno;
        @BindView(R.id.tv_audio_no)
        TextView audiono;
        @BindView(R.id.tv_video_no)
        TextView videono;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}
