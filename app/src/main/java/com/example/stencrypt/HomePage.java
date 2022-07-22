package com.example.stencrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {
    FloatingActionButton userProfile;
    Fragment frag = null;
    BottomAppBar bottomAppBar;
    Boolean bool = false;
    FrameLayout fragL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomAppBar = findViewById(R.id.bottomAppBar);
        userProfile  = findViewById(R.id.userProfile);
        fragL = findViewById(R.id.flFragment);


        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag = new UserProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, frag).commit();
                menuSettings();
            }
        });
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool == true){
                    frag = new HomeFragment();
                    menuAlternateSetting();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,frag,"").commit();
                }else{
                    Toast.makeText(getApplicationContext(), "network", Toast.LENGTH_SHORT).show();
                }
            }
        });

        frag = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,frag,"").commit();


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){

        }

    }
    public void menuSettings(){
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        bottomAppBar.replaceMenu(R.menu.alternate_menu);
        bool = true;
    }
    public void menuAlternateSetting(){
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_network_check_24);
        bottomAppBar.replaceMenu(R.menu.bottom_app_bar);
        bool = false;
    }
    public void menuSettingEncode(){
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        userProfile.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
        bool = true;
    }
}