package com.example.stencrypt;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

public class HomeFragment extends Fragment {
    ImageView encode,decode;
    SeekBar sliderEncodeDecode;
    Fragment frag;
    String fragmentName;
    int resultOk;
    ContentResolver contentResolver;
    Context applicationContext;

    public HomeFragment(int resultOk, ContentResolver contentResolver) {
        this.resultOk = resultOk;
        this.contentResolver = contentResolver;
        this.applicationContext = applicationContext;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderEncodeDecode =view.findViewById(R.id.seekBar);
        encode = view.findViewById(R.id.imageView);
        decode = view.findViewById(R.id.imageView2);
        sliderEncodeDecode.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//               Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                if(i>57 ){
                    encode.setImageResource(R.drawable.round_encode_green);
                    ((HomePage)getActivity()).openEncodeFragment();
                    ((HomePage)getActivity()).menuSettingEncode();
                }
                if(i<=57 && i>=43){
                    decode.setImageResource(R.drawable.round_encode);
                    encode.setImageResource(R.drawable.round_encode);
                    sliderEncodeDecode.setProgress(50);
                }
                if(i<43 ){
                    decode.setImageResource(R.drawable.round_encode_green);
                    ((HomePage)getActivity()).openDecodeFragment();
                    ((HomePage)getActivity()).menuSettingEncode();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }
}