package com.example.stencrypt;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    String whichOne = null, key = null, bitString;
    Bitmap bitmap = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    View v;
    public BottomSheetDialog(String whichOne){
        this.whichOne = whichOne;
    }
    public BottomSheetDialog(Bitmap bitmap, String whichOne, String key){
        this.bitmap = bitmap;
        this.whichOne = whichOne;
        this.key = key;
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        bitString = Base64.encodeToString(b, Base64.DEFAULT);
    }
    private BottomSheetListener mListener;
    private TextInputEditText ecdc_key_et, ecdc_message_et;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(whichOne.equals("encode")) {
            v = inflater.inflate(R.layout.bottom_sheet, container, true);
            encodeBottomSheet();
        }else if(whichOne.equals("decode")){
            v = inflater.inflate(R.layout.bottom_sheet_decode,container,false);
            decodeBottomSheet();

        }else if(whichOne.equals("comm")){
            v = inflater.inflate(R.layout.bottom_sheet_comm,container,false);
            comm();
        }
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String key, String message);
        void onDecodeButtonClicked(String key);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement BottomSheetListener");
        }
    }

    public void encodeBottomSheet(){
        MaterialButton encode_button = v.findViewById(R.id.encode_btn_bottomsheet);
        ecdc_key_et = v.findViewById(R.id.encode_key_et);
        ecdc_message_et = v.findViewById(R.id.encode_message_et);
        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked(ecdc_key_et.getText().toString(), ecdc_message_et.getText().toString());
                dismiss();
            }
        });
    }
    public void decodeBottomSheet(){
        MaterialButton decode_button = v.findViewById(R.id.decode_btn_bottomsheet);
        ecdc_key_et = v.findViewById(R.id.decode_key_et);
        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDecodeButtonClicked(ecdc_key_et.getText().toString());
                dismiss();
            }
        });
    }
    public void comm(){
        Button stenShare = v.findViewById(R.id.stenShare);
        Button stenSend = v.findViewById(R.id.stenSend);
        EditText email_et = v.findViewById(R.id.email_et);
        LinearLayout stenContainer = v.findViewById(R.id.emailContainer);
        LinearLayout shareContainer = v.findViewById(R.id.shareContainer);
        stenShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stenContainer.setVisibility(View.VISIBLE);
                shareContainer.setVisibility(View.GONE);
            }
        });
        stenSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StenShareObject obj = new StenShareObject(bitString,key);
                db.collection("UserDetails").document(email_et.getText().toString()).collection("StenShare").document("Stens").set(obj);
            }
        });
    }
}
