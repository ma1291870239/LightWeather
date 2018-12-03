package com.ma.lightweather.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ma.lightweather.R;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.PhotoUtils;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends BaseActivity implements View.OnClickListener{

    private ImageView backIv,downloadIv;
    private TextView skipTv;
    private static final int DOWNLOAD_CODE=200;
    private CountDownTimer countDownTimer;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD_CODE:
                    CommonUtils.showShortToast(SplashActivity.this,"保存成功");
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
        init();
    }

    private void init() {
        backIv=findViewById(R.id.backIv);
        downloadIv=findViewById(R.id.downloadIv);
        skipTv=findViewById(R.id.skipTv);
        Glide.with(this).load(Contants.BINGURL).into(backIv);
        downloadIv.setOnClickListener(this);
        skipTv.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.downloadIv:
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
}
