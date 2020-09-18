package com.ma.lightweather.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.fragment.FrogWeatherFragment
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.PhotoUtils
import com.ma.lightweather.utils.SharedPrefencesUtils
import org.json.JSONException
import java.util.concurrent.ExecutionException

class SplashActivity : BaseActivity(), View.OnClickListener {

    private var backIv: ImageView? = null
    private var downloadIv: ImageView? = null
    private var skipTv: TextView? = null
    private var countDownTimer: CountDownTimer? = null
    private var mPermissionList = arrayListOf<String>()
    private val permissions= arrayOf(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

    private val DOWNLOAD_CODE = 200
    private val PERMISSION_CODE = 13
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                DOWNLOAD_CODE -> CommonUtils.showShortSnackBar(downloadIv, getString(R.string.splash_save_text))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        initView()
        getBingImg()
        loadData()
        checkPermission()
    }

    private fun getBingImg() {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, Contants.BINGURL,
                Response.Listener<String> { response ->
                    SharedPrefencesUtils.setParam(this, Contants.BING, response)
                },
                Response.ErrorListener {

                })
        requestQueue.add(stringRequest)
    }

    private fun initView() {
        backIv = findViewById(R.id.backIv)
        downloadIv = findViewById(R.id.downloadIv)
        skipTv = findViewById(R.id.skipTv)
        downloadIv?.setOnClickListener(this)
        skipTv?.setOnClickListener(this)
    }

    private fun initData() {
        Glide.with(this)
                .load(SharedPrefencesUtils.getParam(this, Contants.BING, ""))
                .error(R.mipmap.splash)
                .into(backIv!!)
        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(l: Long) {
                skipTv?.text = getString(R.string.splash_skip_text,l/1000)
            }

            override fun onFinish() {
                toMain()
            }
        }
        countDownTimer?.start()
    }


    private fun checkPermission() {
        mPermissionList.clear()
        for(permission in permissions){
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(permission)
            }
        }
        if (mPermissionList.size>0){
            ActivityCompat.requestPermissions(this,permissions, PERMISSION_CODE)
        }else{
            initData()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.downloadIv -> {
                if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    CommonUtils.showShortSnackBar(downloadIv, getString(R.string.splash_read_permission_text))
                    return
                }
                var url:String=SharedPrefencesUtils.getParam(this, Contants.BING, "") as String
                if(url.isNullOrEmpty()){
                    return
                }
                Thread(Runnable {
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = Glide.with(this)
                                .load(url)
                                .asBitmap()
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    }

                    val isSave = PhotoUtils.saveImageToGallery(this, bitmap!!)
                    if (isSave) {
                        handler.sendEmptyMessage(DOWNLOAD_CODE)
                    }
                }).start()
            }
            R.id.skipTv -> {
                countDownTimer?.cancel()
                toMain()
            }
        }
    }

    fun toMain() {
        val it= Intent(this, MainActivity::class.java)
        startActivity(it)
        finish()
    }


    //加载上部数据
    fun loadData() {
        val city=SharedPrefencesUtils.getParam(this, Contants.CITY, Contants.CITYNAME) as String
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, Contants.WEATHER_AIR + city,
                Response.Listener { response ->
                    try {
                        SharedPrefencesUtils.setParam(this, Contants.WEATHER_AQI_JSON, response)
                        getWeather(city)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {

                })
        requestQueue.add(stringRequest)
    }


    private fun getWeather(city:String?){
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, Contants.WEATHER_ALL + city,
                Response.Listener { response ->
                    try {
                        SharedPrefencesUtils.setParam(this, Contants.WEATHER_JSON, response)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                })
        requestQueue.add(stringRequest)

    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var hasPermissionDismiss = false
        when (requestCode) {
            PERMISSION_CODE -> {
                for (result in grantResults){
                    if(result==-1){
                        hasPermissionDismiss=true
                    }
                }
                if (hasPermissionDismiss){
                    CommonUtils.showShortSnackBar(backIv,getString(R.string.splash_nopass_permission_text))
                }
                initData()
            }
            else -> {
                initData()
            }
        }
    }
}
