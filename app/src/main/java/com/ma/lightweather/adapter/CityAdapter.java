package com.ma.lightweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.DbUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/14.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder> {

    private Context context;
    private List<Weather> weatherList;

    public CityAdapter(Context context,List<Weather> weatherList){
        this.context=context;
        this.weatherList=weatherList;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_city,parent,false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, final int i) {
        holder.citytv.setText(weatherList.get(i).location);
        holder.tmptv.setText(weatherList.get(i).tmp+"℃");
        holder.txttv.setText(weatherList.get(i).txt+" "+weatherList.get(i).dir);
        holder.weatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof MainActivity){
                    ((MainActivity) context).refresh(weatherList.get(i).location,true);
                }
            }
        });
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city1= (String) SharedPrefencesUtils.getParam(context,Contants.CITY,Contants.CITYNAME);
                String city2=weatherList.get(i).location;
                if(weatherList.size()>1) {
                    DbUtils.deleteCity(context, weatherList.get(i).location);
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).refreshCity();
                        if(city1.equals(city2)){
                            ((MainActivity) context).refresh(weatherList.get(0).location,false);
                        }
                    }
                }else {
                    CommonUtils.showShortToast(context,"至少保留一个城市");
                }
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if(weatherList==null){
            return 0;
        }
        return weatherList.size();
    }

    class CityHolder extends RecyclerView.ViewHolder {
        private TextView citytv,tmptv,txttv;
        private LinearLayout weatherLayout;
        private ImageView iv;

        public CityHolder(View itemView) {
            super(itemView);
            weatherLayout=itemView.findViewById(R.id.item_weather);
            citytv= itemView.findViewById(R.id.item_city);
            tmptv= itemView.findViewById(R.id.item_tmp);
            txttv= itemView.findViewById(R.id.item_txt);
            iv=itemView.findViewById(R.id.item_delete);
        }
    }
}
