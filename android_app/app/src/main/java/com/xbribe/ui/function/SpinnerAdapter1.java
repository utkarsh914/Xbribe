package com.xbribe.ui.function;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xbribe.R;
import com.xbribe.data.models.Organizations;

import java.util.ArrayList;

public class SpinnerAdapter1 extends ArrayAdapter<Organizations> {

    public SpinnerAdapter1(Context context, ArrayList<Organizations> organizationsData)
    {
        super(context, 0 ,organizationsData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_ministry,parent,false);
        }

        TextView tvOrgName = convertView.findViewById(R.id.stv_ministry);
        Organizations currentOrganization = getItem(position);

        if(currentOrganization!=null)
        {
            tvOrgName.setText(currentOrganization.getMinistry());
            tvOrgName.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}
