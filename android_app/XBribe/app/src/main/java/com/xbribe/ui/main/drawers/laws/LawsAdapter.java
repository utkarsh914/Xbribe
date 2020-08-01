package com.xbribe.ui.main.drawers.laws;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LawsAdapter extends RecyclerView.Adapter<LawsAdapter.ViewHolder>
{
    private View.OnClickListener onItemClickListener;

    Context context;
    List<LawsModel> lawsModelList;
    int lastPosition= -1;


    public LawsAdapter(Context context, List<LawsModel> lawsModelList)
    {
        this.context = context;
        this.lawsModelList = lawsModelList;
    }
    public void setOnItemClickListener(View.OnClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bribelaws,parent,false);
     return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LawsAdapter.ViewHolder holder, int position)
    {
       if(holder.getAdapterPosition()>lastPosition)
       {
           Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide);
           ((ViewHolder)holder).itemView.startAnimation(animation);
           holder.lawtitle.setText(lawsModelList.get(position).getSection());
           lastPosition=holder.getAdapterPosition();
       }
    }

    @Override
    public int getItemCount()
    {
        return lawsModelList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.tv_lawtitle)
        TextView lawtitle;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setTag(this);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
