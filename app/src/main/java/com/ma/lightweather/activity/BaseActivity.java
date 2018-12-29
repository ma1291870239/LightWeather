package com.ma.lightweather.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ma.lightweather.R;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_base);
        setTaskDescriptionBar();
    }

    private void setTaskDescriptionBar(){
        if (Build.VERSION.SDK_INT>=21){
            ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.weather),
                    getResources().getColor(CommonUtils.getBackColor(this)));
            setTaskDescription(tDesc);
        }
    }

    private void setAppTheme() {
        int themeTag=(int) SharedPrefencesUtils.getParam(this,Contants.THEME,0);
        switch (themeTag){
            case 0:
                setTheme(R.style.CyanAppTheme);
                break;
            case 1:
                setTheme(R.style.PuroleAppTheme);
                break;
            case 2:
                setTheme(R.style.RedAppTheme);
                break;
            case 3:
                setTheme(R.style.PinkAppTheme);
                break;
            case 4:
                setTheme(R.style.GreenAppTheme);
                break;
            case 5:
                setTheme(R.style.BlueAppTheme);
                break;
            case 6:
                setTheme(R.style.OrangeAppTheme);
                break;
            case 7:
                setTheme(R.style.GreyAppTheme);
                break;
        }
    }


}
