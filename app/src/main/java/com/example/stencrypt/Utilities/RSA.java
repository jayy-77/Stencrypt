package com.example.stencrypt.Utilities;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
    private DBHelper mydb ;

    byte[] encryptedBytes, decryptedBytes;
    Cipher cipher, cipher1;
    String encrypted, decrypted , pri_str_key, pub_str_key,rPublicKey,result;

    public RSA(String rPublicKey){
        this.rPublicKey = rPublicKey;
    }
    public RSA(){

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] keyPairGenerate() throws NoSuchAlgorithmException {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

            byte[] byte_prikey = privateKey.getEncoded();
            pri_str_key = Base64.getEncoder().encodeToString(byte_prikey);
            byte [] byte_pubkey = publicKey.getEncoded();
            pub_str_key = Base64.getEncoder().encodeToString(byte_pubkey);
            String keyPair[] = new String[0];
            keyPair[0] = pri_str_key;
            keyPair[1] = pub_str_key;

        return keyPair;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String Encrypt (String plain, String rPublicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException {
        byte[] byte_pubkey  = Base64.getDecoder().decode(rPublicKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey public_key = (PublicKey) factory.generatePublic(new X509EncodedKeySpec(byte_pubkey));
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, public_key);
        encryptedBytes = cipher.doFinal(plain.getBytes());
        encrypted = bytesToString(encryptedBytes);
        return encrypted;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String Decrypt (String result, Context context) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        mydb = new DBHelper(context);
        this.result = result;
        byte[] byte_prikey  = Base64.getDecoder().decode(mydb.getName(1));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey Private_Key = (PrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(byte_prikey));
        cipher1=Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, Private_Key);
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