package com.example.stencrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StenShareObject {
    String bitString,key,name,email,date,time;
    public StenShareObject(){

    }
    public StenShareObject(String bitString, String key, String name, String email, String date, String time){
        this.bitString = bitString;
        this.key = key;
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
    }
    public String getBitString() {
        return bitString;
    }

    public void setBitString(String bitString) {
        this.bitString = bitString;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
