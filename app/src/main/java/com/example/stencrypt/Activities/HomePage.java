package com.example.stencrypt.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.stencrypt.Fragments.BottomSheetDialog;
import com.example.stencrypt.Utilities.DBHelper;
import com.example.stencrypt.Fragments.DecodeFragment;
import com.example.stencrypt.Fragments.EncodeFragment;
import com.example.stencrypt.Fragments.HomeFragment;
import com.example.stencrypt.R;
import com.example.stencrypt.Utilities.RSA;
import com.example.stencrypt.Fragments.StensFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class HomePage extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {
    FloatingActionButton userProfile;
    EncodeFragment encodeFragment;
    Fragment frag = null;
    BottomAppBar bottomAppBar;
    Boolean bool = false, boolStenShare = false;
    FrameLayout fragL;
    GoogleSignInClient mGoogleSignInClient;
    ContentResolver contentResolver;
    Context context;
    private DBHelper dbS;
    EncodeFragment fragment;
    String key, messagae, whichFragment = "userProfile";
    MaterialToolbar materialToolbar;
    String tmpKey;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private CollectionReference userDetailRef = db.collection("UserDetails");
    private CancellationSignal cancellationSignal = null;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;

    @SuppressLint("Range")
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
        materialToolbar.setTitle("Stencrypt");
        String pKey = null;
        dbS = new DBHelper(getApplicationContext());






        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(whichFragment.equals("encode")){
                    ((EncodeFragment) frag).imageOpener();
                }else if(whichFragment.equals("decode")){
                    ((DecodeFragment) frag).imageOpener();
                }
                else if(whichFragment.equals("userProfile")) {

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
                    whichFragment = "userProfile";
                    materialToolbar.setTitle("Stencrypt");

                }else{
                    frag = new StensFragment(HomePage.this);
                    materialToolbar.setTitle("Stenshare");
                    boolStenShare = true;
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, frag).commit();
                    menuSettings();
                }
            }
        });
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.gLogout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), GoogleLoginActivity.class));
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
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void bioMetrics(){

        authenticationCallback = new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(
                    int errorCode, CharSequence errString)
            {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result)
            {
                super.onAuthenticationSucceeded(result);
                try {
                    ((DecodeFragment) frag).getTextDataDecode(new RSA().Decrypt(tmpKey,getApplicationContext()));
                    RSA obj = new RSA();
                    String data = obj.Decrypt(tmpKey,getApplication());
                    notifyUser(data);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
        };

        checkBiometricSupport();

                BiometricPrompt biometricPrompt = new BiometricPrompt
                        .Builder(getApplicationContext())
                        .setTitle("Title of Prompt")
                        .setSubtitle("Subtitle")
                        .setDescription("Uses FP")
                        .setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
                            @Override
                            public void
                            onClick(DialogInterface dialogInterface, int i)
                            {
                                notifyUser("Authentication Cancelled");
                            }
                        }).build();

                biometricPrompt.authenticate(
                        getCancellationSignal(),
                        getMainExecutor(),
                        authenticationCallback);


    }
    private CancellationSignal getCancellationSignal()
    {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(
                new CancellationSignal.OnCancelListener() {
                    @Override public void onCancel()
                    {
                        notifyUser("Authentication was Cancelled by the user");
                    }
                });
        return cancellationSignal;
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private Boolean checkBiometricSupport()
    {
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isDeviceSecure()) {
            notifyUser("Fingerprint authentication has not been enabled in settings");
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC)!= PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint Authentication Permission is not enabled");
            return false;
        }
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return true;
        }
        else
            return true;
    }


    private void notifyUser(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void menuSettings(){
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        bool = true;
        if(!boolStenShare == true){
            bottomAppBar.replaceMenu(R.menu.alternate_menu);
        }
    }
    public void menuAlternateSetting(){
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_chat_bubble_outline_24);
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
        frag = new DecodeFragment(RESULT_OK,contentResolver,context,null);
        menuSettings();
        userProfile.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,frag,"").commit();
        materialToolbar.setTitle("DECODE");
    }
    public void openDecodeFragmentWithData(Bitmap bitmap){
        whichFragment = "decode";
        frag = new DecodeFragment(RESULT_OK,contentResolver,context,bitmap);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onDecodeButtonClicked(String key, int flag) {
        this.key = key;
        if(flag == 1){
            bioMetrics();
        }else {
            ((DecodeFragment) frag).getTextDataDecode(key);
        }
    }
}