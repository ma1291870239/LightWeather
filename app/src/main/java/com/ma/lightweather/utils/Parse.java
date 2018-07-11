package com.ma.lightweather.utils;

import android.content.Context;

import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.model.Weather;
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

    public static List<Weather> parseWeather(String resource, WeatherView weatherView, Context context) throws JSONException {
        weatherList=new ArrayList<>();
        Weather weather=new Weather();
        JSONObject jsonObject=new JSONObject(resource);
        if(!jsonObject.isNull("HeWeather5")){
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather5");
            if(jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject weatherObj=jsonArray.getJSONObject(i);
                    //
                    if(!weatherObj.isNull("aqi")){
                        JSONObject aqiObj=weatherObj.getJSONObject("aqi");
                        if(!aqiObj.isNull("city")){
                            JSONObject cityObj=aqiObj.getJSONObject("city");
                            if(!cityObj.isNull("pm25"))
                                weather.pm=cityObj.getString("pm25");
                        }
                    }

                    //城市信息
                    if(!weatherObj.isNull("basic")){
                        JSONObject basicObj=weatherObj.getJSONObject("basic");
                        if(!basicObj.isNull("city"))
                            weather.city=basicObj.getString("city");
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
                        if(!nowObj.isNull("cond")) {
                            JSONObject condObj = nowObj.getJSONObject("cond");
                            if (!condObj.isNull("txt"))
                                weather.txt=condObj.getString("txt");
                        }
                        if(!nowObj.isNull("wind")) {
                            JSONObject windObj = nowObj.getJSONObject("wind");
                            if(!windObj.isNull("dir"))
                                weather.dir=windObj.getString("dir");
                        }
                    }
                    //未来七天
                    if(!weatherObj.isNull("daily_forecast")){
                        JSONArray dailyArray=weatherObj.getJSONArray("daily_forecast");
                        if(dailyArray.length()>0){
                            for (int j=0;j<dailyArray.length();j++){
                                JSONObject dailyObj=dailyArray.getJSONObject(j);
                                if(!dailyObj.isNull("date")) {
                                    weather.dateList.add(dailyObj.getString("date"));
                                }
                                if(!dailyObj.isNull("cond")){
                                    JSONObject dailycondObj=dailyObj.getJSONObject("cond");
                                    if(!dailycondObj.isNull("txt_d"))
                                        weather.txtList.add(dailycondObj.getString("txt_d"));
                                }
                                if(!dailyObj.isNull("tmp")){
                                    JSONObject dailytemObj=dailyObj.getJSONObject("tmp");
                                    if(!dailytemObj.isNull("max"))
                                        weather.maxList.add(Integer.valueOf(dailytemObj.getString("max")));
                                    if(!dailytemObj.isNull("min"))
                                        weather.minList.add(Integer.valueOf(dailytemObj.getString("min")));
                                }
                                if(!dailyObj.isNull("wind")){
                                    JSONObject dailywindObj=dailyObj.getJSONObject("wind");
                                    if(!dailywindObj.isNull("dir"))
                                        weather.dirList.add(dailywindObj.getString("dir"));
                                }
                            }
                        }
                    }
                }
            }
        }
        if(weatherView!=null) {
            weatherView.loadViewData(weather.maxList, weather.minList, weather.dateList, weather.txtList, weather.dirList);
        }
        if(weather.city!=null) {
            weatherList.add(weather);
        }
        return weatherList;
    }
}
