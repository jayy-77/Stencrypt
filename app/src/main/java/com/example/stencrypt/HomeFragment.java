package com.example.stencrypt;

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

    public HomeFragment() {
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
                    EncodeFragment encodeFragment = new EncodeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragment, encodeFragment);
                    fragmentTransaction.commit();
                    ((HomePage)getActivity()).menuSettingEncode();
                }
                if(i<=57 && i>=43){
                    decode.setImageResource(R.drawable.round_encode);
                    encode.setImageResource(R.drawable.round_encode);
                    sliderEncodeDecode.setProgress(50);
                }
                if(i<43 ){
                    decode.setImageResource(R.drawable.round_encode_green);
                    DecodeFragment decodeFragment = new DecodeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragment, decodeFragment);
                    fragmentTransaction.commit();
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