package com.ma.lightweather.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
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
import org.json.JSONException


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
    private var weatherList: List<Weather.WeatherBean>? = null
    private var city: String? = null
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            updateData(msg.what)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.frag_weather, null)
        city = SharedPrefencesUtils.getParam(context, Contants.CITY, Contants.CITYNAME) as String
        if (isAdded) {
            initView(view)
            loadData(city)
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

                    if (weatherList!!.size > 0) {
                        handler.sendEmptyMessage(WEATHER_CODE)
                        (activity as MainActivity).refreshCity()
                    } else {
                        handler.sendEmptyMessage(NOCITY_CODE)
                    }
                },
                Response.ErrorListener { swipeRefreshLayout!!.isRefreshing = false })
        requestQueue.add(stringRequest)
    }

    private fun initView(view: View) {
        tmptv = view!!.findViewById(R.id.weather_tmp)//温度
        feeltv = view!!.findViewById(R.id.weather_feel)//体感
        humtv = view!!.findViewById(R.id.weather_hum)//湿度
        pcpntv = view!!.findViewById(R.id.weather_pcpn)//降雨量
        citytv = view!!.findViewById(R.id.weather_city)//城市
        windtv = view!!.findViewById(R.id.weather_wind)//风向
        pmtv = view!!.findViewById(R.id.weather_pm)//PM2.5
        prestv = view!!.findViewById(R.id.weather_pres)//气压
        vistv = view!!.findViewById(R.id.weather_vis)//能见度
        scrollView = view!!.findViewById(R.id.weather_scroll)
        weatherView = view!!.findViewById(R.id.weather_view)
        hourWeatherView = view!!.findViewById(R.id.hourweather_view)
        weatherLife = view!!.findViewById(R.id.weather_life)
        airTv = view!!.findViewById(R.id.airTextView)
        comfTv = view!!.findViewById(R.id.comfTextView)
        cwTv = view!!.findViewById(R.id.cwTextView)
        drsgTv = view!!.findViewById(R.id.drsgTextView)
        fluTv = view!!.findViewById(R.id.fluTextView)
        sportTv = view!!.findViewById(R.id.sportTextView)
        travTv = view!!.findViewById(R.id.travTextView)
        uvTv = view!!.findViewById(R.id.uvTextView)

        swipeRefreshLayout = view!!.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout!!.setColorSchemeResources(CommonUtils.getBackColor(context))
        swipeRefreshLayout!!.setOnRefreshListener { loadData(city) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
        }
    }

    private fun updateData(code:Int){
        when (code) {
            WEATHER_CODE -> {
                swipeRefreshLayout!!.isRefreshing = false
                scrollView!!.scrollTo(0, 0)
                for (i in weatherList!!.indices) {
                    tmptv!!.text = weatherList!![i].now?.tmp+ "℃"
                    feeltv!!.text = "　体感：" + weatherList!![i].now?.fl + " ℃"
                    humtv!!.text = "　湿度：" + weatherList!![i].now?.hum + " %"
                    pcpntv!!.text = "　降雨：" + weatherList!![i].now?.pcpn + " mm"
                    citytv!!.text = weatherList!![i].basic?.location + "　" + weatherList!![i].basic?.cnty
                    windtv!!.text = weatherList!![i].now?.cond_txt + "　" + weatherList!![i].now?.wind_dir
                    pmtv!!.text = "　风速：" + weatherList!![i].now?.wind_spd + " km/h"
                    prestv!!.text = "　气压：" + weatherList!![i].now?.pres + " Pa"
                    vistv!!.text = "　能见：" + weatherList!![i].now?.vis + " km"
                    if (weatherList!![i].lifestyle?.size!! <=0) {
                        weatherLife!!.visibility = View.GONE
                    } else {
                        weatherLife!!.visibility = View.VISIBLE
                    }
                    for (j in weatherList!![i].lifestyle?.indices!!) {
                        val weather = weatherList!![i]
                        val type = weather.lifestyle?.get(j)?.type
                        val s =weather.lifestyle?.get(j)?.brf+ "\n" + weather.lifestyle?.get(j)?.txt
                        if (type == "air" && !TextUtils.isEmpty(s)) {
                            airTv!!.visibility = View.VISIBLE
                            airTv!!.text = "空气指数　　$s"
                        } else {
                            airTv!!.visibility = View.GONE
                            airTv!!.text = ""
                        }
                        if (type == "cw" && !TextUtils.isEmpty(s)) {
                            cwTv!!.visibility = View.VISIBLE
                            cwTv!!.text = "洗车指数　　$s"
                        } else {
                            cwTv!!.visibility = View.GONE
                            cwTv!!.text = ""
                        }
                        if (type == "drsg" && !TextUtils.isEmpty(s)) {
                            drsgTv!!.visibility = View.VISIBLE
                            drsgTv!!.text = "穿衣指数　　$s"
                        } else {
                            drsgTv!!.visibility = View.GONE
                            drsgTv!!.text = ""
                        }
                        if (type == "flu" && !TextUtils.isEmpty(s)) {
                            fluTv!!.visibility = View.VISIBLE
                            fluTv!!.text = "感冒指数　　$s"
                        } else {
                            fluTv!!.visibility = View.GONE
                            fluTv!!.text = ""
                        }
                        if (type == "sport" && !TextUtils.isEmpty(s)) {
                            sportTv!!.visibility = View.VISIBLE
                            sportTv!!.text = "运动指数　　$s"
                        } else {
                            sportTv!!.visibility = View.GONE
                            sportTv!!.text = ""
                        }
                        if (type == "trav" && !TextUtils.isEmpty(s)) {
                            travTv!!.visibility = View.VISIBLE
                            travTv!!.text = "旅游指数　　$s"
                        } else {
                            travTv!!.visibility = View.GONE
                            travTv!!.text = ""
                        }
                        if (type == "comf" && !TextUtils.isEmpty(s)) {
                            comfTv!!.visibility = View.VISIBLE
                            comfTv!!.text = "舒适度指数　$s"
                        } else {
                            comfTv!!.visibility = View.GONE
                            comfTv!!.text = ""
                        }
                        if (type == "uv" && !TextUtils.isEmpty(s)) {
                            uvTv!!.visibility = View.VISIBLE
                            uvTv!!.text = "紫外线指数　$s"
                        } else {
                            uvTv!!.visibility = View.GONE
                            uvTv!!.text = ""
                        }
                    }
                    weatherList!![i].basic?.location.let { SharedPrefencesUtils.setParam(context, Contants.CITY, it!!) }
                    weatherList!![i].now?.tmp.let { SharedPrefencesUtils.setParam(context, Contants.TMP, it!!) }
                    weatherList!![i].now?.cond_txt.let { SharedPrefencesUtils.setParam(context, Contants.TXT, it!!) }
                    //CommonUtils.showShortToast(getC,"数据已更新");
                    if (SharedPrefencesUtils.getParam(context, Contants.NOTIFY, false) as Boolean) {
                        val it = Intent(context, WeatherService::class.java)
                        context.startService(it)
                    }
                }
            }
            NOCITY_CODE -> CommonUtils.showShortToast(context, "未找到该城市")
        }
    }

    companion object {
        private const val WEATHER_CODE = 200
        private const val NOCITY_CODE = 100
    }

}
