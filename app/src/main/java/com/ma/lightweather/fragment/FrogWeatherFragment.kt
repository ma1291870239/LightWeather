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
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.AppBarLayout
import com.ma.lightweather.R
import com.ma.lightweather.activity.MainActivity
import com.ma.lightweather.adapter.PopAdapter
import com.ma.lightweather.adapter.WindAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.databinding.FragFrogweatherBinding
import com.ma.lightweather.model.Air
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.SPUtils
import com.ma.lightweather.utils.WeatherUtils
import com.ma.lightweather.widget.CardTextView
import com.ma.lightweather.widget.HourFrogWeatherView
import com.ma.lightweather.widget.WeatherView
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FrogWeatherFragment: BaseFragment<FragFrogweatherBinding>() {

    private var timetv:TextView?=null
    private var maxmintmptv: TextView? = null
    private var tmptv: TextView? = null
    private var feeltv: TextView? = null
    private var condtv: TextView? = null
    private var humtv: TextView? = null
    private var pcpntv: TextView? = null
    private var citytv: TextView? = null
    private var winddirtv: TextView? = null
    private var windsctv: TextView? = null
    private var pmtv: TextView? = null
    private var prestv: TextView? = null
    private var vistv: TextView? = null
    private var pm25Tv: TextView? = null
    private var pm10Tv: TextView? = null
    private var aqiTv: TextView? = null
    private var aqiLevelTv: TextView? = null
    private var aqimainTv: TextView? = null
    private var airTv: TextView? = null
    private var sunriseTv: TextView? = null
    private var sunsetTv: TextView? = null
    private var suntimeTv: TextView? = null
    private var sunremianTv: TextView? = null


    private var weatherLife: LinearLayout? = null
    private var scrollView: NestedScrollView? = null
    private var weatherView: WeatherView? = null
    private var hourWeatherView: HourFrogWeatherView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var appBarLayout: AppBarLayout? = null
    private var relativeLayout1: LinearLayout? = null
    private var relativeLayout2: RelativeLayout? = null
    private var textView: TextView? = null
    private var ivTop: ImageView? = null
    private var ivBottom:ImageView?=null
    private var popRv: RecyclerView? = null
    private var windRv: RecyclerView? = null
    private var weatherList: List<Weather>? = null
    private var airList: List<Air>? = null
    private var city: String? = null
    private var locArea: String? = null
    private var weatherJson: String = ""
    private var weatherAqiJson: String = ""
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    private var h:Int=0

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
                    val dates=CommonUtils.changeTimeFormat(weatherList!![i].update.loc)
                    timetv?.text=dates[1]+"月"+dates[2]+"日"+" "+""+dates[3]+":"+dates[4]
                    maxmintmptv?.text="白天气温："+weatherList!![i].daily_forecast[0].tmp_max+ "℃ · "+
                            "夜晚气温："+weatherList!![i].daily_forecast[0].tmp_min+ "℃"
                    tmptv?.text = weatherList!![i].now.tmp
                    feeltv?.text ="体感温度：" +weatherList!![i].now.fl+ "℃"
                    var cond=weatherList!![i].now.cond_txt
                    //cond="阴"
                    condtv?.text =cond
                    ivTop?.setImageResource(WeatherUtils.getColorWeatherIcon(cond))
                    ivBottom?.setImageResource(WeatherUtils.getColorWeatherImg(cond))
                    relativeLayout1?.setBackgroundColor(ContextCompat.getColor(context!!,WeatherUtils.getColorWeatherBack(cond)))
                    relativeLayout1?.setBackgroundColor(ContextCompat.getColor(context!!,R.color.weather_back_sunny))
                    //(activity as MainActivity).setWeatherBack(cond)
                    humtv?.text = "" + weatherList!![i].now.hum + " %"
                    pcpntv?.text = "日降水总量  " + weatherList!![i].now.pcpn + " 毫米"
                    citytv?.text = weatherList!![i].basic.location + "·" + weatherList!![i].basic.cnty
                    winddirtv?.text =  "当前·" + weatherList!![i].now.wind_dir
                    windsctv?.text = "" + weatherList!![i].now.wind_sc+"级"
                    pmtv?.text = "" + weatherList!![i].now.wind_spd + " 公里/小时"
                    prestv?.text = "" + weatherList!![i].now.pres + " 帕"
                    vistv?.text = "" + weatherList!![i].now.vis + " 公里"
                    pm25Tv?.text = "" + airList!![i].air_now_city.pm25 + " 微克/立方米"
                    pm10Tv?.text = "" + airList!![i].air_now_city.pm10 + " 微克/立方米"
                    val aqi=airList!![i].air_now_city.aqi.toInt()
                    aqiTv?.text = "" + aqi
                    when {
                        aqi<50 -> {
                            aqiLevelTv?.text = "优"
                            aqiTv?.setTextColor(ContextCompat.getColor(mContext,R.color.aqi_1))
                        }
                        aqi in 51..100 -> {
                            aqiLevelTv?.text = "良"
                            aqiTv?.setTextColor(ContextCompat.getColor(mContext,R.color.aqi_2))
                        }
                        aqi in 101..150 -> {
                            aqiLevelTv?.text = "轻度污染"
                            aqiTv?.setTextColor(ContextCompat.getColor(mContext,R.color.aqi_3))
                        }
                        aqi in 151..200 -> {
                            aqiLevelTv?.text = "中度污染"
                            aqiTv?.setTextColor(ContextCompat.getColor(mContext,R.color.aqi_4))
                        }
                        aqi in 201..300 -> {
                            aqiLevelTv?.text = "重度污染"
                            aqiTv?.setTextColor(ContextCompat.getColor(mContext,R.color.aqi_5))
                        }
                        else -> {
                            aqiLevelTv?.text = "严重污染"
                            aqiTv?.setTextColor(ContextCompat.getColor(mContext,R.color.aqi_6))
                        }
                    }
                    aqimainTv?.text = "主要污染物 " + airList!![i].air_now_city.main
                    val sunrise=CommonUtils.getTimeFormat(weatherList!![i].daily_forecast[0].sr)
                    sunriseTv?.text=CommonUtils.change24To12(sunrise[0])+":"+sunrise[1]
                    val sunset=CommonUtils.getTimeFormat(weatherList!![i].daily_forecast[0].ss)
                    sunsetTv?.text=CommonUtils.change24To12(sunset[0])+":"+sunset[1]
                    suntimeTv?.text="白昼时长 "+CommonUtils.minutesToHours(CommonUtils.getTimeValue(weatherList!![i].daily_forecast[0].date,weatherList!![i].daily_forecast[0].sr+":00" ,weatherList!![i].daily_forecast[0].ss+":00"))
                    sunremianTv?.text="剩余的白昼时长 "+CommonUtils.minutesToHours(CommonUtils.getTimeValue(weatherList!![i].daily_forecast[0].date, SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),weatherList!![i].daily_forecast[0].ss+":00"))
                    if (weatherList!![i].lifestyle.isNotEmpty() && weatherList!![i].lifestyle.size  <=0) {
                        weatherLife?.visibility = View.GONE
                    } else {
                        weatherLife?.visibility = View.VISIBLE
                    }
                    for (j in weatherList!![i].lifestyle.indices) {
                        val lifeWeather = weatherList!![i].lifestyle[j]
                        val type = lifeWeather.type
                        if (type == getString(R.string.air_en_text)) {
                            airTv?.text=lifeWeather.txt
                        }

                    }
                    popRv?.adapter=PopAdapter(context!!,weatherList!![i].hourly)
                    windRv?.adapter= WindAdapter(context!!,weatherList!![i].hourly)

                    SPUtils.setParam(mContext, Contants.CITY, weatherList!![i].basic.location)
                    SPUtils.setParam(mContext, Contants.TMP, weatherList!![i].now.tmp)
                    SPUtils.setParam(mContext, Contants.TXT, weatherList!![i].now.cond_txt)
                    SPUtils.setParam(mContext, Contants.WEATHER_JSON, weatherJson)
                    SPUtils.setParam(mContext, Contants.WEATHER_AQI_JSON, weatherAqiJson)
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
        val view = inflater.inflate(R.layout.frag_frogweather, null)
        viewBinding= FragFrogweatherBinding.inflate(inflater,container,false)
        val weatherJson = SPUtils.getParam(context, Contants.WEATHER_JSON, "") as String
        val weatherAqiJson = SPUtils.getParam(context, Contants.WEATHER_AQI_JSON, "") as String
        arguments?.takeIf { it.containsKey("height") }?.apply {
            h = getInt("height")
        }
        if (isAdded) {
            initView(view)
            //setViewHeight()
            if(weatherJson.isNotEmpty()&&weatherAqiJson.isNotEmpty()) {
                airList = Parse.parseAir(weatherAqiJson, weatherView, hourWeatherView, mContext)
                weatherList = Parse.parseWeather(weatherJson, weatherView, hourWeatherView, mContext)
                handler.sendEmptyMessage(WEATHER_SUCCESE)
                //(activity as MainActivity).refreshCity()
            }
            if (SPUtils.getParam(mContext, Contants.NOTIFY, false) as Boolean) {
                val it = Intent(mContext, WeatherService::class.java)
                mContext.startService(it)
            }
            city=SPUtils.getParam(activity, Contants.CITY, Contants.CITYNAME) as String
            loadData(city)
            //requestLocationPermission()
        }
        return view
    }

    private fun setViewHeight() {
        val display= resources.displayMetrics
        val width :Int=display.widthPixels
        val height:Int=display.heightPixels-CommonUtils.getStatusBarHeight(requireActivity())-CommonUtils.dp2px(requireActivity(),50f+8f+8f+40f)
        Log.e(TAG, "setViewHeight: ${display.widthPixels}---${display.heightPixels}---${height}---${h}---${relativeLayout1?.measuredHeight}", )
        relativeLayout1?.layoutParams?.height=height
        relativeLayout2?.layoutParams?.height=height+CommonUtils.dp2px(activity!!,250f)
        textView?.layoutParams?.height=height
        appBarLayout?.layoutParams?.height=height+CommonUtils.dp2px(activity!!,250f)
//        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//        ivBottom?.layoutParams=lp
    }

    fun setLayourEnable(verticalOffset: Int?) {
        swipeRefreshLayout?.isEnabled = verticalOffset!! >=0
        var offset=(250+CommonUtils.px2dp(activity!!,verticalOffset.toFloat()))/250f
        if(0<offset&&offset<1) {
            ivBottom?.alpha = offset
        }
    }

    //加载上部数据
    fun loadData(city: String?) {
        this.city = city
        val requestQueue = Volley.newRequestQueue(mContext)
        val stringRequest = StringRequest(Request.Method.GET, Contants.WEATHER_AIR + city,
                Response.Listener { response ->
                    try {
                        weatherAqiJson=response
                        airList= Parse.parseAir(response, weatherView, hourWeatherView, mContext)
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
        val stringRequest = StringRequest(Request.Method.GET, Contants.WEATHER_ALL + city,
                { response ->
                    try {
                        weatherJson=response
                        weatherList= Parse.parseWeather(response, weatherView, hourWeatherView, mContext)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (weatherList!!.isNotEmpty()&& weatherList!![0].status == "ok") {
                        handler.sendEmptyMessage(WEATHER_SUCCESE)
                        //(activity as MainActivity).refreshCity()
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

    private fun initView(view: View?) {
        timetv=view?.findViewById(R.id.weather_time)//时间
        maxmintmptv=view?.findViewById(R.id.weather_maxmintmp)
        tmptv = view?.findViewById(R.id.weather_tmp)//温度
        feeltv = view?.findViewById(R.id.weather_feel)//体感
        condtv = view?.findViewById(R.id.weather_cond)//体感
        humtv = view?.findViewById(R.id.weather_hum)//湿度
        pcpntv = view?.findViewById(R.id.weather_pcpn)//降雨量
        citytv = view?.findViewById(R.id.weather_city)//城市
        winddirtv = view?.findViewById(R.id.wind_dir)//风向
        windsctv = view?.findViewById(R.id.wind_sc)//风力
        pmtv = view?.findViewById(R.id.weather_spd)//风速
        prestv = view?.findViewById(R.id.weather_pres)//气压
        vistv = view?.findViewById(R.id.weather_vis)//能见度
        pm25Tv = view?.findViewById(R.id.weather_pm25)//PM2.5
        pm10Tv = view?.findViewById(R.id.weather_pm10)//PM10
        aqiTv = view?.findViewById(R.id.weather_aqi)//空气
        aqiLevelTv= view?.findViewById(R.id.weather_aqi_level)//污染等级
        aqimainTv= view?.findViewById(R.id.weather_aqi_main)//污染
        scrollView = view?.findViewById(R.id.weather_scroll)
        weatherView = view?.findViewById(R.id.weather_view)
        hourWeatherView = view?.findViewById(R.id.hourweather_view)
        weatherLife = view?.findViewById(R.id.weather_life)
        airTv = view?.findViewById(R.id.airTextView)
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        appBarLayout=view?.findViewById(R.id.appBarLayout)
        relativeLayout1=view?.findViewById(R.id.relativeLayout1)
        ivTop=view?.findViewById(R.id.weather_iv_top)
        ivBottom=view?.findViewById(R.id.weather_iv_bottom)
        popRv= view?.findViewById(R.id.pop_rv)
        windRv= view?.findViewById(R.id.wind_rv)
        sunriseTv= view?.findViewById(R.id.weather_rise_text)
        sunsetTv= view?.findViewById(R.id.weather_set_text)
        suntimeTv= view?.findViewById(R.id.weather_sun_time)
        sunremianTv= view?.findViewById(R.id.weather_remain_time)
        val popManager=LinearLayoutManager(context)
        val windManager=LinearLayoutManager(context)
        popManager.orientation=LinearLayoutManager.HORIZONTAL
        windManager.orientation=LinearLayoutManager.HORIZONTAL
        mBinding.popRv.layoutManager=popManager
        mBinding.windRv.layoutManager=windManager
        mBinding.swipeRefreshLayout.setColorSchemeResources(WeatherUtils.getBackColor(mContext))
        appBarLayout?.addOnOffsetChangedListener (AppBarLayout.OnOffsetChangedListener { _, p1 -> setLayourEnable(p1) })
        mBinding.swipeRefreshLayout.setOnRefreshListener { loadData(city) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            val isShow=SPUtils.getParam(activity, Contants.LIFE, true) as Boolean
            if (isShow){
                weatherLife?.visibility=View.VISIBLE
            }else{
                weatherLife?.visibility=View.GONE
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