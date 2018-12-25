package com.ma.lightweather.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ma.lightweather.R;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.utils.SharedPrefencesUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Contants.THEMETAG=(int) SharedPrefencesUtils.getParam(this,Contants.THEME,0);
        if(Contants.THEMETAG==0){
            setTheme(R.style.CyanAppTheme);
        }else if(Contants.THEMETAG==1){
            setTheme(R.style.PuroleAppTheme);
        }else if(Contants.THEMETAG==2){
            setTheme(R.style.RedAppTheme);
        }else if(Contants.THEMETAG==3){
            setTheme(R.style.PinkAppTheme);
        }else if(Contants.THEMETAG==4){
            setTheme(R.style.GreenAppTheme);
        }else if(Contants.THEMETAG==5){
            setTheme(R.style.BlueAppTheme);
        }else if(Contants.THEMETAG==6){
            setTheme(R.style.OrangeAppTheme);
        }else if(Contants.THEMETAG==7){
            setTheme(R.style.GreyAppTheme);
        }
        setContentView(R.layout.activity_base);
    }
    


}
