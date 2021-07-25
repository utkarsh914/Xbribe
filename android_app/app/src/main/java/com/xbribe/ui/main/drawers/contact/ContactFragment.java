package com.xbribe.ui.main.drawers.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xbribe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactFragment extends Fragment
{
    @BindView(R.id.btn_connect_nandini)
    Button nandini_link;

    @BindView(R.id.btn_connect_rishi)
    Button  rishi_link;

    String url[]={"https://www.linkedin.com/in/rishiraj-paul-chowdhury-825419171/","https://www.linkedin.com/in/nandini-agarwal-889217189/"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent=inflater.inflate(R.layout.fragment_contact_us,container,false);
        ButterKnife.bind(this,parent);
        nandini_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link(url[1]);
            }
        });
        rishi_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link(url[0]);
            }
        });
        return parent;

    }
    private void link(String url)
    {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
