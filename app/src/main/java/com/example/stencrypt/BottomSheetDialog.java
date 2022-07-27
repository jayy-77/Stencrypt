package com.example.stencrypt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    String whichOne = null;
    View v;
    public BottomSheetDialog(String whichOne){
        this.whichOne = whichOne;
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
}
