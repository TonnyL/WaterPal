package com.marktony.drinkingwater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lizhaotailang on 2015/10/4.
 * 用于创建用户记录数据库
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{

    //建立一张名为History的表
    public static final String CREATE_BOOK = "create table History ("
            + "id integer primary key autoincrement, "
            + "date text, "
            + "bottle text, "
            + "time text, "
            + "week text)";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
