package com.ma.lightweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.Parse;
import com.ma.lightweather.utils.SharedPrefencesUtils;
import com.ma.lightweather.widget.HourWeatherView;
import com.ma.lightweather.widget.WeatherView;

import org.json.JSONException;

import java.util.List;


/**
 * Created by Ma-PC on 2016/12/5.
 */
public class WeatherFragment extends BaseFragment{

    private View view;
    private TextView tmptv,feeltv,humtv,pcpntv,citytv,windtv,pmtv,prestv,vistv;
    private TextView airTv,comfTv,cwTv,drsgTv,fluTv,sportTv,travTv,uvTv;
    private NestedScrollView scrollView;
    private WeatherView weatherView;
    private HourWeatherView hourWeatherView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Weather> weatherList;
    private String city;
    private static final int WEATHER_CODE=200;
    private static final int NOCITY_CODE=100;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WEATHER_CODE:
                    swipeRefreshLayout.setRefreshing(false);
                    scrollView.scrollTo(0,0);
                    for(int i=0;i<weatherList.size();i++) {
                        tmptv.setText(weatherList.get(i).tmp+"℃");
                        feeltv.setText("　体感："+weatherList.get(i).feel+" ℃");
                        humtv.setText("　湿度："+weatherList.get(i).hum+" %");
                        pcpntv.setText("　降雨："+weatherList.get(i).pcpn+" mm");
                        citytv.setText(weatherList.get(i).city+"　"+weatherList.get(i).cnty);
                        windtv.setText(weatherList.get(i).txt+"　"+weatherList.get(i).dir);
                        pmtv.setText("　风速："+weatherList.get(i).wind+" km/h");
                        prestv.setText("　气压："+weatherList.get(i).pres+" Pa");
                        vistv.setText("　能见："+weatherList.get(i).vis+" km");
                        for (int j=0;j<weatherList.get(i).lifeTypeList.size();j++){
                            Weather weather=weatherList.get(i);
                            String type=weather.lifeTypeList.get(j);
                            String s=weather.lifeBrfList.get(j)+"\n"+weather.lifeTxtList.get(j);
                            if(type.equals("air")){
                                airTv.setText("空气指数　　"+s);
                            }
                            if(type.equals("cw")){
                                cwTv.setText("洗车指数　　"+s);
                            }
                            if(type.equals("drsg")){
                                drsgTv.setText("穿衣指数　　"+s);
                            }
                            if(type.equals("flu")){
                                fluTv.setText("感冒指数　　"+s);
                            }
                            if(type.equals("sport")){
                                sportTv.setText("运动指数　　"+s);
                            }
                            if(type.equals("trav")){
                                travTv.setText("旅游指数　　"+s);
                            }
                            if(type.equals("comf")){
                                comfTv.setText("舒适度指数　"+s);
                            }
                            if(type.equals("uv")){
                                uvTv.setText("紫外线指数　"+s);
                            }
                        }
                        SharedPrefencesUtils.setParam(getActivity(),Contants.CITY,weatherList.get(i).city);
                        SharedPrefencesUtils.setParam(getActivity(),Contants.TMP,weatherList.get(i).tmp);
                        SharedPrefencesUtils.setParam(getActivity(),Contants.TXT,weatherList.get(i).txt);
                        //CommonUtils.showShortToast(getActivity(),"数据已更新");
                    }
                    break;
                case NOCITY_CODE:
                    CommonUtils.showShortToast(getActivity(),"未找到该城市");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_weather,null);
        city= (String) SharedPrefencesUtils.getParam(getActivity(),Contants.CITY,Contants.CITYNAME);
        initView();
        if(isAdded()){
            loadData(city);
        }
        return view;
    }

    //加载上部数据
    public void loadData(String city) {
        this.city=city;
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            weatherList= Parse.parseWeather(response,weatherView,hourWeatherView,getActivity());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(weatherList.size()>0) {
                            handler.sendEmptyMessage(WEATHER_CODE);
                            ((MainActivity)getActivity()).refreshCity();
                        }else {
                            handler.sendEmptyMessage(NOCITY_CODE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void initView() {
        tmptv=view.findViewById(R.id.weather_tmp);//温度
        feeltv=view.findViewById(R.id.weather_feel);//体感
        humtv=view.findViewById(R.id.weather_hum);//湿度
        pcpntv=view.findViewById(R.id.weather_pcpn);//降雨量
        citytv=view.findViewById(R.id.weather_city);//城市
        windtv=view.findViewById(R.id.weather_wind);//风向
        pmtv=view.findViewById(R.id.weather_pm);//PM2.5
        prestv=view.findViewById(R.id.weather_pres);//气压
        vistv=view.findViewById(R.id.weather_vis);//能见度
        scrollView=view.findViewById(R.id.weather_scroll);
        weatherView=view.findViewById(R.id.weather_view);
        hourWeatherView=view.findViewById(R.id.hourweather_view);
        airTv=view.findViewById(R.id.airTextView);
        comfTv=view.findViewById(R.id.comfTextView);
        cwTv=view.findViewById(R.id.cwTextView);
        drsgTv=view.findViewById(R.id.drsgTextView);
        fluTv=view.findViewById(R.id.fluTextView);
        sportTv=view.findViewById(R.id.sportTextView);
        travTv=view.findViewById(R.id.travTextView);
        uvTv=view.findViewById(R.id.uvTextView);

        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.background));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(city);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isResumed()){
        }
    }

}
