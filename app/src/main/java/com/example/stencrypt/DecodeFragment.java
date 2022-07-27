package com.example.stencrypt;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
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
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.stencrypt.steganography.ImageSteganography;
import com.example.stencrypt.steganography.TextDecoding;
import com.example.stencrypt.steganography.TextDecodingCallback;

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

        Button choose_image_button = view.findViewById(R.id.btn_choose_decode);
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //Image set to imageView
//        if (requestCode == SELECT_PICTURE && resultCode == resultOk && data != null && data.getData() != null) {
//
//            filepath = data.getData();
//            try {
//                original_image = MediaStore.Images.Media.getBitmap(contentResolver, filepath);
//
//                imageView.setImageBitmap(original_image);
//            } catch (IOException e) {
//                Log.d(TAG, "Error : " + e);
//            }
//        }
//
//    }

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do by the start of textDecoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textDecoding

        if (result != null) {
            if (!result.isDecoded())
                textView.setText("No message found");
            else {
                if (!result.isSecretKeyWrong()) {
//                    textView.setText("Decoded");
                    Toast.makeText(applicationContext, result.getMessage(), Toast.LENGTH_SHORT).show();
//                    message.setText("" + result.getMessage());
                } else {
//                    textView.setText("Wrong secret key");
                }
            }
        } else {
//            textView.setText("Select Image First");
        }
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