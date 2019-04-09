package com.ma.lightweather.app;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.utils.Parse;
import com.ma.lightweather.utils.SharedPrefencesUtils;

import org.json.JSONException;

import java.util.List;

public class WeatherService extends Service {

    private List<Weather> weatherList;
    private static final int WEATHER_CODE=200;
    private String channelId ;
    private String channelName;
    private RemoteViews remoteViews;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WEATHER_CODE:
                    creatNotify();
                    break;
            }
        }
    };

    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        creatChannel();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        wakeLock.release();
        stopForeground(true);
        super.onDestroy();
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initData();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
        wakeLock.acquire();
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int spaceTime=8*60*60*1000;
        long updateTime= SystemClock.elapsedRealtime()+spaceTime;
        Intent it=new Intent(this,WeatherService.class);
        PendingIntent pi=PendingIntent.getService(this,0,it,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,updateTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initData() {
        String city= (String) SharedPrefencesUtils.getParam(getApplicationContext(),Contants.CITY,Contants.CITYNAME);
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.WEATHER_ALL + city,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            weatherList= Parse.parseWeather(response,null,null,getApplicationContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(weatherList.size()>0) {
                            handler.sendEmptyMessage(WEATHER_CODE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void creatNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(channelId);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
            }
        }
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.item_notify_simple);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,channelId)
                .setContent(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon( R.mipmap.weather);
        if((boolean) SharedPrefencesUtils.getParam(this,Contants.STATUS,false)) {
            notification.setPriority(NotificationCompat.PRIORITY_MIN);
        }
        setWeatherMsg(notification);
        startForeground(111, notification.build());
    }

    private void setWeatherMsg(NotificationCompat.Builder notification) {
        for(int i=0;i<weatherList.size();i++) {
            if(weatherList.get(i).txt.contains("晴")) {
                notification.setSmallIcon(R.mipmap.sunny);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.sunny));
            }if(weatherList.get(i).txt.contains("云")) {
                notification.setSmallIcon(R.mipmap.cloudy);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.cloudy));
            }if(weatherList.get(i).txt.contains("阴")) {
                notification.setSmallIcon(R.mipmap.shade);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.shade));
            }if(weatherList.get(i).txt.contains("风")) {
                notification.setSmallIcon(R.mipmap.gale);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.gale));
            }if(weatherList.get(i).txt.contains("雨")) {
                notification.setSmallIcon(R.mipmap.rain);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.rain));
            }if(weatherList.get(i).txt.contains("雪")) {
                notification.setSmallIcon(R.mipmap.snow);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.snow));
            }if(weatherList.get(i).txt.contains("雾")) {
                notification.setSmallIcon(R.mipmap.smog);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.smog));
            }if(weatherList.get(i).txt.contains("霾")) {
                notification.setSmallIcon(R.mipmap.smog);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.smog));
            }if(weatherList.get(i).txt.contains("沙")) {
                notification.setSmallIcon(R.mipmap.sand);
                remoteViews.setImageViewBitmap(R.id.weatherImg,BitmapFactory.decodeResource(getResources(), R.mipmap.sand));
            }

            remoteViews.setTextViewText(R.id.weatherCity,weatherList.get(i).city);
            remoteViews.setTextViewText(R.id.weatherWind,weatherList.get(i).txt+"　"+weatherList.get(i).dir);
            remoteViews.setTextViewText(R.id.weatherTmp,weatherList.get(i).tmp+"℃");
        }

    }
    private void creatChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "service";
            channelName = "通知栏天气";
            int importance=NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendMsg(String msg) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(channelId);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("收到新消息")
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify((int) System.currentTimeMillis(), notification);
    }
}
