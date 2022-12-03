package com.example.myapplication1.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication1.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyDbManger {
private Context context;
private MyDbHelper myDbHelper;
private SQLiteDatabase db;

    public MyDbManger(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }
    public void openDb(){

        db = myDbHelper.getWritableDatabase();
    }
    public void insertToDb(String title, String disc, String uri){

        ContentValues values = new ContentValues();
        values.put(MyConstants.TITLE, title);
        values.put(MyConstants.DISC, disc);
        values.put(MyConstants.URI, uri);
        db.insert(MyConstants.TABLE_NAME, null, values);
    }
    public void updateItem(String title, String disc, String uri, int id){
        String selection = MyConstants._ID + " ="  + id;
        ContentValues values = new ContentValues();
        values.put(MyConstants.TITLE, title);
        values.put(MyConstants.DISC, disc);
        values.put(MyConstants.URI, uri);
        db.update(MyConstants.TABLE_NAME, values, selection, null);

    }
    public void delete(int id){
        String selection = MyConstants._ID + " ="  + id;
        db.delete(MyConstants.TABLE_NAME,selection, null);
    }
    public void getFromDb(String searchText, OnDataReceived onDataReceived){
        String selection = MyConstants.TITLE + " like ?";
        List<ListItem> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, selection, new String[]{"%"+searchText+"%"},null,
                null, null);
        while (cursor.moveToNext()){
            ListItem item = new ListItem();
             @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            @SuppressLint("Range") String desc = cursor.getString(cursor.getColumnIndex(MyConstants.DISC));
            @SuppressLint("Range") String uri = cursor.getString(cursor.getColumnIndex(MyConstants.URI));
            @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));


            item.setTitle(title);
           item.setDesc(desc);
           item.setUri(uri);
            item.setId(_id);

            tempList.add(item);
        }
        cursor.close();
        onDataReceived.onReceived(tempList);
    }
    public void closeDb(){
        myDbHelper.close();
    }
}
