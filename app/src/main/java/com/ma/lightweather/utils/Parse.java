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

                    //实时天气
                    if(!weatherObj.isNull("hourly_forecast")){
                        JSONArray hourArray=weatherObj.getJSONArray("hourly_forecast");
                        if(hourArray.length()>0){
                            for (int j=0;j<hourArray.length();j++){
                                JSONObject hourObj=hourArray.getJSONObject(j);
                                if(!hourObj.isNull("date")) {
                                    weather.hourDateList.add(hourObj.getString("date"));
                                }
                                if(!hourObj.isNull("cond")){
                                    JSONObject hourCondObj=hourObj.getJSONObject("cond");
                                    if(!hourCondObj.isNull("txt"))
                                        weather.hourTxtList.add(hourCondObj.getString("txt"));
                                }
                                if(!hourObj.isNull("pop")){
                                    weather.hourPopList.add(hourObj.getInt("pop"));
                                }
                                if(!hourObj.isNull("tmp")){
                                    weather.hourTmpList.add(hourObj.getInt("tmp"));
                                }
                                if(!hourObj.isNull("wind")){
                                    JSONObject hourWindObj=hourObj.getJSONObject("wind");
                                    if(!hourWindObj.isNull("dir"))
                                        weather.hourDirList.add(hourWindObj.getString("dir"));
                                }
                            }
                        }
                    }

                    //生活指数
                    if(!weatherObj.isNull("suggestion")){
                        Log.e("abc",weatherObj.getString("suggestion"));
                        JSONObject suggestObj=weatherObj.getJSONObject("suggestion");
                        if(!suggestObj.isNull("air")){
                            Log.e("abc",suggestObj.getString("air"));
                            JSONObject airObj=suggestObj.getJSONObject("air");
                            if(!airObj.isNull("brf"))
                                Log.e("abc",airObj.getString("brf"));
                                weather.airBrf=airObj.getString("brf");
                            if(!airObj.isNull("txt"))
                                weather.airTxt=airObj.getString("txt");
                        }
                        if(!suggestObj.isNull("comf")){
                            JSONObject comfObj=suggestObj.getJSONObject("comf");
                            if(!comfObj.isNull("brf"))
                                weather.comfBrf=comfObj.getString("brf");
                            if(!comfObj.isNull("txt"))
                                weather.comfTxt=comfObj.getString("txt");
                        }
                        if(!suggestObj.isNull("cw")){
                            JSONObject cwObj=suggestObj.getJSONObject("cw");
                            if(!cwObj.isNull("brf"))
                                weather.cwBrf=cwObj.getString("brf");
                            if(!cwObj.isNull("txt"))
                                weather.cwTxt=cwObj.getString("txt");
                        }
                        if(!suggestObj.isNull("drsg")){
                            JSONObject drsgObj=suggestObj.getJSONObject("drsg");
                            if(!drsgObj.isNull("brf"))
                                weather.drsgBrf=drsgObj.getString("brf");
                            if(!drsgObj.isNull("txt"))
                                weather.drsgTxt=drsgObj.getString("txt");
                        }
                        if(!suggestObj.isNull("flu")){
                            JSONObject fluObj=suggestObj.getJSONObject("flu");
                            if(!fluObj.isNull("brf"))
                                weather.fluBrf=fluObj.getString("brf");
                            if(!fluObj.isNull("txt"))
                                weather.fluTxt=fluObj.getString("txt");
                        }
                        if(!suggestObj.isNull("sport")){
                            JSONObject sportObj=suggestObj.getJSONObject("sport");
                            if(!sportObj.isNull("brf"))
                                weather.sportBrf=sportObj.getString("brf");
                            if(!sportObj.isNull("txt"))
                                weather.sportTxt=sportObj.getString("txt");
                        }
                        if(!suggestObj.isNull("trav")){
                            JSONObject travObj=suggestObj.getJSONObject("trav");
                            if(!travObj.isNull("brf"))
                                weather.travBrf=travObj.getString("brf");
                            if(!travObj.isNull("txt"))
                                weather.travTxt=travObj.getString("txt");
                        }
                        if(!suggestObj.isNull("uv")){
                            JSONObject uvObj=suggestObj.getJSONObject("uv");
                            if(!uvObj.isNull("brf"))
                                weather.uvBrf=uvObj.getString("brf");
                            if(!uvObj.isNull("txt"))
                                weather.uvTxt=uvObj.getString("txt");
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
