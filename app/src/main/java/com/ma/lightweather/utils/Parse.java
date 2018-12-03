package com.ma.lightweather.utils;

import android.content.Context;
import android.util.Log;

import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.widget.HourWeatherView;
import com.ma.lightweather.widget.WeatherView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/13.
 */
public class Parse {

    private static List<Weather> weatherList;

    public static List<Weather> parseWeather(String resource, WeatherView weatherView, HourWeatherView hourWeatherView, Context context) throws JSONException {
        weatherList=new ArrayList<>();
        Weather weather=new Weather();
        JSONObject jsonObject=new JSONObject(resource);
        if(!jsonObject.isNull("HeWeather6")){
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            if(jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject weatherObj=jsonArray.getJSONObject(i);

                    //城市信息
                    if(!weatherObj.isNull("basic")){
                        JSONObject basicObj=weatherObj.getJSONObject("basic");
                        if(!basicObj.isNull("location"))
                            weather.city=basicObj.getString("location");
                        if (!basicObj.isNull("cnty"))
                            weather.cnty=basicObj.getString("cnty");
                    }
                    //实况天气
                    if(!weatherObj.isNull("now")){
                        JSONObject nowObj=weatherObj.getJSONObject("now");
                        if(!nowObj.isNull("fl"))
                            weather.feel=nowObj.getString("fl");
                        if(!nowObj.isNull("hum"))
                            weather.hum=nowObj.getString("hum");
                        if(!nowObj.isNull("pcpn"))
                            weather.pcpn=nowObj.getString("pcpn");
                        if(!nowObj.isNull("pres"))
                            weather.pres=nowObj.getString("pres");
                        if(!nowObj.isNull("tmp"))
                            weather.tmp=nowObj.getString("tmp");
                        if(!nowObj.isNull("vis"))
                            weather.vis=nowObj.getString("vis");
                        if(!nowObj.isNull("cond_txt"))
                            weather.txt= nowObj.getString("cond_txt");
                        if(!nowObj.isNull("wind_dir"))
                            weather.dir=nowObj.getString("wind_dir");
                    }
                    //未来七天
                    if(!weatherObj.isNull("daily_forecast")){
                        JSONArray dailyArray=weatherObj.getJSONArray("daily_forecast");
                        if(dailyArray.length()>0){
                            for (int j=0;j<dailyArray.length();j++){
                                JSONObject dailyObj=dailyArray.getJSONObject(j);
                                if(!dailyObj.isNull("date"))
                                    weather.dateList.add(dailyObj.getString("date"));
                                if(!dailyObj.isNull("cond_txt_d"))
                                    weather.txtList.add(dailyObj.getString("cond_txt_d"));
                                if(!dailyObj.isNull("tmp_max"))
                                    weather.maxList.add(Integer.valueOf(dailyObj.getString("tmp_max")));
                                if(!dailyObj.isNull("tmp_min"))
                                    weather.minList.add(Integer.valueOf(dailyObj.getString("tmp_min")));
                                if(!dailyObj.isNull("wind_dir"))
                                    weather.dirList.add(dailyObj.getString("wind_dir"));
                            }
                        }
                    }

                    //实时天气
                    if(!weatherObj.isNull("hourly")){
                        JSONArray hourArray=weatherObj.getJSONArray("hourly");
                        if(hourArray.length()>0){
                            for (int j=0;j<hourArray.length();j++){
                                JSONObject hourObj=hourArray.getJSONObject(j);
                                if(!hourObj.isNull("time"))
                                    weather.hourDateList.add(hourObj.getString("time"));
                                if(!hourObj.isNull("cond_txt"))
                                    weather.hourTxtList.add(hourObj.getString("cond_txt"));
                                if(!hourObj.isNull("pop"))
                                    weather.hourPopList.add(hourObj.getInt("pop"));
                                if(!hourObj.isNull("tmp"))
                                    weather.hourTmpList.add(hourObj.getInt("tmp"));
                                if(!hourObj.isNull("wind_dir"))
                                    weather.hourDirList.add(hourObj.getString("wind_dir"));
                            }
                        }
                    }

                    //实时天气
                    if(!weatherObj.isNull("lifestyle")){
                        JSONArray lifeArray=weatherObj.getJSONArray("lifestyle");
                        if(lifeArray.length()>0){
                            for (int j=0;j<lifeArray.length();j++){
                                JSONObject lifeObj=lifeArray.getJSONObject(j);
                                if(!lifeObj.isNull("type"))
                                    weather.lifeTypeList.add(lifeObj.getString("type"));
                                if(!lifeObj.isNull("txt"))
                                    weather.lifeTxtList.add(lifeObj.getString("txt"));
                                if(!lifeObj.isNull("brf"))
                                    weather.lifeBrfList.add(lifeObj.getString("brf"));
                            }
                        }
                    }
                }
            }
        }
        if(weatherView!=null) {
            weatherView.loadViewData(weather.maxList, weather.minList, weather.dateList, weather.txtList, weather.dirList);
        }
        if(hourWeatherView!=null) {
            hourWeatherView.loadViewData(weather.hourTmpList, weather.hourPopList, weather.hourDateList, weather.hourTxtList, weather.hourDirList);
        }
        if(weather.city!=null) {
            weatherList.add(weather);
        }
        return weatherList;
    }
}
