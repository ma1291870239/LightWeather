package com.ma.lightweather.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
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
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.SharedPrefencesUtils
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
    private var airTv: TextView? = null
    private var comfTv: TextView? = null
    private var cwTv: TextView? = null
    private var drsgTv: TextView? = null
    private var fluTv: TextView? = null
    private var sportTv: TextView? = null
    private var travTv: TextView? = null
    private var uvTv: TextView? = null
    private var weatherLife: LinearLayout? = null
    private var scrollView: NestedScrollView? = null
    private var weatherView: WeatherView? = null
    private var hourWeatherView: HourWeatherView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var weatherList: List<Weather>? = null
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.frag_weather, null)
        city = SharedPrefencesUtils.getParam(context, Contants.CITY, Contants.CITYNAME) as String
        if (isAdded) {
            initView(view)
            loadData(city)
            requestLocationPermission()
        }
        return view
    }

    //加载上部数据
    fun loadData(city: String?) {
        this.city = city
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city!!,
                Response.Listener { response ->
                    try {
                        //weatherList = Parse.parseWeather(response, weatherView, hourWeatherView, context)
                        weatherList=Parse.parse(response, weatherView, hourWeatherView, context)
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
                    swipeRefreshLayout!!.isRefreshing = false
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
        pmtv = view?.findViewById(R.id.weather_pm)//PM2.5
        prestv = view?.findViewById(R.id.weather_pres)//气压
        vistv = view?.findViewById(R.id.weather_vis)//能见度
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

        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setColorSchemeResources(CommonUtils.getBackColor(context))
        swipeRefreshLayout?.setOnRefreshListener { loadData(city) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
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
                    citytv?.text = weatherList!![i].basic.location + "　" + weatherList!![i].basic.cnty
                    windtv?.text = weatherList!![i].now.cond_txt + "　" + weatherList!![i].now.wind_dir
                    pmtv?.text = "　风速：" + weatherList!![i].now.wind_spd + " km/h"
                    prestv?.text = "　气压：" + weatherList!![i].now.pres + " Pa"
                    vistv?.text = "　能见：" + weatherList!![i].now.vis + " km"
                    if (weatherList!![i].lifestyle.isNotEmpty() && weatherList!![i].lifestyle.size  <=0) {
                        weatherLife?.visibility = View.GONE
                    } else {
                        weatherLife?.visibility = View.VISIBLE
                    }
                    for (j in weatherList!![i].lifestyle.indices) {
                        val weather = weatherList!![i]
                        val type = weather.lifestyle[j].type
                        val s = weather.lifestyle[j].brf+ "\n" + weather.lifestyle[j].txt
                        if (type == "air") {
                            setLifeView(airTv,s,"空气指数")
                        }
                        if (type == "cw" ) {
                            setLifeView(cwTv,s,"洗车指数")
                        }
                        if (type == "drsg") {
                            setLifeView(drsgTv,s,"穿衣指数")
                        }
                        if (type == "flu" ) {
                            setLifeView(fluTv,s,"感冒指数")
                        }
                        if (type == "sport") {
                            setLifeView(sportTv,s,"运动指数")
                        }
                        if (type == "trav") {
                            setLifeView(travTv,s,"旅游指数")
                        }
                        if (type == "comf") {
                            setLifeView(comfTv,s,"舒适度指数")
                        }
                        if (type == "uv") {
                            setLifeView(uvTv,s,"紫外线指数")
                        }
                    }
                    SharedPrefencesUtils.setParam(context, Contants.CITY, weatherList!![i].basic.location)
                    SharedPrefencesUtils.setParam(context, Contants.TMP, weatherList!![i].now.tmp)
                    SharedPrefencesUtils.setParam(context, Contants.TXT, weatherList!![i].now.cond_txt)
                    //CommonUtils.showShortToast(getC,"数据已更新");
                    if (SharedPrefencesUtils.getParam(context, Contants.NOTIFY, false) as Boolean) {
                        val it = Intent(context, WeatherService::class.java)
                        context.startService(it)
                    }
                }
            }
            WEATHER_NOMORE -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "请求超过每天次数")
            WEATHER_NOLOCATION -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "未找到该城市")
            WEATHER_ERROR -> CommonUtils.showShortSnackBar(swipeRefreshLayout, "服务器错误")
            WEATHER_CHANGECITY->{
                val builder=AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("")
                        .setPositiveButton("确定") { _, _ -> loadData(locArea) }
                        .setNegativeButton("取消") { p0, _ -> p0?.dismiss() }
                builder.create().show()
            }
        }
    }

    private fun setLifeView(view: TextView?, text:String ,type:String){
        if (text.isNotEmpty()){
            view?.visibility=View.VISIBLE
            view?.text=type+"    "+text
        }else{
            view?.visibility=View.GONE
            view?.text=""
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.showShortSnackBar(downloadIv, "当前没有定位权限")
            return
        }
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                getDistrictFromLocation(location)
                if (locationManager != null) {
                    locationManager?.removeUpdates(locationListener)
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

        for (s in locationManager?.allProviders!!) {
            if (s == LocationManager.NETWORK_PROVIDER) {
            }
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager?.requestSingleUpdate(LocationManager.PASSIVE_PROVIDER, locationListener, null)
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