package com.example.stencrypt;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.stencrypt.steganography.ImageSteganography;
import com.example.stencrypt.steganography.TextEncoding;
import com.example.stencrypt.steganography.TextEncodingCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EncodeFragment extends Fragment implements TextEncodingCallback {
    private static final String TAG = "Encode Class";
    private ImageView imageView;
    private String key, message;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextEncoding textEncoding;
    private ImageSteganography imageSteganography;
    private ProgressDialog save;
    AlertDialog materialAlertDialogBuilder;
    private Uri filepath;
    public Bitmap original_image, encoded_image;
    LottieAnimationView lottieUpload;
    int resultOk;
    ContentResolver contentResolver;
    Context applicationContext;

    public EncodeFragment(int resultOk, ContentResolver contentResolver, Context applicationContext){
        this.resultOk = resultOk;
        this.contentResolver = contentResolver;
        this.applicationContext = applicationContext;
    }
    public EncodeFragment(){

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lottieUpload = view.findViewById(R.id.lottieUploadDecode);
        imageView = view.findViewById(R.id.iv_decode);
        Button choose_image_button = view.findViewById(R.id.btn_share_decode);
        Button encode_button = view.findViewById(R.id.btn_decode);
        Button save_image_button = view.findViewById(R.id.btn_save_decode);

        checkAndRequestPermissions();


        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog obj = new BottomSheetDialog(encoded_image,"comm",key);
                obj.show(getParentFragmentManager(),"");
            }
        });

        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog  obj = new BottomSheetDialog("encode");

                obj.show(getParentFragmentManager(),"");
            }
        });
        save_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap imgToSave = encoded_image;
                Thread PerformEncoding = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveToInternalStorage(encoded_image);
                    }
                });
              materialAlertDialogBuilder =  new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Saving Image")
                        .setMessage("Saving, Please Wait")
                        .show();
//                save = new ProgressDialog(getActivity(),R.layout.activity_google_login);
//                save.setMessage("Saving, Please Wait...");
//                save.setTitle("Saving Image");
//                save.setIndeterminate(false);
//                save.setCancelable(false);
//                save.show();

                PerformEncoding.start();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_encode, container, false);

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

                Log.d("HElowoooooooooooooooooo", String.valueOf(original_image.getByteCount()));
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

        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            imageView.setImageBitmap(encoded_image);
            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Successfully encoded", Snackbar.LENGTH_LONG);
            snackBar.show();
        }else{
            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Failed", Snackbar.LENGTH_LONG);
            snackBar.show();
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Encoded" + ".PNG");
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush();
            fOut.close();
            imageView.post(new Runnable() {
                @Override
                public void run() {
//                    save.dismiss();
                    materialAlertDialogBuilder.dismiss();
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),"Image saved", Snackbar.LENGTH_LONG);
                    snackBar.show();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkAndRequestPermissions() {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }


    public void getTextData(String key, String message){
        this.key = key;
        this.message = message;
        if (filepath != null) {
            if (message!= null) {
                imageSteganography = new ImageSteganography(message, key, original_image);
                textEncoding = new TextEncoding(getActivity(),EncodeFragment.this);
                textEncoding.execute(imageSteganography);
            }
        }
    }
    public void encodeImage(){

    }
    public void imageOpener(){
        ImageChooser();
    }
}
