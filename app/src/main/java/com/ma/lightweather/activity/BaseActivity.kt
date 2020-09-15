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

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppTheme()
        setContentView(R.layout.activity_base)
        setTaskDescriptionBar()
    }

    private fun setTaskDescriptionBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val tDesc = ActivityManager.TaskDescription(getString(R.string.app_name),
                    BitmapFactory.decodeResource(resources, R.drawable.ic_app_launcher),
                    ContextCompat.getColor(this,CommonUtils.getBackColor(this)))
            setTaskDescription(tDesc)
        }
    }

    private fun setAppTheme() {
        when (SharedPrefencesUtils.getParam(this, Contants.THEME, 0) as Int) {
            0 -> setTheme(R.style.CyanAppTheme)
            1 -> setTheme(R.style.PuroleAppTheme)
            2 -> setTheme(R.style.RedAppTheme)
            3 -> setTheme(R.style.PinkAppTheme)
            4 -> setTheme(R.style.GreenAppTheme)
            5 -> setTheme(R.style.BlueAppTheme)
            6 -> setTheme(R.style.OrangeAppTheme)
            7 -> setTheme(R.style.GreyAppTheme)
        }
    }


}
