package com.ma.lightweather.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ma.lightweather.fragment.FrogWeatherFragment
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.model.Weather
import kotlinx.android.synthetic.main.frag_frogweather.*
import org.json.JSONException


object VolleyUtils {

    private var requestQueue: RequestQueue? = null

    fun <T> requestGet(context:Context, url:String,t: Class<T>, volleySuccess: (t: Class<T>?)->Unit, volleyError:(error:String?)->Unit){
        if (requestQueue==null) {
            requestQueue = Volley.newRequestQueue(context)
        }
        var stringRequest = StringRequest(Request.Method.GET,url,
            { response ->
                if (response.isNullOrEmpty()){
                    LogUtils.e(response)
                    val bean=Gson().fromJson<T>(response,t::class.java)


                }else{
                    volleySuccess(null)
                }
            },
            {
                LogUtils.e(it.message?:"error")
                volleyError(it.message)
            })
        requestQueue?.add(stringRequest);
    }

    fun  requestGetHFWearher(context:Context, url:String, volleySuccess: (hfWeatherBean: HFWeather?, code:String?, msg:String?)->Unit, volleyError:(error:String?)->Unit){
        if (requestQueue==null) {
            requestQueue = Volley.newRequestQueue(context)
        }
        var stringRequest = StringRequest(Request.Method.GET,url,
            { response ->
                if (!response.isNullOrEmpty()){
                    LogUtils.e(response)
                    val hfWeatherBean: HFWeather = Gson().fromJson(response,HFWeather::class.java)
                    val code=hfWeatherBean.code
                    var s=""
                    when (code){
                        "200" ->s="OK"
                        "204" ->s="暂无数据"
                        "400" ->s="参数缺失"
                        "401" ->s="认证失败"
                        "402" ->s="余额不足"
                        "403" ->s="暂无权限"
                        "404" ->s="暂无城市"
                        "429" ->s="超过次数"
                        "500" ->s="服务超时"
                    }
                    volleySuccess(hfWeatherBean,code,s)
                }else{
                    volleyError("服务器错误")
                }
            },
            {
                LogUtils.e(it.message?:"error")
                volleyError(it.message)
            })
        requestQueue?.add(stringRequest);
    }



}