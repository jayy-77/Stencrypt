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

    private BottomSheetListener mListener;
    private TextInputEditText enc_key_et,enc_msg_et;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);

        MaterialButton encode_button = v.findViewById(R.id.encode_btn_bottomsheet);
        enc_key_et = v.findViewById(R.id.enc_key_et);
        enc_msg_et = v.findViewById(R.id.enc_message_et);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked(enc_key_et.getText().toString(),enc_msg_et.getText().toString());
                dismiss();
            }
        });


        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String key, String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(""
                    + " must implement BottomSheetListener");
        }
    }
}
