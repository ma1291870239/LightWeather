package com.ma.lightweather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.model.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma on 2018/12/8.
 */

public class DbUtils {

    public static List<Weather> selectdb(Context context) {
        List<Weather> weatherData=new ArrayList<>();
        MydataBaseHelper dbHelper=new MydataBaseHelper(context,"Weather.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("weather",null,null,null,null,null,"id desc");
        if(cursor.moveToFirst()){
            do{
                Weather weather=new Weather();
                weather.setCity(cursor.getString(cursor.getColumnIndex("city")));
                weather.setTmp(cursor.getString(cursor.getColumnIndex("tmp")));
                weather.setTxt(cursor.getString(cursor.getColumnIndex("txt")));
                weather.setDir(cursor.getString(cursor.getColumnIndex("dir")));
                if(weather.getCity() !=null) {
                    weatherData.add(weather);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  weatherData;
    }

    public static void createdb(Context context,List<Weather> weatherList) {
        MydataBaseHelper dbHelper=new MydataBaseHelper(context,"Weather.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.beginTransaction();
        for (int i=0;i<weatherList.size();i++){
            ContentValues values = new ContentValues();
            values.put("city", weatherList.get(i).getCity());
            values.put("tmp", weatherList.get(i).getTmp());
            values.put("txt", weatherList.get(i).getTxt());
            values.put("dir", weatherList.get(i).getDir());
            db.delete("weather","city = ?",new String[]{weatherList.get(i).getCity()});
            db.insert("weather",null,values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static void deleteCity(Context context,String city){
        MydataBaseHelper dbHelper=new MydataBaseHelper(context,"Weather.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("weather","city = ?",new String[]{city});
    }
}
