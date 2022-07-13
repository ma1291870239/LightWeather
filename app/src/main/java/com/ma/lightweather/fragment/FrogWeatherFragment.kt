package com.ma.lightweather.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.Log.e
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.ma.lightweather.R
import com.ma.lightweather.activity.MainActivity
import com.ma.lightweather.adapter.PopAdapter
import com.ma.lightweather.adapter.WindAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.databinding.FragFrogweatherBinding
import com.ma.lightweather.model.Air
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.*
import com.ma.lightweather.widget.CardTextView
import com.ma.lightweather.widget.HourFrogWeatherView
import com.ma.lightweather.widget.WeatherView
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.frag_frogweather.*
import kotlinx.android.synthetic.main.item_notify_simple.*
import org.json.JSONException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FrogWeatherFragment: BaseFragment<FragFrogweatherBinding>() {

    private var weatherList: List<Weather>? = null
    private var airList: List<Air>? = null
    private var city: String? = null
    private var locArea: String? = null
    private var weatherJson: String = ""
    private var weatherAqiJson: String = ""
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var isGetHeight: Boolean=false
    private var height: Int=0
    private var newHeight:Int=0

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            updateData(msg.what)
        }
    }

    private fun updateData(code:Int){
        when (code) {
            WEATHER_SUCCESE -> {
                mBinding.swipeRefreshLayout.isRefreshing = false
                weatherList?.let{ weatherList->
                    for (i in weatherList.indices) {
                        val dates = CommonUtils.changeTimeFormat(weatherList[i].update.loc)
                        mBinding.weatherTime.text =
                            "${dates[1]}月${dates[2]}日 ${dates[3]}:${dates[4]}"
                        mBinding.weatherMaxmintmp.text =
                            "白天气温：" + weatherList[i].daily_forecast[0].tmp_max + "℃ · " +
                                    "夜晚气温：" + weatherList[i].daily_forecast[0].tmp_min + "℃"
                        mBinding.weatherTmp.text = weatherList[i].now.tmp
                        mBinding.weatherFeel.text = "体感温度：" + weatherList!![i].now.fl + "℃"
                        var cond = weatherList[i].now.cond_txt
                        mBinding.weatherCond.text = cond
                        mBinding.weatherIvTop.setImageResource(WeatherUtils.getColorWeatherIcon(cond))
                        mBinding.weatherIvBottom.setImageResource(
                            WeatherUtils.getColorWeatherImg(
                                cond
                            )
                        )

                        val backgroundColor = ContextCompat.getColor(
                            requireContext(),
                            WeatherUtils.getColorWeatherBack(cond)
                        )
                        var themeColor = ContextCompat.getColor(
                            requireContext(),
                            WeatherUtils.getColorWeatherBack(cond)
                        )
                        val vibrantSwatch = Palette.from(
                            BitmapFactory.decodeResource(
                                resources,
                                WeatherUtils.getColorWeatherImg(cond)
                            )
                        ).generate().vibrantSwatch
                        vibrantSwatch?.let {
                            themeColor = vibrantSwatch.rgb
                        }
                        mBinding.collapsingToolbarLayout.setBackgroundColor(backgroundColor)
                        setFragmentResult(
                            "backgroundColor",
                            bundleOf("backgroundColor" to themeColor)
                        )


                        mBinding.weatherHum.text = "" + weatherList[i].now.hum + " %"
                        mBinding.weatherPcpn.text = "日降水总量  " + weatherList[i].now.pcpn + " 毫米"
                        mBinding.weatherCity.text =
                            weatherList[i].basic.location + "·" + weatherList[i].basic.cnty
                        mBinding.windDir.text = "当前·" + weatherList[i].now.wind_dir
                        mBinding.windSc.text = "" + weatherList[i].now.wind_sc + "级"
                        mBinding.weatherSpd.text = "" + weatherList[i].now.wind_spd + " 公里/小时"
                        mBinding.weatherPres.text = "" + weatherList[i].now.pres + " 帕"
                        mBinding.weatherVis.text = "" + weatherList[i].now.vis + " 公里"
                        mBinding.weatherPm25.text = "" + airList!![i].air_now_city.pm25 + " 微克/立方米"
                        mBinding.weatherPm10.text = "" + airList!![i].air_now_city.pm10 + " 微克/立方米"
                        val aqi = airList!![i].air_now_city.aqi.toInt()
                        mBinding.weatherAqi.text = "" + aqi
                        when {
                            aqi < 50 -> {
                                mBinding.weatherAqiLevel.text = "优"
                                mBinding.weatherAqi.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.aqi_1
                                    )
                                )
                            }
                            aqi in 51..100 -> {
                                mBinding.weatherAqiLevel.text = "良"
                                mBinding.weatherAqi.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.aqi_2
                                    )
                                )
                            }
                            aqi in 101..150 -> {
                                mBinding.weatherAqiLevel.text = "轻度污染"
                                mBinding.weatherAqi?.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.aqi_3
                                    )
                                )
                            }
                            aqi in 151..200 -> {
                                mBinding.weatherAqiLevel.text = "中度污染"
                                mBinding.weatherAqi.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.aqi_4
                                    )
                                )
                            }
                            aqi in 201..300 -> {
                                mBinding.weatherAqiLevel.text = "重度污染"
                                mBinding.weatherAqi?.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.aqi_5
                                    )
                                )
                            }
                            else -> {
                                mBinding.weatherAqiLevel.text = "严重污染"
                                mBinding.weatherAqi.setTextColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.aqi_6
                                    )
                                )
                            }
                        }
                        mBinding.weatherAqiMain.text = "主要污染物 " + airList!![i].air_now_city.main
                        val sunrise =
                            CommonUtils.getTimeFormat(weatherList[i].daily_forecast[0].sr)
                        mBinding.weatherRise.text =
                            CommonUtils.change24To12(sunrise[0]) + ":" + sunrise[1]
                        val sunset =
                            CommonUtils.getTimeFormat(weatherList[i].daily_forecast[0].ss)
                        mBinding.weatherSet.text =
                            CommonUtils.change24To12(sunset[0]) + ":" + sunset[1]
                        mBinding.weatherSunTime.text = "白昼时长 " + CommonUtils.minutesToHours(
                            CommonUtils.getTimeValue(
                                weatherList[i].daily_forecast[0].date,
                                weatherList[i].daily_forecast[0].sr + ":00",
                                weatherList[i].daily_forecast[0].ss + ":00"
                            )
                        )
                        mBinding.weatherRemainTime.text = "剩余的白昼时长 " + CommonUtils.minutesToHours(
                            CommonUtils.getTimeValue(
                                weatherList[i].daily_forecast[0].date,
                                SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),
                                weatherList[i].daily_forecast[0].ss + ":00"
                            )
                        )
                        for (j in weatherList[i].lifestyle.indices) {
                            val lifeWeather = weatherList[i].lifestyle[j]
                            val type = lifeWeather.type
                            if (type == getString(R.string.air_en_text)) {
                                mBinding.airTextView.text = lifeWeather.txt
                            }

                        }
                        mBinding.popRv.adapter =
                            PopAdapter(requireContext(), weatherList[i].hourly)
                        mBinding.windRv.adapter =
                            WindAdapter(requireContext(), weatherList[i].hourly)

                        SPUtils.setParam(
                            requireContext(),
                            Contants.CITY,
                            weatherList[i].basic.location
                        )
                        SPUtils.setParam(requireContext(), Contants.TMP, weatherList[i].now.tmp)
                        SPUtils.setParam(
                            requireContext(),
                            Contants.TXT,
                            weatherList[i].now.cond_txt
                        )
                        SPUtils.setParam(requireContext(), Contants.WEATHER_JSON, weatherJson)
                        SPUtils.setParam(
                            requireContext(),
                            Contants.WEATHER_AQI_JSON,
                            weatherAqiJson
                        )
                        if (SPUtils.getParam(requireContext(), Contants.NOTIFY, false) as Boolean) {
                            val it = Intent(requireContext(), WeatherService::class.java)
                            requireContext().startService(it)
                        }
                    }
                }
            }
            WEATHER_NOMORE -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "请求超过每天次数")
            WEATHER_NOLOCATION -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "未找到该城市")
            WEATHER_ERROR -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "服务器错误")
            WEATHER_CHANGECITY ->{
                val builder= AlertDialog.Builder(activity)
                        .setTitle("提示")
                        .setMessage("")
                        .setPositiveButton("确定") { _, _ -> loadData(locArea) }
                        .setNegativeButton("取消") { p0, _ -> p0?.dismiss() }
                builder.create().show()
            }
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding= FragFrogweatherBinding.inflate(inflater,container,false)
        val weatherJson = SPUtils.getParam(requireContext(), Contants.WEATHER_JSON, "") as String
        val weatherAqiJson = SPUtils.getParam(requireContext(), Contants.WEATHER_AQI_JSON, "") as String
        val city=SPUtils.getParam(requireContext(), Contants.CITY, Contants.CITYNAME) as String
        if (isAdded) {
            initView()
            if(weatherJson.isNotEmpty()&&weatherAqiJson.isNotEmpty()) {
                airList = Parse.parseAir(weatherAqiJson, weatherView = null, mBinding.hourweatherView, requireContext())
                weatherList = Parse.parseWeather(weatherJson,weatherView = null, mBinding.hourweatherView, requireContext())
                handler.sendEmptyMessage(WEATHER_SUCCESE)
            }
            if (SPUtils.getParam(mContext, Contants.NOTIFY, false) as Boolean) {
                val it = Intent(mContext, WeatherService::class.java)
                mContext.startService(it)
            }
            loadData(city)
            //requestLocationPermission()
        }
        return mBinding.root
    }

    //加载上部数据
    fun loadData(city: String) {
        this.city = city
        val requestQueue = Volley.newRequestQueue(mContext)
        val stringRequest = StringRequest(Request.Method.GET, Contants.HF_WEATHER_NOW + city,
                { response ->
                    try {
                        weatherAqiJson=response
                        airList= Parse.parseAir(response, weatherView = null, mBinding.hourweatherView, requireContext())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (airList!!.isNotEmpty()&& airList!![0].status == "ok") {
                        getWeather(city)
                    } else if(airList!![0].status == "no more requests"){
                        handler.sendEmptyMessage(WEATHER_NOMORE)
                    } else if(airList!![0].status == "unknown location"){
                        handler.sendEmptyMessage(WEATHER_NOLOCATION)
                    } else{
                        handler.sendEmptyMessage(WEATHER_ERROR)
                    }
                },
                {
                    swipeRefreshLayout?.isRefreshing = false
                    handler.sendEmptyMessage(WEATHER_ERROR)
                })
        requestQueue.add(stringRequest)
    }


    private fun getWeather(city:String?){
        val requestQueue = Volley.newRequestQueue(mContext)
        val stringRequest = StringRequest(Request.Method.GET, Contants.WEATHER_ALL + city,
                { response ->
                    try {
                        weatherJson=response
                        weatherList= Parse.parseWeather(response, weatherView = null, mBinding.hourweatherView, mContext)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (weatherList!!.isNotEmpty()&& weatherList!![0].status == "ok") {
                        handler.sendEmptyMessage(WEATHER_SUCCESE)
                    } else if(weatherList!![0].status == "no more requests"){
                        handler.sendEmptyMessage(WEATHER_NOMORE)
                    } else if(weatherList!![0].status == "unknown location"){
                        handler.sendEmptyMessage(WEATHER_NOLOCATION)
                    } else{
                        handler.sendEmptyMessage(WEATHER_ERROR)
                    }
                },
                {
                    swipeRefreshLayout?.isRefreshing = false
                    handler.sendEmptyMessage(WEATHER_ERROR)
                })
        requestQueue.add(stringRequest)

    }

    private fun initView() {
        val popManager=LinearLayoutManager(context)
        val windManager=LinearLayoutManager(context)
        popManager.orientation=LinearLayoutManager.HORIZONTAL
        windManager.orientation=LinearLayoutManager.HORIZONTAL
        mBinding.popRv.layoutManager=popManager
        mBinding.windRv.layoutManager=windManager
        mBinding.swipeRefreshLayout.setColorSchemeResources(WeatherUtils.getBackColor(mContext))
        mBinding.collapsingToolbarLayout.setOnClickListener {

        }
        mBinding.appBarLayout.addOnOffsetChangedListener (AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            mBinding.swipeRefreshLayout.isEnabled = verticalOffset >=0
            var offset=1+verticalOffset.toFloat()/mBinding.hourweatherView.measuredHeight
            e(TAG, "getOffset: ${verticalOffset.toFloat()}---${mBinding.hourweatherView.measuredHeight}---${offset}")
            if(0<offset&&offset<1) {
                mBinding.weatherIvBottom.alpha = offset
            }
        })
        mBinding.swipeRefreshLayout.setOnRefreshListener { loadData(city) }
        mBinding.hourweatherView.viewTreeObserver.addOnGlobalLayoutListener {

            if (!isGetHeight
                &&mBinding.appBarLayout.measuredHeight!=0
                &&mBinding.hourweatherView.measuredHeight!=0) {
                LogUtils.e("getHeight: ${mBinding.appBarLayout.measuredHeight}---${mBinding.weatherIvBottom.measuredHeight}---${mBinding.hourweatherView.measuredHeight}", )
                isGetHeight=true
                height = mBinding.appBarLayout.measuredHeight+1
                newHeight = mBinding.appBarLayout.measuredHeight + mBinding.hourweatherView.measuredHeight
                mBinding.appBarLayout.layoutParams.height = newHeight
                mBinding.collapsingToolbarLayout.layoutParams.height = newHeight
                mBinding.relativeLayout1.layoutParams.height = height
            }
        }
    }


    companion object {
        private const val WEATHER_CHANGECITY = 14
        private const val WEATHER_ERROR = 13
        private const val WEATHER_NOMORE = 12
        private const val WEATHER_NOLOCATION = 11
        private const val WEATHER_SUCCESE = 200
    }

}