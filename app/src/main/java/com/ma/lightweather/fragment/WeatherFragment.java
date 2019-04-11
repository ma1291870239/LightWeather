package com.ma.lightweather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.app.WeatherService;
import com.ma.lightweather.model.HeFengWeather;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.DbUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;
import com.ma.lightweather.widget.HourWeatherView;
import com.ma.lightweather.widget.WeatherView;

import java.util.List;


/**
 * Created by Ma-PC on 2016/12/5.
 */
public class WeatherFragment extends BaseFragment{

    private View view;
    private TextView tmptv,feeltv,humtv,pcpntv,citytv,windtv,pmtv,prestv,vistv;
    private TextView airTv,comfTv,cwTv,drsgTv,fluTv,sportTv,travTv,uvTv;
    private LinearLayout weatherLife;
    private NestedScrollView scrollView;
    private WeatherView weatherView;
    private HourWeatherView hourWeatherView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<HeFengWeather.HeWeather> weatherList;
    private String city;
    private static final int WEATHER_CODE=200;
    private static final int NOCITY_CODE=100;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WEATHER_CODE:
                    setData();
                    break;
                case NOCITY_CODE:
                    CommonUtils.showShortToast(context,"未找到该城市");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_weather,null);
        city= (String) SharedPrefencesUtils.getParam(context,Contants.CITY,Contants.CITYNAME);
        if(isAdded()){
            initView();
            loadData(city);
        }
        return view;
    }

    //加载上部数据
    public void loadData(String city) {
        this.city=city;
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson=new Gson();
                        HeFengWeather heFengWeather = gson.fromJson(response, HeFengWeather.class);
                        weatherList=heFengWeather.getHeWeather6();
                        DbUtils.createdb(context,weatherList);
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

    private void setData(){
        swipeRefreshLayout.setRefreshing(false);
        scrollView.scrollTo(0,0);
        for(int i=0;i<weatherList.size();i++) {
            HeFengWeather.Basic basic=weatherList.get(i).getBasic();
            HeFengWeather.Now now=weatherList.get(i).getNow();
            List<HeFengWeather.Hourly> hourlyList=weatherList.get(i).getHourly();
            List<HeFengWeather.Daily> dailyList=weatherList.get(i).getDaily_forecast();

            tmptv.setText(now.getTmp()+"℃");
            feeltv.setText("　体感："+now.getFl()+" ℃");
            humtv.setText("　湿度："+now.getHum()+" %");
            pcpntv.setText("　降雨："+now.getPcpn()+" mm");
            citytv.setText(basic.getLocation()+"　"+basic.getParent_city()+"　"+basic.getCnty());
            windtv.setText(now.getCond_txt()+"　"+now.getWind_sc()+"级"+now.getWind_dir());
            pmtv.setText("　风速："+now.getWind_spd()+" km/h");
            prestv.setText("　气压："+now.getPres()+" Pa");
            vistv.setText("　能见："+now.getVis()+" km");
            if(weatherList.get(i).getLifestyle().size()<=0){
                weatherLife.setVisibility(View.GONE);
            }else {
                weatherLife.setVisibility(View.VISIBLE);
            }
            if((boolean) SharedPrefencesUtils.getParam(context,Contants.LIFE,true)){
                weatherLife.setVisibility(View.VISIBLE);
                for (int j=0;j<weatherList.get(i).getLifestyle().size();j++){
                    HeFengWeather.Lifestyle lifestyle=weatherList.get(i).getLifestyle().get(j);
                    String type=lifestyle.getType();
                    String s=lifestyle.getBrf()+"\n"+lifestyle.getTxt();
                    if(type.equals("air")&& !TextUtils.isEmpty(s)){
                        airTv.setText("空气指数　　"+s);
                    }
                    if(type.equals("cw")&&!TextUtils.isEmpty(s)){
                        cwTv.setText("洗车指数　　"+s);
                    }
                    if(type.equals("drsg")&&!TextUtils.isEmpty(s)){
                        drsgTv.setText("穿衣指数　　"+s);
                    }
                    if(type.equals("flu")&&!TextUtils.isEmpty(s)){
                        fluTv.setText("感冒指数　　"+s);
                    }
                    if(type.equals("sport")&&!TextUtils.isEmpty(s)){
                        sportTv.setText("运动指数　　"+s);
                    }
                    if(type.equals("trav")&&!TextUtils.isEmpty(s)){
                        travTv.setText("旅游指数　　"+s);
                    }
                    if(type.equals("comf")&&!TextUtils.isEmpty(s)){
                        comfTv.setText("舒适度指数　"+s);
                    }
                    if(type.equals("uv")&&!TextUtils.isEmpty(s)){
                        uvTv.setText("紫外线指数　"+s);
                    }
                }
            }else{
                weatherLife.setVisibility(View.GONE);
            }
            hourWeatherView.loadViewData(hourlyList);
            weatherView.loadViewData(dailyList);

            SharedPrefencesUtils.setParam(context,Contants.CITY,basic.getLocation());
            SharedPrefencesUtils.setParam(context,Contants.TMP,now.getTmp());
            SharedPrefencesUtils.setParam(context,Contants.TXT,now.getCond_txt());
            //CommonUtils.showShortToast(getC,"数据已更新");
            if((boolean) SharedPrefencesUtils.getParam(context,Contants.NOTIFY,false)){
                Intent it=new Intent(context, WeatherService.class);
                context.startService(it);
            }
        }
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
        weatherLife=view.findViewById(R.id.weather_life);
        airTv=view.findViewById(R.id.airTextView);
        comfTv=view.findViewById(R.id.comfTextView);
        cwTv=view.findViewById(R.id.cwTextView);
        drsgTv=view.findViewById(R.id.drsgTextView);
        fluTv=view.findViewById(R.id.fluTextView);
        sportTv=view.findViewById(R.id.sportTextView);
        travTv=view.findViewById(R.id.travTextView);
        uvTv=view.findViewById(R.id.uvTextView);

        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(CommonUtils.getBackColor(context));
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
