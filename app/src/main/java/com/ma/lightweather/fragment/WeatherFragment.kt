package com.ma.lightweather.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ma.lightweather.R
import com.ma.lightweather.activity.MainActivity
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.model.Air
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.SPUtils
import com.ma.lightweather.utils.WeatherUtils
import com.ma.lightweather.widget.CardTextView
import com.ma.lightweather.widget.HourWeatherView
import com.ma.lightweather.widget.WeatherView
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONException
import java.io.IOException
import java.util.*


/**
 * Created by Ma-PC on 2016/12/5.
 */
class WeatherFragment : BaseFragment() {

    
    private var tmptv: TextView? = null
    private var feeltv: TextView? = null
    private var humtv: TextView? = null
    private var pcpntv: TextView? = null
    private var citytv: TextView? = null
    private var windtv: TextView? = null
    private var pmtv: TextView? = null
    private var prestv: TextView? = null
    private var vistv: TextView? = null
    private var pm25Tv: TextView? = null
    private var pm10Tv: TextView? = null
    private var aqiTv: TextView? = null
    private var mainTv: TextView? = null
    private var airTv: CardTextView? = null
    private var comfTv: CardTextView? = null
    private var cwTv: CardTextView? = null
    private var drsgTv: CardTextView? = null
    private var fluTv: CardTextView? = null
    private var sportTv: CardTextView? = null
    private var travTv: CardTextView? = null
    private var uvTv: CardTextView? = null
    private var weatherLayout: LinearLayout? = null

    private var weatherLife: LinearLayout? = null
    private var scrollView: NestedScrollView? = null
    private var weatherView: WeatherView? = null
    private var hourWeatherView: HourWeatherView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var weatherList: List<Weather>? = null
    private var airList: List<Air>? = null
    private var city: String? = null
    private var locArea: String? = null
    private var locationManager:LocationManager? = null
    private var locationListener:LocationListener? = null
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            updateData(msg.what)
        }
    }

    private fun updateData(code:Int){
        when (code) {
            WEATHER_SUCCESE -> {
                swipeRefreshLayout?.isRefreshing = false
                scrollView?.scrollTo(0, 0)
                for (i in weatherList!!.indices) {
                    tmptv?.text = weatherList!![i].now.tmp+ "℃"
                    feeltv?.text = "　体感：" + weatherList!![i].now.fl + " ℃"
                    humtv?.text = "　湿度：" + weatherList!![i].now.hum + " %"
                    pcpntv?.text = "　降雨：" + weatherList!![i].now.pcpn + " mm"
                    citytv?.text = weatherList!![i].basic.location + "　" + airList!![i].air_now_city.qlty
                    windtv?.text = weatherList!![i].now.cond_txt + "　" + weatherList!![i].now.wind_dir+ "　" + weatherList!![i].now.wind_sc+"级"
                    pmtv?.text = "　风速：" + weatherList!![i].now.wind_spd + " km/h"
                    prestv?.text = "　气压：" + weatherList!![i].now.pres + " Pa"
                    vistv?.text = "　能见：" + weatherList!![i].now.vis + " km"
                    pm25Tv?.text = "　PM25：" + airList!![i].air_now_city.pm25 + " μg/m3"
                    pm10Tv?.text = "　PM10：" + airList!![i].air_now_city.pm10 + " μg/m3"
                    aqiTv?.text = "　空气质量：" + airList!![i].air_now_city.aqi
                    mainTv?.text = "　主要污染：" + airList!![i].air_now_city.main
                    if (weatherList!![i].lifestyle.isNotEmpty() && weatherList!![i].lifestyle.size  <=0) {
                        weatherLife?.visibility = View.GONE
                    } else {
                        weatherLife?.visibility = View.VISIBLE
                    }
                    for (j in weatherList!![i].lifestyle.indices) {
                        val lifeWeather = weatherList!![i].lifestyle[j]
                        val type = lifeWeather.type
                        if (type == getString(R.string.comf_en_text)) {
                            setLifeView(R.drawable.ic_comf_index,comfTv,lifeWeather, getString(R.string.comf_ch_text))
                        }
                        if (type == getString(R.string.uv_en_text)) {
                            setLifeView(R.drawable.ic_uv_index,uvTv,lifeWeather, getString(R.string.uv_ch_text))
                        }
                        if (type == getString(R.string.air_en_text)) {
                            setLifeView(R.drawable.ic_air_index,airTv,lifeWeather, getString(R.string.air_ch_text))
                        }
                        if (type == getString(R.string.cw_en_text)) {
                            setLifeView(R.drawable.ic_cw_index,cwTv,lifeWeather, getString(R.string.cw_ch_text))
                        }
                        if (type == getString(R.string.drsg_en_text)) {
                            setLifeView(R.drawable.ic_drsg_index,drsgTv,lifeWeather, getString(R.string.drsg_ch_text))
                        }
                        if (type == getString(R.string.flu_en_text)) {
                            setLifeView(R.drawable.ic_flu_index,fluTv,lifeWeather, getString(R.string.flu_ch_text))
                        }
                        if (type == getString(R.string.sport_en_text)) {
                            setLifeView(R.drawable.ic_sport_index,sportTv,lifeWeather, getString(R.string.sport_ch_text))
                        }
                        if (type == getString(R.string.trav_en_text)) {
                            setLifeView(R.drawable.ic_trav_index,travTv,lifeWeather, getString(R.string.trav_ch_text))
                        }

                    }
                    SPUtils.setParam(mContext, Contants.CITY, weatherList!![i].basic.location)
                    SPUtils.setParam(mContext, Contants.TMP, weatherList!![i].now.tmp)
                    SPUtils.setParam(mContext, Contants.TXT, weatherList!![i].now.cond_txt)
                    //CommonUtils.showShortToast(getC,"数据已更新");
                    if (SPUtils.getParam(mContext, Contants.NOTIFY, false) as Boolean) {
                        val it = Intent(mContext, WeatherService::class.java)
                        mContext.startService(it)
                    }
                }
            }
            WEATHER_NOMORE -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "请求超过每天次数")
            WEATHER_NOLOCATION -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "未找到该城市")
            WEATHER_ERROR -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "服务器错误")
            WEATHER_CHANGECITY->{
                val builder=AlertDialog.Builder(activity)
                        .setTitle("提示")
                        .setMessage("")
                        .setPositiveButton("确定") { _, _ -> loadData(locArea) }
                        .setNegativeButton("取消") { p0, _ -> p0?.dismiss() }
                builder.create().show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_weather, null)
        city = SPUtils.getParam(mContext, Contants.CITY, Contants.CITYNAME) as String
        if (isAdded) {
            initView(view)
            loadData(city)
            //requestLocationPermission()
        }
        return view
    }

    //加载上部数据
    fun loadData(city: String?) {
        this.city = city
        val requestQueue = Volley.newRequestQueue(mContext)
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_AIR + city,
                Response.Listener { response ->
                    try {
                        airList=Parse.parseAir(response, weatherView, hourWeatherView, mContext)
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
                Response.ErrorListener {
                    swipeRefreshLayout?.isRefreshing = false
                    handler.sendEmptyMessage(WEATHER_ERROR)
                })
        requestQueue.add(stringRequest)
    }


    private fun getWeather(city:String?){
        val requestQueue = Volley.newRequestQueue(mContext)
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city,
                Response.Listener { response ->
                    try {
                        //weatherList = Parse.parseWeather(response, weatherView, hourWeatherView, context)
                        weatherList=Parse.parseWeather(response, weatherView, hourWeatherView, mContext)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (weatherList!!.isNotEmpty()&& weatherList!![0].status == "ok") {
                        handler.sendEmptyMessage(WEATHER_SUCCESE)
                        (activity as MainActivity).refreshCity()
                    } else if(weatherList!![0].status == "no more requests"){
                        handler.sendEmptyMessage(WEATHER_NOMORE)
                    } else if(weatherList!![0].status == "unknown location"){
                        handler.sendEmptyMessage(WEATHER_NOLOCATION)
                    } else{
                        handler.sendEmptyMessage(WEATHER_ERROR)
                    }
                },
                Response.ErrorListener {
                    swipeRefreshLayout?.isRefreshing = false
                    handler.sendEmptyMessage(WEATHER_ERROR)
                })
        requestQueue.add(stringRequest)

    }

    private fun initView(view: View?) {
        tmptv = view?.findViewById(R.id.weather_tmp)//温度
        feeltv = view?.findViewById(R.id.weather_feel)//体感
        humtv = view?.findViewById(R.id.weather_hum)//湿度
        pcpntv = view?.findViewById(R.id.weather_pcpn)//降雨量
        citytv = view?.findViewById(R.id.weather_city)//城市
        windtv = view?.findViewById(R.id.weather_wind)//风向
        pmtv = view?.findViewById(R.id.weather_spd)//风速
        prestv = view?.findViewById(R.id.weather_pres)//气压
        vistv = view?.findViewById(R.id.weather_vis)//能见度
        pm25Tv = view?.findViewById(R.id.weather_pm25)//PM2.5
        pm10Tv = view?.findViewById(R.id.weather_pm10)//PM10
        aqiTv = view?.findViewById(R.id.weather_aqi)//空气
        mainTv= view?.findViewById(R.id.weather_main)//污染
        scrollView = view?.findViewById(R.id.weather_scroll)
        weatherView = view?.findViewById(R.id.weather_view)
        hourWeatherView = view?.findViewById(R.id.hourweather_view)
        weatherLife = view?.findViewById(R.id.weather_life)
        airTv = view?.findViewById(R.id.airTextView)
        comfTv = view?.findViewById(R.id.comfTextView)
        cwTv = view?.findViewById(R.id.cwTextView)
        drsgTv = view?.findViewById(R.id.drsgTextView)
        fluTv = view?.findViewById(R.id.fluTextView)
        sportTv = view?.findViewById(R.id.sportTextView)
        travTv = view?.findViewById(R.id.travTextView)
        uvTv = view?.findViewById(R.id.uvTextView)
        travTv = view?.findViewById(R.id.travTextView)
        uvTv = view?.findViewById(R.id.uvTextView)
        weatherLayout=view?.findViewById(R.id.weatherLayout)
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        weatherLayout?.setBackgroundResource(WeatherUtils.getTextColor(mContext))
        swipeRefreshLayout?.setColorSchemeResources(WeatherUtils.getBackColor(mContext))

        swipeRefreshLayout?.setOnRefreshListener { loadData(city) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            val closeLife=SPUtils.getParam(activity, Contants.LIFE, false) as Boolean
            if (closeLife){
                weatherLife?.visibility=View.GONE
            }else{
                weatherLife?.visibility=View.VISIBLE
            }
        }
    }

    private fun setLifeView(icon:Int,view: CardTextView?, lifeWeather: Weather.LifeWeather ,text:String){
        if (text.isNotEmpty()){
            view?.visibility=View.VISIBLE
            val type=text+"  "+lifeWeather.brf
            view?.setText(icon,type,lifeWeather.txt)
        }else{
            view?.visibility=View.GONE
            view?.setText(0,"","")
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.showShortSnackBar(downloadIv, "当前没有定位权限")
            return
        }
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                getDistrictFromLocation(location)
                if (locationManager != null) {
                    locationListener?.let { locationManager?.removeUpdates(it) }
                    locationManager = null
                    locationListener = null
                }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onProviderDisabled(provider: String) {

            }
        }
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isSpeedRequired = false
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isCostAllowed = false
        criteria.powerRequirement = Criteria.POWER_LOW
        locationManager?.getBestProvider(criteria, true)
        locationManager?.requestSingleUpdate(LocationManager.PASSIVE_PROVIDER, locationListener as LocationListener,null)
    }

    private fun getDistrictFromLocation(location: Location?) {
        if (location == null) {
            return
        }
        try {
            val geocoder = Geocoder(mContext, Locale.getDefault())
            val latitude = location.latitude
            val longitude = location.longitude
            val addressList = geocoder.getFromLocation(latitude, longitude, 2)
            if (addressList.size > 0) {
                val address = addressList[0]
                val locProvince = address.adminArea
                val locCity = address.locality
                locArea = address.subLocality
                if(locArea?.isNotEmpty()!! &&city!=locArea){
                    handler.sendEmptyMessage(WEATHER_CHANGECITY)
                }
            }
        } catch (e: IOException) {
            CommonUtils.showShortSnackBar(downloadIv, "定位失败")
            e.printStackTrace()
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
