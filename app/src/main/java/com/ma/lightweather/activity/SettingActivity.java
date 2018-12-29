package com.ma.lightweather.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ma.lightweather.R;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.app.WeatherService;
import com.ma.lightweather.utils.SharedPrefencesUtils;

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    
    private Switch notifySwitch;

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
        }
    }
}
