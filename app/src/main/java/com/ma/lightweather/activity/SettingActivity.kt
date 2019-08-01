package com.ma.lightweather.activity

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.utils.SharedPrefencesUtils

class SettingActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {

    private var notifySwitch: Switch? = null
    private var statusSwitch: Switch? = null
    private var lifeSwitch: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
    }

    private fun initView() {
        notifySwitch = findViewById(R.id.notifySwitch)
        statusSwitch = findViewById(R.id.statusSwitch)
        lifeSwitch = findViewById(R.id.lifeSwitch)
        notifySwitch?.setOnCheckedChangeListener(this)
        statusSwitch?.setOnCheckedChangeListener(this)
        lifeSwitch?.setOnCheckedChangeListener(this)
        notifySwitch?.isChecked = SharedPrefencesUtils.getParam(this, Contants.NOTIFY, false) as Boolean
        statusSwitch?.isChecked = SharedPrefencesUtils.getParam(this, Contants.STATUS, false) as Boolean
        lifeSwitch?.isChecked = SharedPrefencesUtils.getParam(this, Contants.LIFE, true) as Boolean
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.notifySwitch -> {
                SharedPrefencesUtils.setParam(this@SettingActivity, Contants.NOTIFY, isChecked)
                if (isChecked) {
                    val it = Intent(this@SettingActivity, WeatherService::class.java)
                    startService(it)
                } else {
                    val it  = Intent(this@SettingActivity, WeatherService::class.java)
                    stopService(it)
                }
            }
            R.id.statusSwitch -> {
                SharedPrefencesUtils.setParam(this@SettingActivity, Contants.STATUS, isChecked)
                if (isChecked) {
                    val it = Intent(this@SettingActivity, WeatherService::class.java)
                    startService(it)
                } else {
                    val it  = Intent(this@SettingActivity, WeatherService::class.java)
                    stopService(it)
                }
            }
            R.id.lifeSwitch -> {
                SharedPrefencesUtils.setParam(this@SettingActivity, Contants.LIFE, isChecked)
                if (isChecked) {

                } else {

                }
            }
        }
    }
}
