package com.xbribe.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraFragment;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraFocus;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.google.android.material.button.MaterialButton;
import com.xbribe.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecretCamera extends HiddenCameraFragment{

    @BindView(R.id.img_view_clicked)
    ImageView imgViewClicked;

    @BindView(R.id.btn_take_pic)
    MaterialButton takePic;

    @BindView(R.id.btn_save_pic)
    MaterialButton savePic;

    @BindView(R.id.pb_progress_img)
    ProgressBar pbImg;

    @BindView(R.id.tv_img_process)
    TextView imgProcess;

    private CameraConfig mCameraConfig;
    private Bitmap imgBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_secret_camera,container,false);
        ButterKnife.bind(this,parent);

        mCameraConfig = new CameraConfig()
                .getBuilder(getActivity())
                .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                .setImageFormat(CameraImageFormat.FORMAT_PNG)
                .setCameraFocus(CameraFocus.AUTO)
                .setImageRotation(CameraRotation.ROTATION_90)
                .build();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera(mCameraConfig);
        }

        savePic.setEnabled(false);
        return parent;
    }

    @Override
    public void onStart() {
        mCameraConfig = new CameraConfig()
                .getBuilder(getActivity())
                .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                .setCameraFocus(CameraFocus.AUTO)
                .setImageRotation(CameraRotation.ROTATION_90)
                .build();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera(mCameraConfig);
        }
        super.onStart();
    }

    @OnClick(R.id.btn_take_pic)
    void setTakePic()
    {
        showProgress();
        imgViewClicked.setVisibility(View.INVISIBLE);
        imgProcess.setVisibility(View.VISIBLE);
        takePicture();
        savePic.setEnabled(true);
    }

    @OnClick(R.id.btn_save_pic)
    void setSavePic()
    {
        try{
             File storageDir = new File(Environment.getExternalStorageDirectory(), "XBribe");

             if (!storageDir.exists()) {
                 if (!storageDir.mkdirs()) {
                     Log.d("App", "Failed to create directory");
                 }
             }
            String timeStamp = new SimpleDateFormat("MMdd_HHmm").format(new Date());
            String filename = "img"+timeStamp+".jpg";
            File outFile = new File(storageDir, filename);
            FileOutputStream outputStream = new FileOutputStream(outFile);
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Toast.makeText(getActivity(), "Image Saved", Toast.LENGTH_SHORT).show();
            savePic.setEnabled(false);
        }
        catch (Exception e) {
            Log.e("Image Save", e.getMessage());
        }
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        imgProcess.setVisibility(View.INVISIBLE);
        imgViewClicked.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        imgBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        hideProgress();
        //Display the image to the image view
        imgViewClicked.setImageBitmap(imgBitmap);
    }

    @Override
    public void onStop() {
        stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopCamera();
        super.onDestroy();
    }

    @Override
    public void onCameraError(int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                Toast.makeText(getActivity(),"Cannot open camera! Please reopen screen.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(getActivity(),"Cannot save image! Please reopen screen.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(getActivity(),"Cannot get camera permission!", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(getContext());
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(getContext(), "Front camera not present!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void showProgress() {
        pbImg.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbImg.setVisibility(View.INVISIBLE);
    }
}
