package com.ma.lightweather.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.model.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/14.
 */
public class CityAdapter extends BaseAdapter {

    private MainActivity mainActivity;
    private List<Weather> weatherList;
    private String city;

    public CityAdapter(MainActivity mainActivity,List<Weather> weatherList){
        this.mainActivity=mainActivity;
        this.weatherList=weatherList;
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Log.e("abc", "getView: "+i+weatherList.get(i).city );
        ViewHolder vh=null;
        if(view==null){
            vh=new ViewHolder();
            view= LayoutInflater.from(mainActivity).inflate(R.layout.item_city,null);
            vh.weatherLayout=view.findViewById(R.id.item_weather);
            vh.citytv= (TextView) view.findViewById(R.id.item_city);
            vh.tmptv= (TextView) view.findViewById(R.id.item_tmp);
            vh.txttv= (TextView) view.findViewById(R.id.item_txt);
            vh.iv=view.findViewById(R.id.item_delete);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        vh.citytv.setText(weatherList.get(i).city);
        vh.tmptv.setText(weatherList.get(i).tmp+"℃");
        vh.txttv.setText(weatherList.get(i).txt+" "+weatherList.get(i).dir);
        vh.weatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.refresh(weatherList.get(i).city);
            }
        });
        vh.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.deleteCity(city,weatherList,i);
            }
        });

        return view;
    }

    class ViewHolder{
        private TextView citytv,tmptv,txttv;
        private LinearLayout weatherLayout;
        private ImageView iv;
    }
}
