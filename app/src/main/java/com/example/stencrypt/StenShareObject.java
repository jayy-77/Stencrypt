package com.example.stencrypt;

import android.graphics.Bitmap;

public class StenShareObject {
    String  key,bitString,time,date,name;

    public StenShareObject(String bitString, String key) {
        this.bitString = bitString;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBitString() {
        return bitString;
    }

    public void setBitString(String bitString) {
        this.bitString = bitString;
    }
}
