package com.ma.lightweather.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Ma-PC on 2016/12/15.
 */
class MydataBaseHelper(private val context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREAT_WEATHER)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

    }

    companion object {
        private const val CREAT_WEATHER = "create table weather(id integer primary key autoincrement,city,tmp,txt,dir)"
    }
}
