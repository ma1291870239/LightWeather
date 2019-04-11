package com.ma.lightweather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.model.HeFengWeather;
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
                weather.location=cursor.getString(cursor.getColumnIndex("city"));
                weather.tmp=cursor.getString(cursor.getColumnIndex("tmp"));
                weather.txt=cursor.getString(cursor.getColumnIndex("txt"));
                weather.dir=cursor.getString(cursor.getColumnIndex("dir"));
                if(weather.location!=null) {
                    weatherData.add(weather);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  weatherData;
    }

    public static void createdb(Context context,List<HeFengWeather.HeWeather> weatherList) {
        MydataBaseHelper dbHelper=new MydataBaseHelper(context,"Weather.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.beginTransaction();
        for (int i=0;i<weatherList.size();i++){
            ContentValues values = new ContentValues();
            values.put("city", weatherList.get(i).getBasic().getLocation());
            values.put("tmp", weatherList.get(i).getNow().getTmp());
            values.put("txt", weatherList.get(i).getNow().getCond_txt());
            values.put("dir", weatherList.get(i).getNow().getWind_dir());
            db.delete("weather","city = ?",new String[]{weatherList.get(i).getBasic().getLocation()});
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
