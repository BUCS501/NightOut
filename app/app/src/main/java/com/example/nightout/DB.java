package com.example.nightout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DB(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, name TEXT,location TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData( String username, String password,String name,String location){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("name", name);
        contentValues.put("location", location);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public String getdataname (String usernn) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            if ( cursor.getString(0).equals( usernn)) {
                buffer.append(cursor.getString(2));
            }
        }
        return buffer.toString();
    }
    public String getdatapass (String usernn) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            if ( cursor.getString(0).equals( usernn)) {
                buffer.append(cursor.getString(1));
            }
        }
        return buffer.toString();
    }
    public String getdataloc (String userr) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            if ( cursor.getString(0).equals( userr)) {
                buffer.append(cursor.getString(3));
            }
        }
        return buffer.toString();
    }
}
