package com.xbribe.ui.main;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.xbribe.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecretFragment extends Fragment {
    private MediaRecorder myAudioRecorder;
    private String outputFile;

    private SecretCamera secretCameraFragment;

    @BindView(R.id.btn_record)
    MaterialButton record;

    @BindView(R.id.btn_stop)
    MaterialButton stop;

    @BindView(R.id.img_off)
    ImageView imgOff;

    @BindView(R.id.img_on)
    ImageView imgOn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_secret,container,false);
        ButterKnife.bind(this,parent);

        secretCameraFragment = new SecretCamera();
        stop.setEnabled(false);
        return parent;
    }

    @OnClick(R.id.btn_record)
    void startRecord()
    {
        imgOn.setVisibility(View.VISIBLE);
        imgOff.setVisibility(View.INVISIBLE);
        startRecording();
        Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_stop)
    void stopRecord()
    {
        imgOn.setVisibility(View.INVISIBLE);
        imgOff.setVisibility(View.VISIBLE);
        stopRecording();
        Toast.makeText(getActivity(), "Audio Recorded successfully", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_open_scamera)
    void openSecretCamera()
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame,secretCameraFragment)
                    .addToBackStack("Secret Fragment")
                    .commit();
    }

    private void startRecording()
    {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "XBribe");

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d("App", "Failed to create directory");
            }
        }
        try {
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "XBribe" + File.separator + "aud"+timeStamp+".amr";
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            record.setEnabled(false);
            stop.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording()
    {
        try{
            myAudioRecorder.stop();
            myAudioRecorder.reset();
            myAudioRecorder.release();
            record.setEnabled(true);
            stop.setEnabled(false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
