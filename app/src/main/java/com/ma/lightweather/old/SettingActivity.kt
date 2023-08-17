package com.ma.lightweather.old

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.activity.BaseActivity
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.databinding.ActivitySettingBinding
import com.ma.lightweather.utils.SPUtils
import com.ma.lightweather.utils.WeatherUtils

class SettingActivity : BaseActivity<ActivitySettingBinding>(), CompoundButton.OnCheckedChangeListener {

    private var notifySwitch: Switch? = null
    private var statusSwitch: Switch? = null
    private var lifeSwitch: Switch? = null
    private var versionSwitch: Switch? = null
    private var toolBar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
        val oldVersion= SPUtils.getParam(this, Contants.OLDVERSION, false) as Boolean
        if(oldVersion){

        }else{
            val cond=SPUtils.getParam(this, Contants.TXT, "") as String
            var color= WeatherUtils.getFrogWeatherTheme(cond)
            toolBar?.setBackgroundColor(ContextCompat.getColor(this,color))
            setStatusColor(color)
        }
    }

    private fun initView() {
        notifySwitch = findViewById(R.id.notifySwitch)
        statusSwitch = findViewById(R.id.statusSwitch)
        lifeSwitch = findViewById(R.id.lifeSwitch)
        versionSwitch = findViewById(R.id.versionSwitch)
        toolBar = findViewById(R.id.toolBar)
        notifySwitch?.setOnCheckedChangeListener(this)
        statusSwitch?.setOnCheckedChangeListener(this)
        lifeSwitch?.setOnCheckedChangeListener(this)
        versionSwitch?.setOnCheckedChangeListener(this)
        notifySwitch?.isChecked = SPUtils.getParam(this, Contants.NOTIFY, false) as Boolean
        statusSwitch?.isChecked = SPUtils.getParam(this, Contants.STATUS, false) as Boolean
        lifeSwitch?.isChecked = SPUtils.getParam(this, Contants.LIFE, false) as Boolean
        versionSwitch?.isChecked = SPUtils.getParam(this, Contants.OLDVERSION, false) as Boolean
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.notifySwitch -> {
                SPUtils.setParam(this, Contants.NOTIFY, isChecked)
                if (isChecked) {
                    val it = Intent(this, WeatherService::class.java)
                    startService(it)
                } else {
                    val it  = Intent(this, WeatherService::class.java)
                    stopService(it)
                }
            }
            R.id.statusSwitch -> {
                SPUtils.setParam(this, Contants.STATUS, isChecked)
                if (isChecked) {
                    val it = Intent(this, WeatherService::class.java)
                    startService(it)
                } else {
                    val it  = Intent(this, WeatherService::class.java)
                    stopService(it)
                }
            }
            R.id.lifeSwitch -> {
                SPUtils.setParam(this, Contants.LIFE, isChecked)
                if (isChecked) {

                } else {

                }
            }
            R.id.versionSwitch -> {
                SPUtils.setParam(this, Contants.OLDVERSION, isChecked)
                if (isChecked) {

                } else {

                }
            }
        }
    }
}
