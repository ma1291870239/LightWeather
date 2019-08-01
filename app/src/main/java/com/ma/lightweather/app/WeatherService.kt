package com.ma.lightweather.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ma.lightweather.R
import com.ma.lightweather.model.Air
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.SharedPrefencesUtils
import org.json.JSONException

class WeatherService : Service() {

    private var weatherList: List<Weather>? = null
    private var airList: List<Air>? = null
    private var channelId: String? = null
    private var channelName: String? = null
    private var remoteViews: RemoteViews? = null
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                WEATHER_CODE -> creatNotify()
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        creatChannel()
        super.onCreate()
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        initData()
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val spaceTime = 8 * 60 * 60 * 1000
        val updateTime = SystemClock.elapsedRealtime() + spaceTime
        val it = Intent(this, WeatherService::class.java)
        val pi = PendingIntent.getService(this, 0, it, 0)
        manager.cancel(pi)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, updateTime, pi)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initData() {
        val city = SharedPrefencesUtils.getParam(applicationContext, Contants.CITY, Contants.CITYNAME) as String
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val airStringRequest = StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_AIR + city,
                Response.Listener { response ->
                    try {
                        airList = Parse.parseAir(response, null, null, applicationContext)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    if (airList!!.isNotEmpty()) {
                        getWeather(city)
                    }
                },
                Response.ErrorListener { })
        requestQueue.add(airStringRequest)
    }

    private fun getWeather(city:String){
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city,
                Response.Listener { response ->
                    try {
                        weatherList = Parse.parseWeather(response, null, null, applicationContext)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    if (weatherList!!.isNotEmpty()) {
                        handler.sendEmptyMessage(WEATHER_CODE)
                    }
                },
                Response.ErrorListener { })
        requestQueue.add(stringRequest)

    }

    private fun creatNotify() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = manager.getNotificationChannel(channelId)
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                startActivity(intent)
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show()
            }
        }
        remoteViews = RemoteViews(this.packageName, R.layout.item_notify_simple)
        val notification = NotificationCompat.Builder(this, channelId!!)
                .setContent(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.weather)

        setWeatherMsg(notification)
        startForeground(111, notification.build())
    }

    private fun setWeatherMsg(notification: NotificationCompat.Builder) {
        for (i in weatherList!!.indices) {
            val condTxt=weatherList!![i].now.cond_txt
            if (condTxt.contains("晴")) {
                notification.setSmallIcon(R.mipmap.sunny)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.sunny))
            }
            if (condTxt.contains("云")) {
                notification.setSmallIcon(R.mipmap.cloudy)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.cloudy))
            }
            if (condTxt.contains("阴")) {
                notification.setSmallIcon(R.mipmap.shade)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.shade))
            }
            if (condTxt.contains("雨")) {
                notification.setSmallIcon(R.mipmap.rain)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.rain))
            }
            if (condTxt.contains("雪")) {
                notification.setSmallIcon(R.mipmap.snow)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.snow))
            }
            if (condTxt.contains("雾")) {
                notification.setSmallIcon(R.mipmap.smog)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.smog))
            }
            if (condTxt.contains("霾")) {
                notification.setSmallIcon(R.mipmap.smog)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.smog))
            }
            if (condTxt.contains("沙")) {
                notification.setSmallIcon(R.mipmap.sand)
                remoteViews?.setImageViewBitmap(R.id.weatherImg, BitmapFactory.decodeResource(resources, R.mipmap.sand))
            }

            remoteViews?.setTextViewText(R.id.weatherCity, weatherList!![i].basic.location)
            remoteViews?.setTextViewText(R.id.weatherTime, weatherList!![i].update.loc)
            remoteViews?.setTextViewText(R.id.weatherWind, weatherList!![i].now.cond_txt)
            remoteViews?.setTextViewText(R.id.weatherDir, weatherList!![i].now.wind_dir)
            remoteViews?.setTextViewText(R.id.weatherSc, weatherList!![i].now.wind_sc+"级")
            remoteViews?.setTextViewText(R.id.weatherTmp, weatherList!![i].now.tmp + "℃")
            remoteViews?.setTextViewText(R.id.weatherQlit, airList!![i].air_now_city.qlty)
        }
    }

    private fun creatChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "Service"
            channelName = "前台服务"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object {
        private const val WEATHER_CODE = 200
        private const val AIR_CODE = 300
    }
}
