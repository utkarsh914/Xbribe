package com.xbribe.ui.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.CamcorderProfile.get;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder>
{
    Context context;
    List<Uri> imageModelList;


    public ImagePreviewAdapter(Context context, List<Uri> imageModelList)
    {
        this.context = context;
        this.imageModelList = imageModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagepreview,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImagePreviewAdapter.ViewHolder holder, int position)
    {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(),imageModelList.get(position));
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            holder.imagepreview.setImageBitmap(resizedBitmap);
             }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount()
    {
        return  imageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imag_preview)
        ImageView imagepreview;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
