package com.example.rohit.arishit_f.login.chat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rohit.arishit_f.registration.RegisterPage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ChatDatabase.db";
    final String SQL_CREATE_TABLE = "CREATE TABLE CHAT ( MESSAGE TEXT, Key1 TEXT, Key2 TEXT )";
    final String SQL_CREATE_TABLE2 = "CREATE TABLE LASTLOGIN (Last_Login TEXT, Register_Token TEXT)";
    public List<String> msg = new ArrayList<String>();
    public List<String> key1 = new ArrayList<String>();
    public List<String> key2 = new ArrayList<String>();
    SQLiteDatabase db;
    RegisterPage registerPage;

    public DatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
//        db=this.getWritableDatabase();
        System.out.println("Database");
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE2);
        //String token=registerPage.Register_token;
    }

    public boolean addMessage(String message, String key1, String key2) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MESSAGE", message);
        cv.put("Key1", key1);
        cv.put("Key2", key2);
        long result = db.insert("CHAT", null, cv);
        System.out.println(result +"result");
        ContentValues cv1 = new ContentValues();
        cv1.put("Last_Login", "093700");
        //long result1=db.insert("LASTLOGIN",null,cv1);
        //System.out.println(result1);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void insertToken(String token)
    {
        System.out.println(token+"registertoken");
        String ROW1 = "INSERT INTO " + "LASTLOGIN" + " Values ('013700','"+token+"');";
        db.execSQL(ROW1);

    }

    public void checkLastLogin(String current) {
        String query = "SELECT * from LASTLOGIN";
        String name = "103700";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(0);
            }
        }
        //String name = cursor.getString(cursor.getColumnIndex("Last_Login"));
        int curr_time = Integer.parseInt(current);
        current = String.valueOf(curr_time);
        int last_time = Integer.parseInt(name);
        if (curr_time - last_time > 90000) {
            db.execSQL("Drop table CHAT");
            String s = "UPDATE LASTLOGIN SET Last_Login='" + current + "' WHERE Last_Login='" + name + "'";
            db.execSQL(s);
        } else {
            String s = "UPDATE LASTLOGIN SET Last_Login='" + current + "' WHERE Last_Login='" + name + "'";
            System.out.println(s + "query");
            db.execSQL(s);
        }
    }

    public void fetchmessage() {
        Cursor cursor = db.rawQuery("select * from CHAT", null);
        int i=0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String message = cursor.getString(cursor.getColumnIndex("MESSAGE"));
                msg.add(message);
                String key_1 = cursor.getString(cursor.getColumnIndex("Key1"));
                key1.add(key_1);
                String key_2 = cursor.getString(cursor.getColumnIndex("Key2"));
                key2.add(key_2);
                i++;
                cursor.moveToNext();

            }
        }
        System.out.println(msg+"MESSAGE");
        System.out.println(key1);
        System.out.println(key2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "CHAT");
        db.execSQL("DROP TABLE IF EXISTS " + "LASTLOGIN");
        onCreate(db);
    }
}
