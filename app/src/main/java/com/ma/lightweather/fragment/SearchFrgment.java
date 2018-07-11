package com.ma.lightweather.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.adapter.CityAdapter;
import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.model.Contants;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.Parse;
import com.ma.lightweather.widget.WeatherView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/5.
 */
public class SearchFrgment extends BaseFragment{

    private View view;
    private LinearLayout rootView;
    private SearchView sv;
    private ListView lv;
    private WeatherView weatherView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Weather> weatherList;
    private List<Weather> weatherData=new ArrayList<>();
    private CityAdapter cityAdapter;
    private MydataBaseHelper dbHelper;
    private MainActivity mainActivity;
    private String city;
    private boolean isdelete=false;
    private static final int WEATHERLIST_CODE=200;
    private static final int NOCITY_CODE=100;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what){
                case WEATHERLIST_CODE:
                    if(cityAdapter==null) {
                        mainActivity= (MainActivity) getActivity();
                        cityAdapter = new CityAdapter(mainActivity, weatherData);
                        lv.setAdapter(cityAdapter);
                    }else{
                        cityAdapter.notifyDataSetChanged();
                    }
                    if(!isdelete) {
                        mainActivity = (MainActivity) getActivity();
                        mainActivity.refresh(city);
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
        view=inflater.inflate(R.layout.frag_search,null);
        if(!isAdded()){
            return view;
        }
        city=getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).getString(Contants.CITY,"洛阳");
        initView();
        loadData(city);
        return view;
    }


    public void selectdb(boolean isdelete) {
        this.isdelete=isdelete;
        weatherData.clear();
        dbHelper=new MydataBaseHelper(getActivity(),"Weather.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("weather",null,null,null,null,null,"id desc");
        if(cursor.moveToFirst()){
            do{
                Weather weather=new Weather();
                weather.city=cursor.getString(cursor.getColumnIndex("city"));
                weather.tmp=cursor.getString(cursor.getColumnIndex("tmp"));
                weather.txt=cursor.getString(cursor.getColumnIndex("txt"));
                weather.dir=cursor.getString(cursor.getColumnIndex("dir"));
                if(weather.city!=null)
                    weatherData.add(weather);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        handler.sendEmptyMessage(WEATHERLIST_CODE);
    }

    private void createdb() {
        dbHelper=new MydataBaseHelper(getActivity(),"Weather.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.beginTransaction();
        for (int i=0;i<weatherList.size();i++){
            ContentValues values = new ContentValues();
            values.put("city", weatherList.get(i).city);
            values.put("tmp", weatherList.get(i).tmp);
            values.put("txt", weatherList.get(i).txt);
            values.put("dir", weatherList.get(i).dir);
            db.delete("weather","city = ?",new String[]{weatherList.get(i).city});
            db.insert("weather",null,values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private void initView() {
        sv= (SearchView) view.findViewById(R.id.search_sv);
        lv= (ListView) view.findViewById(R.id.search_lv);
        rootView=view.findViewById(R.id.rootView);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.background));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(city);
            }
        });
        if(sv==null){
            return;
        }else{
            //获取到TextView的ID
            int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
            //获取到TextView的控件
            TextView textView = (TextView) sv.findViewById(id);
            //设置字体大小为14sp
            textView.setTextSize(14);
            //设置字体颜色
            textView.setTextColor(getActivity().getResources().getColor(R.color.background));
            //设置提示文字颜色
            textView.setHintTextColor(getActivity().getResources().getColor(R.color.background));
        }
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                city=query;
                loadData(query);
                sv.clearFocus();
                sv.setQuery("",false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void loadData(final String city) {
        RequestQueue requestQueue=Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city + Contants.KEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            weatherList= Parse.parseWeather(response,weatherView,getActivity());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(weatherList.size()>0) {
                            createdb();
                            selectdb(false);
                        }else {
                            handler.sendEmptyMessage(NOCITY_CODE);
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


}
