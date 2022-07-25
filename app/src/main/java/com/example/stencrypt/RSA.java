package com.example.stencrypt;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte[] encryptedBytes, decryptedBytes;
    Cipher cipher, cipher1;
    String encrypted, decrypted , pri_str_key, pub_str_key;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String keyPairGenerate(String whichOne) throws NoSuchAlgorithmException {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
        if(whichOne.equals("private")) {
            byte[] byte_prikey = privateKey.getEncoded();
            pri_str_key = Base64.getEncoder().encodeToString(byte_prikey);
            return pri_str_key;
        }else if(whichOne.equals("public")){
            byte [] byte_pubkey = publicKey.getEncoded();
            pub_str_key = Base64.getEncoder().encodeToString(byte_pubkey);
            return pub_str_key;
        }
        return null;
    }
    public String Encrypt (String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IllegalBlockSizeException, BadPaddingException {

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());

        encrypted = bytesToString(encryptedBytes);
        return encrypted;

    }

    public String Decrypt (String result) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {

        cipher1=Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(stringToBytes(result));
        decrypted = new String(decryptedBytes);
        return decrypted;

    }

    public  String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    public  byte[] stringToBytes(String s) {
        byte[] b2 = new BigInteger(s, 36).toByteArray();
        return Arrays.copyOfRange(b2, 1, b2.length);
    }
}