package com.example.stencrypt;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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

import com.example.stencrypt.steganography.ImageSteganography;
import com.example.stencrypt.steganography.TextDecoding;
import com.example.stencrypt.steganography.TextDecodingCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;


public class DecodeFragment extends Fragment implements TextDecodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";
    private TextView textView;
    private ImageView imageView;
    private EditText message;
    private EditText secret_key;
    private Uri filepath;
    int resultOk;
    ContentResolver contentResolver;


    private Bitmap original_image;
    public DecodeFragment(int resultOk, ContentResolver contentResolver) {
        this.resultOk = resultOk;
        this.contentResolver = contentResolver;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.logT_dc);

        imageView = view.findViewById(R.id.iv_encode_dc);

        message = view.findViewById(R.id.message_et_dc);
        secret_key = view.findViewById(R.id.key_et_dc);

        Button choose_image_button = view.findViewById(R.id.btn_choose_dc);
        Button decode_button = view.findViewById(R.id.btn_encode_dc);

        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null) {

                    ImageSteganography imageSteganography = new ImageSteganography(secret_key.getText().toString(),
                            original_image);

                    TextDecoding textDecoding = new TextDecoding(getActivity(),DecodeFragment.this);

                    textDecoding.execute(imageSteganography);
                }
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Image set to imageView
        if (requestCode == SELECT_PICTURE && resultCode == resultOk && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(contentResolver, filepath);

                imageView.setImageBitmap(original_image);
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }

    }

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
                    textView.setText("Decoded");
                    message.setText("" + result.getMessage());
                } else {
                    textView.setText("Wrong secret key");
                }
            }
        } else {
            textView.setText("Select Image First");
        }


    }
}