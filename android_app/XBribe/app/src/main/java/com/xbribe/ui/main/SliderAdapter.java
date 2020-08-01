package com.xbribe.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.xbribe.R;

import java.util.ArrayList;

public class SliderAdapter extends  SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

private Context context;


public SliderAdapter(Context context) {
        this.context = context;
        }

@Override
public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cases, null);
        return new SliderAdapterVH(inflate);
        }

@Override
public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        switch (position) {
            case 0:
                Glide.with(context).load(R.drawable.flat_art).into(viewHolder.imageViewBackground);
                break;

            case 1:
                Glide.with(context).load(R.drawable.flow_chart).into(viewHolder.imageViewBackground);
                break;

            default:
                Glide.with(context).load(R.drawable.flowchart).into(viewHolder.imageViewBackground);
                break;

        }
}
@Override
public int getCount()
       {
        //slider view count could be dynamic size
        return 3;
        }

class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

    View itemView;
    ImageView imageViewBackground;
    TextView textViewDescription;

    public SliderAdapterVH(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.imageView);
        this.itemView = itemView;
    }
}
}
