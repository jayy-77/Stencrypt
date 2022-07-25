package com.example.stencrypt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stencrypt.steganography.ImageSteganography;
import com.example.stencrypt.steganography.TextEncoding;
import com.example.stencrypt.steganography.TextEncodingCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EncodeFragment extends Fragment implements TextEncodingCallback {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Encode Class";
    private TextView whether_encoded;
    private ImageView imageView;
    private EditText message;
    private EditText secret_key;
    private TextEncoding textEncoding;
    private ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Uri filepath;
    private Bitmap original_image;
    private Bitmap encoded_image;
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
        whether_encoded = view.findViewById(R.id.logT);

        imageView = view.findViewById(R.id.iv_encode);

        message = view.findViewById(R.id.message_et);
        secret_key = view.findViewById(R.id.key_et);

        Button choose_image_button = view.findViewById(R.id.btn_choose);
        Button encode_button = view.findViewById(R.id.btn_encode);
        Button save_image_button = view.findViewById(R.id.btn_save);

        checkAndRequestPermissions();


        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                whether_encoded.setText("");
//                if (filepath != null) {
//                    if (message.getText() != null) {
//                        imageSteganography = new ImageSteganography(message.getText().toString(),
//                                secret_key.getText().toString(),
//                                original_image);
//                        textEncoding = new TextEncoding(getActivity(),EncodeFragment.this);
//                        textEncoding.execute(imageSteganography);
//                    }
//                }
                BottomSheetDialog  obj = new BottomSheetDialog();

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
                        saveToInternalStorage(imgToSave);
                    }
                });
                save = new ProgressDialog(getContext());
                save.setMessage("Saving, Please Wait...");
                save.setTitle("Saving Image");
                save.setIndeterminate(false);
                save.setCancelable(false);
                save.show();
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {


        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            whether_encoded.setText("Encoded");
            imageView.setImageBitmap(encoded_image);
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Encoded" + ".PNG"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            whether_encoded.post(new Runnable() {
                @Override
                public void run() {
                    save.dismiss();
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
        whether_encoded.setText(key+"\n"+message);
    }
}
