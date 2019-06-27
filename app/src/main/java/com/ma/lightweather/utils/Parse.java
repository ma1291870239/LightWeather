package com.ma.lightweather.utils;

import android.content.Context;

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
                            weather.setCity(basicObj.getString("location"));
                        if (!basicObj.isNull("cnty"))
                            weather.setCnty(basicObj.getString("cnty"));
                    }
                    //实况天气
                    if(!weatherObj.isNull("now")){
                        JSONObject nowObj=weatherObj.getJSONObject("now");
                        if(!nowObj.isNull("fl"))
                            weather.setFeel(nowObj.getString("fl"));
                        if(!nowObj.isNull("hum"))
                            weather.setHum(nowObj.getString("hum"));
                        if(!nowObj.isNull("pcpn"))
                            weather.setPcpn(nowObj.getString("pcpn"));
                        if(!nowObj.isNull("pres"))
                            weather.setPres(nowObj.getString("pres"));
                        if(!nowObj.isNull("tmp"))
                            weather.setTmp(nowObj.getString("tmp"));
                        if(!nowObj.isNull("vis"))
                            weather.setVis(nowObj.getString("vis"));
                        if(!nowObj.isNull("cond_txt"))
                            weather.setTxt(nowObj.getString("cond_txt"));
                        if(!nowObj.isNull("wind_dir"))
                            weather.setDir(nowObj.getString("wind_dir"));
                        if(!nowObj.isNull("wind_spd"))
                            weather.setWind(nowObj.getString("wind_spd"));
                    }
                    //未来七天
                    if(!weatherObj.isNull("daily_forecast")){
                        JSONArray dailyArray=weatherObj.getJSONArray("daily_forecast");
                        if(dailyArray.length()>0){
                            for (int j=0;j<dailyArray.length();j++){
                                JSONObject dailyObj=dailyArray.getJSONObject(j);
                                if(!dailyObj.isNull("date"))
                                    weather.getDateList().add(dailyObj.getString("date"));
                                if(!dailyObj.isNull("cond_txt_d"))
                                    weather.getTxtList().add(dailyObj.getString("cond_txt_d"));
                                if(!dailyObj.isNull("tmp_max"))
                                    weather.getMaxList().add(Integer.valueOf(dailyObj.getString("tmp_max")));
                                if(!dailyObj.isNull("tmp_min"))
                                    weather.getMinList().add(Integer.valueOf(dailyObj.getString("tmp_min")));
                                if(!dailyObj.isNull("wind_dir"))
                                    weather.getDirList().add(dailyObj.getString("wind_dir"));
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
                                    weather.getHourDateList().add(hourObj.getString("time"));
                                if(!hourObj.isNull("cond_txt"))
                                    weather.getHourTxtList().add(hourObj.getString("cond_txt"));
                                if(!hourObj.isNull("pop"))
                                    weather.getHourPopList().add(hourObj.getInt("pop"));
                                if(!hourObj.isNull("tmp"))
                                    weather.getHourTmpList().add(hourObj.getInt("tmp"));
                                if(!hourObj.isNull("wind_dir"))
                                    weather.getHourDirList().add(hourObj.getString("wind_dir"));
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
                                    weather.getLifeTypeList().add(lifeObj.getString("type"));
                                if(!lifeObj.isNull("txt"))
                                    weather.getLifeTxtList().add(lifeObj.getString("txt"));
                                if(!lifeObj.isNull("brf"))
                                    weather.getLifeBrfList().add(lifeObj.getString("brf"));
                            }
                        }
                    }
                }
            }
        }

        if(weather.getCity() !=null) {
            weatherList.add(weather);
            DbUtils.createdb(context,weatherList);
            if(weatherView!=null) {
                weatherView.loadViewData(weather.getMaxList(), weather.getMinList(), weather.getDateList(), weather.getTxtList(), weather.getDirList());
            }
            if(hourWeatherView!=null) {
                hourWeatherView.loadViewData(weather.getHourTmpList(), weather.getHourPopList(), weather.getHourDateList(), weather.getHourTxtList(), weather.getHourDirList());
            }
        }
        return weatherList;
    }
}
