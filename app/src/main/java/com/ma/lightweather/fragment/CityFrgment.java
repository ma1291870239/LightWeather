package com.ma.lightweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.adapter.CityAdapter;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.DbUtils;
import com.ma.lightweather.utils.Parse;
import com.ma.lightweather.widget.HourWeatherView;
import com.ma.lightweather.widget.WeatherView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/5.
 */
public class CityFrgment extends BaseFragment{

    private View view;
    private RecyclerView recyclerView;
    private WeatherView weatherView;
    private HourWeatherView hourWeatherView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Weather> weatherList=new ArrayList<>();
    private List<Weather> weatherData=new ArrayList<>();
    private CityAdapter cityAdapter;
    private String city;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_city,null);
        if(isAdded()){
            initView();
            initData();
        }
        return view;
    }

    public void initData() {
        city="";
        weatherData.clear();
        weatherData.addAll(DbUtils.selectdb(context));
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(false);
        }
        if(cityAdapter==null) {
            cityAdapter = new CityAdapter(getActivity(), weatherData);
            recyclerView.setAdapter(cityAdapter);
        }else {
            cityAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(CommonUtils.getColor());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context,R.drawable.bg_divider));
        recyclerView.addItemDecoration(divider);
    }

    private void loadData(final String city) {
        RequestQueue requestQueue=Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            weatherList= Parse.parseWeather(response,weatherView,hourWeatherView,context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(weatherList.size()>0) {

                        }else {

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isResumed()){
        }
    }

}
