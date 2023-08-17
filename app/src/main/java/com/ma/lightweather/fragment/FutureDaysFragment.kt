package com.ma.lightweather.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.databinding.FragFuturedaysBinding
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.adapter.FutureDaysAdapter
import com.ma.lightweather.utils.SPUtils
import com.ma.lightweather.utils.WeatherUtils

class FutureDaysFragment: BaseFragment<FragFuturedaysBinding>() {

    private var expandableListView: ExpandableListView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var hfWeather: HFWeather
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
        hfWeather= HFWeather()
        val weatherJson = SPUtils.getParam(context, Contants.WEATHER_JSON, "") as String
        if (weatherJson.isNullOrEmpty()) {
            return
        }
        hfWeather=Gson().fromJson(weatherJson, HFWeather::class.java)
        swipeRefreshLayout?.isRefreshing = false
        if (futureDaysAdapter == null) {
            futureDaysAdapter= FutureDaysAdapter(mContext, hfWeather.daily)
            expandableListView?.setAdapter(futureDaysAdapter)
        }else{
            futureDaysAdapter?.notifyDataSetChanged()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.e(TAG, "setUserVisibleHint: $isVisibleToUser" )
        if (isVisibleToUser && isResumed) {
            initData()
        }
    }
}