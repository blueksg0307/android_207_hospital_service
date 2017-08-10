package com.first.administrator.project207.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-30.
 */

public class MyDatabaseManager extends SQLiteOpenHelper {

    private List<Member> memberList ;

    public MyDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
        db.execSQL("CREATE TABLE FamilyList( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, birth TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from FamilyList", null);
        while(cursor.moveToNext()) {
            str +=
                     cursor.getString(1)
                    + cursor.getInt(2)
                    ;
        }

        return str;
    }

    public int RowCount(){
        SQLiteDatabase db = getReadableDatabase();
        int count = 0  ;
        db.execSQL( "SELECT count(*) FROM FamilyList;");
       return count ;
    }

    public ArrayList<String> PrintName() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> name = new ArrayList();

        Cursor cursor = db.rawQuery("select * from FamilyList", null);
        while(cursor.moveToNext()) {
            int i = 0 ;
           String str = new String(cursor.getString(1));
            name.add(str);
        }return name ;
    }

    public ArrayList<String> PrintBirth() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> name = new ArrayList();

        Cursor cursor = db.rawQuery("select * from FamilyList", null);
        while(cursor.moveToNext()) {
            int i = 0 ;
            String str = new String(cursor.getString(2));
            name.add(str);
        }return name ;
    }

}
