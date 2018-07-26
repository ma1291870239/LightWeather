package com.ma.lightweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ma.lightweather.R;
import com.ma.lightweather.model.Contants;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.Parse;
import com.ma.lightweather.utils.SharedPrefencesUtils;
import com.ma.lightweather.widget.WeatherView;

import org.json.JSONException;

import java.util.List;


/**
 * Created by Ma-PC on 2016/12/5.
 */
public class WeatherFragment extends BaseFragment{

    private View view;
    private TextView tmptv,feeltv,humtv,pcpntv,citytv,windtv,pmtv,prestv,vistv;
    private WeatherView weatherView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Weather> weatherList;
    private String city;
    private static final int WEATHER_CODE=200;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WEATHER_CODE:
                    swipeRefreshLayout.setRefreshing(false);
                    for(int i=0;i<weatherList.size();i++) {
                        tmptv.setText(weatherList.get(i).tmp+"℃");
                        feeltv.setText("　体感："+weatherList.get(i).feel+"℃");
                        humtv.setText("　湿度："+weatherList.get(i).hum+"%");
                        pcpntv.setText("　降雨："+weatherList.get(i).pcpn+"mm");
                        citytv.setText(weatherList.get(i).city+"　"+weatherList.get(i).cnty);
                        windtv.setText(weatherList.get(i).txt+"　"+weatherList.get(i).dir);
                        pmtv.setText("　污染："+weatherList.get(i).pm);
                        prestv.setText("　气压："+weatherList.get(i).pres+"Pa");
                        vistv.setText("　能见："+weatherList.get(i).vis+"km");
                        SharedPrefencesUtils.setParam(getActivity(),Contants.CITY,weatherList.get(i).city);
                        SharedPrefencesUtils.setParam(getActivity(),Contants.TMP,weatherList.get(i).tmp);
                        SharedPrefencesUtils.setParam(getActivity(),Contants.TXT,weatherList.get(i).txt);
                        //CommonUtils.showShortToast(getActivity(),"数据已更新");
                        break;
                    }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_weather,null);
        city= (String) SharedPrefencesUtils.getParam(getActivity(),Contants.CITY,"洛阳");
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
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city + Contants.KEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            weatherList= Parse.parseWeather(response,weatherView,getActivity());
                            handler.sendEmptyMessage(WEATHER_CODE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void initView() {
        tmptv= (TextView) view.findViewById(R.id.weather_tmp);//温度
        feeltv= (TextView) view.findViewById(R.id.weather_feel);//体感
        humtv= (TextView) view.findViewById(R.id.weather_hum);//湿度
        pcpntv= (TextView) view.findViewById(R.id.weather_pcpn);//降雨量
        citytv= (TextView) view.findViewById(R.id.weather_city);//城市
        windtv= (TextView) view.findViewById(R.id.weather_wind);//风向
        pmtv= (TextView) view.findViewById(R.id.weather_pm);//PM2.5
        prestv= (TextView) view.findViewById(R.id.weather_pres);//气压
        vistv= (TextView) view.findViewById(R.id.weather_vis);//能见度
        weatherView= (WeatherView) view.findViewById(R.id.weather_view);
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
            softInputHide();
        }
    }

    public void softInputHide() {
        InputMethodManager imm = ( InputMethodManager ) view.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken( ) , 0 );
        }
    }
}
