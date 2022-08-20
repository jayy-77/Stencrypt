package com.example.stencrypt.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    private static final String COL2 = "PrivateKey";
    public static final String DATABASE_NAME = "Stencrypt.db";
    public static final String CONTACTS_TABLE_NAME = "RSA_PrivateKey";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_TEXT1 = "PrivateKey";

    private HashMap hp;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table RSA_privateKey" + "(id integer primary key, PrivateKey text)"
        );
        db.execSQL(
                "create table tempKeyStorage" + "(id integer primary key, currentKey text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS RSA_privateKey");
//        onCreate(db);
    }

    public boolean insertData(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PrivateKey", key);
        db.insert("RSA_privateKey", null, contentValues);
        return true;
    }
    public boolean insertCurrentKey(String currentKey){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentKey", currentKey);
        db.insert("tempKeyStorage", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from RSA_privateKey where id=" + id + "", null);
        return res;
    }
    public String getName(long id) {

        String rv = "not found";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query("RSA_privateKey",null,whereclause,whereargs,null,null,null);
        if (csr.moveToFirst()) {
            rv = csr.getString(csr.getColumnIndex(COL2));
        }
        return rv;
    }




    public boolean updateBarcode(Integer id, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PrivateKey", key);
        db.update("RSA_privateKey", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
}
