package com.example.nightout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BookmarksDB extends SQLiteOpenHelper {

    public BookmarksDB(Context context) {
        super(context, "Bookmarks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table bookmarks(username TEXT, itemid TEXT, itemname TEXT, itemtype TEXT, PRIMARY KEY(username, itemid))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists bookmarks");
    }

    // Function to save Bookmarks Data, Saves both Restaurant & Event Data in same Table using itemtype
    public Boolean saveData (String username, String itemid, String itemname, String itemtype) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("itemid", itemid);
        contentValues.put("itemname", itemname);
        contentValues.put("itemtype", itemtype);
        long result = MyDB.insert("bookmarks", null, contentValues);
        return (result != -1);
    }

    // Function to access savedData by querying & returns Names of Rest./Events as ArrayList
    public ArrayList<String> getSavedData(String username, String itemtype) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from bookmarks WHERE username = ? and itemtype = ?", new String[] {username, itemtype});
        ArrayList<String> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) arrayList.add(cursor.getString(2));
        if (arrayList.isEmpty()) { arrayList.add("ArrayList"); arrayList.add("is"); arrayList.add("empty"); }
        return arrayList;
    }
}
