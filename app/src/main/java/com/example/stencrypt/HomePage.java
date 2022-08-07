package com.example.stencrypt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class HomePage extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {
    FloatingActionButton userProfile;
    EncodeFragment encodeFragment;
    Fragment frag = null;
    BottomAppBar bottomAppBar;
    Boolean bool = false;
    FrameLayout fragL;
    GoogleSignInClient mGoogleSignInClient;
    ContentResolver contentResolver;
    Context context;
    private DBHelper dbS;
    EncodeFragment fragment;
    String key, messagae, whichFragment = "userProfile";
    MaterialToolbar materialToolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private CollectionReference userDetailRef = db.collection("UserDetails");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        userProfile  = findViewById(R.id.userProfile);
        fragL = findViewById(R.id.flFragment);
        materialToolbar = findViewById(R.id.mToolbarDefaultSize_AppBarTopActivity);
        contentResolver = getContentResolver();
        context = getApplicationContext();
        fragment  = new EncodeFragment();


        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(whichFragment.equals("encode")){
                    ((EncodeFragment) frag).imageOpener();
                }else if(whichFragment.equals("decode")){
                    ((DecodeFragment) frag).imageOpener();
                }
                else if(whichFragment.equals("userProfile")) {
                    frag = new UserProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, frag).commit();
                    menuSettings();
                }
            }
        });
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool == true){
                    frag = new HomeFragment(RESULT_OK, getContentResolver());
                    menuAlternateSetting();
                    userProfile.setImageResource(R.drawable.person);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,frag,"").commit();
                }else{
                    Toast.makeText(getApplicationContext(), "network", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.gLogout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),GoogleLoginActivity.class));
                        finish();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return true;
            }
        });

        frag = new HomeFragment(RESULT_OK, getContentResolver());
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
    public void openEncodeFragment(){
        whichFragment = "encode";
        frag = new EncodeFragment(RESULT_OK,contentResolver,context);
        menuSettings();
        userProfile.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,frag,"").commit();
        materialToolbar.setTitle("ENCODE");
    }
    public void openDecodeFragment(){
        whichFragment = "decode";
        frag = new DecodeFragment(RESULT_OK,contentResolver,context);
        menuSettings();
        userProfile.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,frag,"").commit();
        materialToolbar.setTitle("DECODE");
    }

    @Override
    public void onButtonClicked(String key,String message) {
        this.key = key;
        this.messagae = message;

            ((EncodeFragment) frag).getTextData(key, messagae);


            Toast.makeText(getApplicationContext(), "data transfered", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDecodeButtonClicked(String key) {
        this.key = key;
        ((DecodeFragment) frag).getTextDataDecode(key);
    }
}