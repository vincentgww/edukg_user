package com.fairychild.edukguser.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    /*
    其他成员变量
     */
    private Context mContext;
    private String name;
    /*
    调试用标志
     */
    private static final String TAG = "UserDatabaseHelper";

    /*
    建表指令
     */
    public static final String createSearchHistoryTable = "CREATE TABLE SEARCHHISTORY ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "COURSE TEXT NOT NULL,"
            + "SEARCHKEY TEXT NOT NULL,"
            + "TIME TEXT NOT NULL);";
    public static final String createBrowsingHistoryTable = "CREATE TABLE BROWSINGHISTORY ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "COURSE TEXT NOT NULL,"
            + "NAME TEXT NOT NULL,"
            + "TIME TEXT NOT NULL);";
    public static final String createFavouritesTable = "CREATE TABLE FAVOURITES ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "COURSE TEXT NOT NULL,"
            + "NAME TEXT NOT NULL,"
            + "UNIQUE(COURSE,NAME) ON CONFLICT REPLACE);";
    public static final String createDetailContentTable = "CREATE TABLE DETAIL ("
            + "COURSE TEXT NOT NULL,"
            + "NAME TEXT NOT NULL,"
            + "CONTENT TEXT NOT NULL,"
            + "UNIQUE(COURSE,NAME) ON CONFLICT REPLACE);";

    /*
    context: 默认为MainActivity
    name: 用户名
    factory: 用来创建游标，默认值为null，指向一开始的位置
    version: 默认为1
    */
    public UserDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        this.name = name;
    }

    public UserDatabaseHelper(@Nullable Context context, @Nullable String name){
        this(context, name, null, 1);
    }

    /*
    db: 数据库变量
    根据需要创建表和初始化一些数值
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(createSearchHistoryTable);
        db.execSQL(createBrowsingHistoryTable);
        db.execSQL(createFavouritesTable);
        db.execSQL(createDetailContentTable);
        Log.d(TAG, name + "create successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}