package com.ma.lightweather.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ma.lightweather.R;
import com.ma.lightweather.model.Contants;
import com.ma.lightweather.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
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
                        saveImageToGallery(SplashActivity.this,bitmap);
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

    public boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "lightweather";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                handler.sendEmptyMessage(DOWNLOAD_CODE);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
