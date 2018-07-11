package com.ma.lightweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ma-PC on 2016/12/15.
 */
public class MydataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String CREAT_WEATHER="create table weather(id integer primary key autoincrement,city,tmp,txt,dir)";

    public MydataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREAT_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
