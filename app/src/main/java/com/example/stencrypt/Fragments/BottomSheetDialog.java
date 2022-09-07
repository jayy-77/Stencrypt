package com.example.stencrypt.Fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.stencrypt.Activities.HomePage;
import com.example.stencrypt.Utilities.DBHelper;
import com.example.stencrypt.R;
import com.example.stencrypt.Utilities.RSA;
import com.example.stencrypt.DataModel.StenShareObject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    String whichOne = null, key = null, bitString, currentDate, currentTime, rPublicKey;
    StenShareObject obj = null;
    Bitmap bitmap = null;
    private DBHelper mydb ;

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
        void onDecodeButtonClicked(String key,int flag);
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
                mListener.onDecodeButtonClicked(ecdc_key_et.getText().toString(),0);
                dismiss();
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }
        });
        MaterialButton biometrics = v.findViewById(R.id.bioDecodeBtn);
        biometrics.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                ((HomePage) getContext()).bioMetrics();
                mListener.onDecodeButtonClicked(null,1);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                 currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                 currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                 db.collection("UserDetails").document(email_et.getText().toString().trim()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {
                         rPublicKey = documentSnapshot.getString("publicKey");
                         RSA objAlgo = new RSA(rPublicKey);
                         try {
                             obj = new StenShareObject(bitString,objAlgo.Encrypt(key,rPublicKey),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),email_et.getText().toString().trim(),currentDate,currentTime);
                             db.collection("UserDetails").document(email_et.getText().toString().trim()).collection("StenShare").add(obj);
                         } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchProviderException e) {
                             e.printStackTrace();
                         }
                     }
                 });
            dismiss();
                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Successfully Sent", Snackbar.LENGTH_LONG);
                snackBar.show();
            }
        });
    }
}
