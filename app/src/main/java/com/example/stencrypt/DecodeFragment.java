package com.example.stencrypt;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.stencrypt.steganography.ImageSteganography;
import com.example.stencrypt.steganography.TextDecoding;
import com.example.stencrypt.steganography.TextDecodingCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;


public class DecodeFragment extends Fragment implements TextDecodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";
    private TextView textView;
    private ImageView imageView;
    private Uri filepath;
    int resultOk;
    ContentResolver contentResolver;
    Context applicationContext;
    LottieAnimationView lottieUpload;
    AlertDialog materialDialogs;


    private Bitmap original_image;
    public DecodeFragment(int resultOk, ContentResolver contentResolver, Context applicationContext) {
        this.resultOk = resultOk;
        this.contentResolver = contentResolver;
        this.applicationContext = applicationContext;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imageView = view.findViewById(R.id.iv_decode);
        lottieUpload = view.findViewById(R.id.lottieUploadDecode);

        Button choose_image_button = view.findViewById(R.id.btn_share_decode);
        Button decode_button = view.findViewById(R.id.btn_decode);

        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog obj = new BottomSheetDialog("decode");
                obj.show(getParentFragmentManager(),"");
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_decode, container, false);
    }
    private void ImageChooser() {
        lottieUpload.setVisibility(View.GONE);
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLaunchncher.launch(i);
    }
    ActivityResultLauncher<Intent> someActivityResultLaunchncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                   if(result.getResultCode() == Activity.RESULT_OK){
                       filepath = result.getData().getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(contentResolver, filepath);
                imageView.setImageBitmap(original_image);
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
                   }
                }
            }
    );
    @Override
    public void onStartTextEncoding() {

    }
    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        if (result != null) {
            if (!result.isDecoded()) {
                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "No message found", Snackbar.LENGTH_LONG);
                snackBar.show();
            }
            else {
                if (!result.isSecretKeyWrong()) {
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),"Successfully decoded", Snackbar.LENGTH_LONG);
                    snackBar.show();

                   materialDialogs =  new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Decoded message from image")
                            .setMessage(result.getMessage())
                            .setNegativeButton("close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();

                } else {
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),"Wrong secret key", Snackbar.LENGTH_LONG);
                    snackBar.show();                }
            }
        } else {
            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Select image first", Snackbar.LENGTH_LONG);
            snackBar.show();        }
    }
    public void imageOpener(){
        ImageChooser();
    }
    public void getTextDataDecode(String key){
        if(filepath != null) {
            ImageSteganography imageSteganography = new ImageSteganography(key, original_image);
            TextDecoding textDecoding = new TextDecoding(getActivity(),DecodeFragment.this);
            textDecoding.execute(imageSteganography);
        }
    }
}