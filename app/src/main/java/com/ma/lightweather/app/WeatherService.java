package com.ma.lightweather.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Aeolus on 2018/12/26.
 */

public class WeatherService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
