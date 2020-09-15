package com.ma.lightweather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.ma.lightweather.R
import com.ma.lightweather.adapter.FutureDaysAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.SharedPrefencesUtils
import com.ma.lightweather.widget.HourFrogWeatherView
import kotlinx.android.synthetic.main.frag_futuredays.*

class FutureDaysFragment:BaseFragment() {

    private var expandableListView: ExpandableListView? = null
    private var weatherList: List<Weather>? = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_futuredays, null)
        expandableListView=view.findViewById(R.id.expand_lv)
        initData()
        return view
    }

    private fun initData() {
        val weatherJson=SharedPrefencesUtils.getParam(context, Contants.WEATHER_JSON, "") as String
        weatherList= Parse.parseWeather(weatherJson, null, HourFrogWeatherView(mContext,null),mContext)
        expandableListView?.setAdapter(FutureDaysAdapter(mContext,weatherList!![0].daily_forecast))
    }
}