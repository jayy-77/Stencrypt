package com.example.stencrypt.DataModel;

import android.net.Uri;


public class UserObject {
    private String email;
    private String name;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    private String publicKey;
    private Uri userPhoto;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
    }


    public UserObject(){

    }
    public UserObject(String email, String name, Uri userPhoto,String publicKey){
        this.email = email;
        this.name = name;
        this.userPhoto = userPhoto;
        this.publicKey = publicKey;
    }
}
