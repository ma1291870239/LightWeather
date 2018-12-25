package com.ma.lightweather.app;

import android.app.Application;

/**
 * Created by Aeolus on 2018/12/25.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;//存储引用
    }

    public static MyApplication getInstance(){
        return instance;
    }
}
