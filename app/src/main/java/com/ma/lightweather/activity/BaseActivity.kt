package com.ma.lightweather.activity

import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.SharedPrefencesUtils
import com.ma.lightweather.utils.WeatherUtils

open class BaseActivity<VB:ViewBinding> : AppCompatActivity() {

    lateinit var mBinding: VB
    lateinit var TAG:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val oldVersion= SharedPrefencesUtils.getParam(this, Contants.VERSION, false) as Boolean
        if(oldVersion){
            setTheme(WeatherUtils.getTheme(this))
        }
        setContentView(R.layout.activity_base)
        TAG=localClassName
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

    fun setStatusColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0以上版本
            // 设置状态栏底色颜色
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor =ContextCompat.getColor(this,color)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上版本
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //6.0以下设置默认黑色状态栏
            window.statusBarColor = ContextCompat.getColor(this,color)
        }
    }


}
