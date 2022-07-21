package com.example.stencrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {
    FloatingActionButton userProfile;
    ImageView encode,decode;
    SeekBar sliderEncodeDecode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sliderEncodeDecode = findViewById(R.id.seekBar);
        encode = findViewById(R.id.imageView);
        decode = findViewById(R.id.imageView2);
       sliderEncodeDecode.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//               Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
               if(i>57 ){
                encode.setImageResource(R.drawable.round_encode_green);
                   Fragment fragment = EncodeFragment.newInstance();

                   FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

               }
               if(i<=57 && i>=43){
                   decode.setImageResource(R.drawable.round_encode);
                   encode.setImageResource(R.drawable.round_encode);
                   sliderEncodeDecode.setProgress(50);

               }
               if(i<43 ){
                   decode.setImageResource(R.drawable.round_encode_green);

               }
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
        }

    }
}