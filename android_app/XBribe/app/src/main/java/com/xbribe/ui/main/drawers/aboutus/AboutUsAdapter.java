package com.xbribe.ui.main.drawers.aboutus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.ImageViewHolder>
{
    Context context;
    List<AboutUsModel> llist;

    public AboutUsAdapter(Context context, List<AboutUsModel> llist)
    {
        this.context = context;
        this.llist = llist;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aboutusfragment,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position)
    {

        holder.member_photo.setImageResource(llist.get(position).getImage());
        holder.member_name.setText(llist.get(position).getMember_name());
        holder.member_work.setText(llist.get(position).getMember_work());

    }

    @Override
    public int getItemCount() {
        return llist.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imag_members)
        ImageView member_photo;
        @BindView(R.id.tv_member_name)
        TextView member_name;
        @BindView(R.id.tv_member_work)
        TextView member_work;


        public ImageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
