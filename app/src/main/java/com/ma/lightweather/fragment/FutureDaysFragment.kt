package com.ma.lightweather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ma.lightweather.R
import com.ma.lightweather.adapter.FutureDaysAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.databinding.FragFuturedaysBinding
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.SPUtils
import com.ma.lightweather.utils.WeatherUtils
import com.ma.lightweather.widget.HourFrogWeatherView

class FutureDaysFragment:BaseFragment<FragFuturedaysBinding>() {

    private var expandableListView: ExpandableListView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var weatherList: List<Weather>? = listOf()
    private var futureDaysAdapter: FutureDaysAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_futuredays, null)
        expandableListView=view.findViewById(R.id.expand_lv)
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setColorSchemeResources(WeatherUtils.getBackColor(mContext))
        swipeRefreshLayout?.setOnRefreshListener { initData() }
        initData()
        return view
    }

    private fun initData() {
        val weatherJson = SPUtils.getParam(context, Contants.WEATHER_JSON, "") as String
        if (weatherJson.isEmpty()) {
            return
        }
        weatherList = Parse.parseWeather(weatherJson, null, HourFrogWeatherView(mContext, null), mContext)
        swipeRefreshLayout?.isRefreshing = false
        if (futureDaysAdapter == null) {
            futureDaysAdapter=FutureDaysAdapter(mContext, weatherList!![0].daily_forecast)
            expandableListView?.setAdapter(futureDaysAdapter)
        }else{
            futureDaysAdapter?.notifyDataSetChanged()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            initData()
        }
    }
}