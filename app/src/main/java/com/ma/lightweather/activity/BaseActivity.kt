package com.ma.lightweather.activity

import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.SharedPrefencesUtils
import com.ma.lightweather.utils.WeatherUtils

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(WeatherUtils.getTheme(this))
        setContentView(R.layout.activity_base)
        setTaskDescriptionBar()
    }

    private fun setTaskDescriptionBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val tDesc = ActivityManager.TaskDescription(getString(R.string.app_name),
                    BitmapFactory.decodeResource(resources, R.drawable.ic_app_launcher),
                    ContextCompat.getColor(this,WeatherUtils.getBackColor(this)))
            setTaskDescription(tDesc)
        }
    }



}
