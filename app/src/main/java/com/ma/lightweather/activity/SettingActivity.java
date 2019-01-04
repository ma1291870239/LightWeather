package com.ma.lightweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ma.lightweather.R;
import com.ma.lightweather.adapter.SelectColorAdapter;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.app.WeatherService;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    
    private Switch notifySwitch;
    private Switch statusSwitch;
    private Switch lifeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        notifySwitch=findViewById(R.id.notifySwitch);
        notifySwitch.setOnCheckedChangeListener(this);
        notifySwitch.setChecked((boolean) SharedPrefencesUtils.getParam(this,Contants.NOTIFY,false));
        statusSwitch=findViewById(R.id.statusSwitch);
        statusSwitch.setOnCheckedChangeListener(this);
        statusSwitch.setChecked((boolean) SharedPrefencesUtils.getParam(this,Contants.STATUS,false));
        lifeSwitch=findViewById(R.id.lifeSwitch);
        lifeSwitch.setOnCheckedChangeListener(this);
        lifeSwitch.setChecked((boolean) SharedPrefencesUtils.getParam(this,Contants.LIFE,true));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.notifySwitch:
                SharedPrefencesUtils.setParam(SettingActivity.this, Contants.NOTIFY,isChecked);
                if(isChecked){
                    Intent it=new Intent(SettingActivity.this, WeatherService.class);
                    startService(it);
                }else {
                    Intent it=new Intent(SettingActivity.this, WeatherService.class);
                    stopService(it);
                }
                break;
            case R.id.statusSwitch:
                SharedPrefencesUtils.setParam(SettingActivity.this, Contants.STATUS,isChecked);

                break;
            case R.id.lifeSwitch:
                SharedPrefencesUtils.setParam(SettingActivity.this, Contants.LIFE,isChecked);

                break;
        }
    }
}
