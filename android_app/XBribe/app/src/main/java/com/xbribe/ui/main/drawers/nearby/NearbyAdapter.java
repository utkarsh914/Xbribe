package com.xbribe.ui.main.drawers.nearby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;
import com.xbribe.data.models.NearbyCaseResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyViewHolder> {

    private List<NearbyCaseResponse> mNearbyCases;
    private Context mContext;

    public NearbyAdapter(List<NearbyCaseResponse> mNearbyCases, Context mContext) {
        this.mNearbyCases = mNearbyCases;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearbycases,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.orgName.setText(mNearbyCases.get(position).getOrganizationName());
        holder.addr.setText(mNearbyCases.get(position).getLocation().getAddress());
        holder.city.setText(mNearbyCases.get(position).getCity());
        holder.pin.setText(mNearbyCases.get(position).getLocation().getPin());
    }

    @Override
    public int getItemCount() {
        return mNearbyCases.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_orgName)
        TextView orgName;

        @BindView(R.id.tv_addr)
        TextView addr;

        @BindView(R.id.tv_city)
        TextView city;

        @BindView(R.id.tv_pin)
        TextView pin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
