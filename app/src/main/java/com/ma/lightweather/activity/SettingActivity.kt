package com.ma.lightweather.activity

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.utils.SharedPrefencesUtils
import com.ma.lightweather.utils.WeatherUtils

class SettingActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {

    private var notifySwitch: Switch? = null
    private var statusSwitch: Switch? = null
    private var lifeSwitch: Switch? = null
    private var versionSwitch: Switch? = null
    private var toolBar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
        val oldVersion= SharedPrefencesUtils.getParam(this, Contants.VERSION, false) as Boolean
        if(oldVersion){

        }else{
            val cond=SharedPrefencesUtils.getParam(this, Contants.TXT, "") as String
            var color= WeatherUtils.getColorWeatherTheme(cond)
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
        notifySwitch?.isChecked = SharedPrefencesUtils.getParam(this, Contants.NOTIFY, false) as Boolean
        statusSwitch?.isChecked = SharedPrefencesUtils.getParam(this, Contants.STATUS, false) as Boolean
        lifeSwitch?.isChecked = SharedPrefencesUtils.getParam(this, Contants.LIFE, false) as Boolean
        versionSwitch?.isChecked = SharedPrefencesUtils.getParam(this, Contants.VERSION, false) as Boolean
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.notifySwitch -> {
                SharedPrefencesUtils.setParam(this, Contants.NOTIFY, isChecked)
                if (isChecked) {
                    val it = Intent(this, WeatherService::class.java)
                    startService(it)
                } else {
                    val it  = Intent(this, WeatherService::class.java)
                    stopService(it)
                }
            }
            R.id.statusSwitch -> {
                SharedPrefencesUtils.setParam(this, Contants.STATUS, isChecked)
                if (isChecked) {
                    val it = Intent(this, WeatherService::class.java)
                    startService(it)
                } else {
                    val it  = Intent(this, WeatherService::class.java)
                    stopService(it)
                }
            }
            R.id.lifeSwitch -> {
                SharedPrefencesUtils.setParam(this, Contants.LIFE, isChecked)
                if (isChecked) {

                } else {

                }
            }
            R.id.versionSwitch -> {
                SharedPrefencesUtils.setParam(this, Contants.VERSION, isChecked)
                if (isChecked) {

                } else {

                }
            }
        }
    }
}
