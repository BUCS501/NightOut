package com.example.nightout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DB(Context context) {
        super(context, "Login.db", null, 1);
    }

    //Create the Table in the database with the email address as the primary key and 2 other values
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    //Function for inserting the data into the table when creating a new user
    public Boolean insertData( String username, String password,String name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("name", name);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    //Function to see if the username exists
    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    

    //Function for retrieving the name associated with a certain email address
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

    //Function for getting the password associated with a certain email address
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

    //function for deleting the account
    public void deleter (String usernn) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete("users","username = '" +usernn + "'",null);

        // TODO: Delete Bookmarks when user is deleted
        // MyDB.delete("bookmarks","username = '" +usernn + "'",null);
    }
}
