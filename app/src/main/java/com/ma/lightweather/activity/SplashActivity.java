package com.ma.lightweather.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ma.lightweather.R;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.PhotoUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends BaseActivity implements View.OnClickListener{

    private ImageView backIv,downloadIv;
    private TextView skipTv;
    private static final int DOWNLOAD_CODE=200;
    private static final int IMGLOAD_CODE=100;
    private static final int PERMISSION_CODE_WRITE = 1;
    private CountDownTimer countDownTimer;
    private String url;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD_CODE:
                    CommonUtils.showShortToast(SplashActivity.this,"保存成功");
                    break;
                case IMGLOAD_CODE:

                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        url=(String) SharedPrefencesUtils.getParam(this,Contants.BINGIMG,"http://area.sinaapp.com/bingImg/");
        initView();
        checkPermission();
    }

    private void initView() {
        backIv=findViewById(R.id.backIv);
        downloadIv=findViewById(R.id.downloadIv);
        skipTv=findViewById(R.id.skipTv);
        downloadIv.setOnClickListener(this);
        skipTv.setOnClickListener(this);
    }

    private void initData() {
        Glide.with(SplashActivity.this).load(url).into(backIv);
        countDownTimer=new CountDownTimer(4000,1000) {
            @Override
            public void onTick(long l) {
                skipTv.setText("跳过"+l/1000+"秒");
            }

            @Override
            public void onFinish() {
                toMain();
            }
        };
        countDownTimer.start();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.BINGURL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPrefencesUtils.setParam(SplashActivity.this,Contants.BINGIMG,response);
                        url=response;
                        handler.sendEmptyMessage(IMGLOAD_CODE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_CODE_WRITE);
        }else{
            initData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.downloadIv:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    CommonUtils.showShortToast(this,"当前没有读写权限");
                    return ;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap= null;
                        try {
                            bitmap = Glide.with(SplashActivity.this)
                                    .load(Contants.BINGURL)
                                    .asBitmap()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        boolean isSave= PhotoUtils.saveImageToGallery(SplashActivity.this,bitmap);
                        if(isSave){
                            handler.sendEmptyMessage(DOWNLOAD_CODE);
                        }
                    }
                }).start();
                break;
            case R.id.skipTv:
                countDownTimer.cancel();
                toMain();
                break;
        }
    }

    public void toMain(){
        Intent it=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE_WRITE:
                default:
                initData();
                return;
        }
    }
}
