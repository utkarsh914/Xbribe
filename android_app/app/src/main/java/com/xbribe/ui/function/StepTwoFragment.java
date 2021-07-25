package com.xbribe.ui.function;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xbribe.R;
import com.xbribe.data.AppDataManager;
import com.xbribe.ui.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class StepTwoFragment extends Fragment
{
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;
    private static final int PICK_VIDEO_REQUEST = 3;

    private  String name,city,pincode,ministryId,department,description,address,latitude,longitude,officialName,ministry;

    private ArrayList<Uri> imageList = new ArrayList<Uri>();
    private ArrayList<Uri> audioList = new ArrayList<Uri>();
    private ArrayList<Uri> videoList = new ArrayList<Uri>();

    private ArrayList<String> imageURL = new ArrayList<String>();
    private ArrayList<String> audioURL = new ArrayList<String>();
    private ArrayList<String> videoURL = new ArrayList<String>();

    List<Uri> imagepreview;

    private StorageReference mStorageRef;
    private AppDataManager appDataManager;
    private OTPVerifyFragment otpVerifyFragment;

    private DatabaseHelper databaseHelper;

    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.rview_selected)
    RecyclerView recyclerView;

    @BindView(R.id.imag_camera)
    ImageButton imgChoose;

    @BindView(R.id.imag_audio)
    ImageButton audChoose;

    @BindView(R.id.imag_video)
    ImageButton vidChoose;

    @BindView(R.id.btn_upload)
    Button upload;

    @BindView(R.id.btn_submit)
    Button submit;

    @BindView(R.id.tv_image_files_no)
    TextView tvImgNo;

    @BindView(R.id.tv_audio_files_no)
    TextView tvAudNo;

    @BindView(R.id.tv_video_files_no)
    TextView tvVidNo;

    ImagePreviewAdapter imagePreviewAdapter;
    SubmissionActivityViewModel submissionActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent=inflater.inflate(R.layout.fragment_step_two,container,false);
        ButterKnife.bind(this,parent);
        submissionActivityViewModel=ViewModelProviders.of(getActivity()).get(SubmissionActivityViewModel.class);
        appDataManager = ((MyApplication) getActivity().getApplication()).getDataManager();

        imgChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openImageChoose();
            }
        });

        audChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openAudioChoose();
            }
        });

        vidChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoChoose();
            }
        });

        return parent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseHelper=new DatabaseHelper(getActivity());
        databaseHelper.getWritableDatabase();
        ministry = getArguments().getString("MINISTRY");
        ministryId=getArguments().getString("MINISTRYID");
        department=getArguments().getString("DEPARTMENT");
        name=getArguments().getString("ORGANISATION");
        officialName=getArguments().getString("OFFICIAL");
        city=getArguments().getString("CITY");
        pincode=getArguments().getString("PINCODE");
        description=getArguments().getString("DESCRIPTION");
        address=getArguments().getString("ADDRESS");
        latitude=getArguments().getString("LATITUDE");
        longitude=getArguments().getString("LONGITUDE");

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openImageChoose()
    {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent1, PICK_IMAGE_REQUEST);
    }

    private void openAudioChoose()
    {
        Intent intent2 = new Intent();
        intent2.setType("audio/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent2, PICK_AUDIO_REQUEST);
    }

    private void openVideoChoose()
    {
        Intent intent3 = new Intent();
        intent3.setType("video/*");
        intent3.setAction(Intent.ACTION_GET_CONTENT);
        intent3.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent3, PICK_VIDEO_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            if(data.getData()!=null)
            {
                int i=0;
                Uri img = data.getData();
                imageList.add(img);
                i++;
            }
            else if(data.getClipData()!=null)
            {
                int i=0;
                while(i<data.getClipData().getItemCount())
                {
                    Uri img = data.getClipData().getItemAt(i).getUri();
                    imageList.add(img);
                    i++;
                }
            }
        }
        if(requestCode==PICK_AUDIO_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            if(data.getData()!=null)
            {
                int j=0;
                Uri aud = data.getData();
                audioList.add(aud);
                j++;
            }
            else if(data.getClipData()!=null)
            {
                int j=0;
                while(j<data.getClipData().getItemCount())
                {
                    Uri aud = data.getClipData().getItemAt(j).getUri();
                    audioList.add(aud);
                    j++;
                }
            }
        }

        if(requestCode==PICK_VIDEO_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            if(data.getData()!=null)
            {
                int k=0;
                Uri vid = data.getData();
                videoList.add(vid);
                k++;
            }
            else if(data.getClipData()!=null)
            {
                int k=0;
                while(k<data.getClipData().getItemCount())
                {
                    Uri vid = data.getClipData().getItemAt(k).getUri();
                    videoList.add(vid);
                    k++;
                }
            }
        }

        tvImgNo.setText(String.valueOf(imageList.size()));
        tvAudNo.setText(String.valueOf(audioList.size()));
        tvVidNo.setText(String.valueOf(videoList.size()));

        imagepreview=new ArrayList<>();
        for(int i=0;i<imageList.size();i++)
        {
            imagepreview.add(imageList.get(i));
        }
        imagePreviewAdapter=new ImagePreviewAdapter(getContext(),imagepreview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(imagePreviewAdapter);
    }
    private void uploadImage()
    {

        for(int i=0;i<imageList.size();i++)
        {
            Uri mImageUri = imageList.get(i);
            if(mImageUri!=null)
            {
                StorageReference imageReference = mStorageRef.child("images/").child(appDataManager.getID()+"/"+System.currentTimeMillis()+"."+getFileExtension(mImageUri));
                UploadTask imageTask = imageReference.putFile(mImageUri);
                imageTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        Task<Uri> urlTask = imageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return imageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    hideProgress();
                                    String msg="Uploaded Image Successfully";
                                    showSnackbar(msg);
                                    Uri downloadUri = task.getResult();
                                    String imgURL = downloadUri.toString();
                                    imageURL.add(imgURL);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                        String msg="Not  Uploaded";
                        showSnackbar(msg);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        showProgress();
                    }
                });
            }
            else
            {
                String msg="No File Selected";
                showSnackbar(msg);
            }
        }
        imageList.clear();
    }

    private void uploadAudio()
    {

        for(int i=0;i<audioList.size();i++)
        {
            Uri mAudioUri = audioList.get(i);
            if(mAudioUri!=null)
            {
                StorageReference audioReference = mStorageRef.child("audio/").child(appDataManager.getID()+"/"+System.currentTimeMillis()+"."+getFileExtension(mAudioUri));
                UploadTask audioTask = audioReference.putFile(mAudioUri);
                audioTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = audioTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return audioReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                hideProgress();
                                if (task.isSuccessful()) {
                                    hideProgress();
                                    String msg="Uploaded Audio Successfully";
                                    showSnackbar(msg);
                                    Uri downloadUri = task.getResult();
                                    String audURL = downloadUri.toString();
                                    audioURL.add(audURL);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                        String msg="Not Uploaded";
                        showSnackbar(msg);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        showProgress();
                    }
                });
            }
            else
            {
                String msg="No File Selected";
                showSnackbar(msg);
            }
        }
        audioList.clear();
    }

    private void uploadVideo()
    {
        for(int i=0;i<videoList.size();i++)
        {
            Uri mVideoUri = videoList.get(i);
            if(mVideoUri!=null)
            {
                StorageReference videoReference = mStorageRef.child("video/").child(appDataManager.getID()+"/"+System.currentTimeMillis()+"."+getFileExtension(mVideoUri));
                UploadTask videoTask = videoReference.putFile(mVideoUri);
                videoTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = videoTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return videoReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    hideProgress();
                                    String msg="Uploaded Video Successfully";
                                    showSnackbar(msg);
                                    Uri downloadUri = task.getResult();
                                    String vidURL = downloadUri.toString();
                                    videoURL.add(vidURL);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                        String msg="Not uploaded";
                        showSnackbar(msg);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        showProgress();
                    }
                });
            }
            else
            {
                String msg="No File Selected";
                showSnackbar(msg);
            }
        }
        videoList.clear();
    }

    @OnClick(R.id.btn_upload)
    void upload()
    {
        uploadImage();
        uploadAudio();
        uploadVideo();
    }

    @OnClick(R.id.btn_submit)
    void submit()
    {
        Bundle bundle = new Bundle();
        bundle.putString("MINISTRY",ministry);
        bundle.putString("MINISTRYID",ministryId);
        bundle.putString("DEPARTMENT",department);
        bundle.putString("ORGANISATION",name);
        bundle.putString("OFFICIAL",officialName);
        bundle.putString("CITY",city);
        bundle.putString("PINCODE",pincode);
        bundle.putString("DESCRIPTION",description);
        bundle.putString("ADDRESS",address);
        bundle.putString("LATITUDE",latitude);
        bundle.putString("LONGITUDE",longitude);
        bundle.putStringArrayList("IMGARRAY",imageURL);
        bundle.putStringArrayList("AUDARRAY",audioURL);
        bundle.putStringArrayList("VIDARRAY",videoURL);

        otpVerifyFragment = new OTPVerifyFragment();
        otpVerifyFragment.setArguments(bundle);
        submissionActivityViewModel.setSendOtp();
        submissionActivityViewModel.getSendOtp().observe(this, data->{
            if(data!=null)
            {
                Toast.makeText(getActivity(),"Please check your email.",Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame_two,otpVerifyFragment)
                        .commit();
            }
            else
            {
                String msg = "Please try later!";
                showSnackbar(msg);
            }
        });
    }


    public void showSnackbar(String msg)
    {
        Snackbar snackbar= Snackbar.make(constraintLayout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showProgress() {
        pbProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbProgress.setVisibility(View.GONE);
    }
}
