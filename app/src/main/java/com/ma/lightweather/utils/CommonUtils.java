package com.ma.lightweather.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import com.ma.lightweather.R;
import com.ma.lightweather.app.Contants;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aeolus on 2018/6/5.
 */

public class CommonUtils {
    public static Toast mToast;

    public static void showLongToast(Context context, String content){
        if(mToast==null){
            mToast=Toast.makeText(context,content,Toast.LENGTH_LONG);
        }else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
    public static void showShortToast(Context context,String content){
        if(mToast==null){
            mToast=Toast.makeText(context,content,Toast.LENGTH_SHORT);
        }else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    /**
     * 绘制文字到右下角
     * @param context
     * @param bitmap
     * @return
     */
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String phoneText,String loctionText,String weatherText) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int offset=h/50;
        int phoneSpace=0;
        int loctionSpace=0;
        int paddingLeft=w -offset;
        int paddingTop=h - offset;
        float textSize=h/10;

        //间隔大小
        if(!phoneText.isEmpty()){
            //绘制机型
            Paint phonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            phonePaint.setColor(context.getResources().getColor(R.color.white));
            phonePaint.setTextSize(textSize/4);
            Rect phoneBounds = new Rect();
            phonePaint.getTextBounds(phoneText, 0, phoneText.length(), phoneBounds);
            phoneSpace=phoneBounds.height()+offset;
            bitmap=drawTextToBitmap(context, bitmap,phoneText, phonePaint, phoneBounds,
                    paddingLeft - phoneBounds.width(),
                    paddingTop);
        }

        if(!loctionText.isEmpty()){
            //绘制地点
            Paint loctionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            loctionPaint.setColor(context.getResources().getColor(R.color.white));
            loctionPaint.setTextSize(textSize/4);
            Rect loctionBounds = new Rect();
            loctionPaint.getTextBounds(loctionText, 0, loctionText.length(), loctionBounds);
            loctionSpace=loctionBounds.height()+offset;
            bitmap=drawTextToBitmap(context, bitmap,loctionText, loctionPaint, loctionBounds,
                    paddingLeft - loctionBounds.width() ,
                    paddingTop-phoneSpace);
        }

        if(!weatherText.isEmpty()){
            //绘制天气
            Paint weatherPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            weatherPaint.setColor(context.getResources().getColor(R.color.white));
            weatherPaint.setTextSize(textSize/3);
            Rect weatherBounds = new Rect();
            weatherPaint.getTextBounds(weatherText, 0, weatherText.length(), weatherBounds);

            bitmap=drawTextToBitmap(context, bitmap,weatherText, weatherPaint, weatherBounds,
                    paddingLeft- weatherBounds.width(),
                    paddingTop-phoneSpace-loctionSpace);
        }

        return bitmap;
    }


    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        try {
            paint.setDither(true); // 获取跟清晰的图像采样
            paint.setFilterBitmap(true);// 过滤一些
            Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawBitmap(bitmap,0,0,null);
            canvas.drawText(text, paddingLeft, paddingTop, paint);
            bitmap.recycle();
            return bmp;
        }catch (OutOfMemoryError error){
            CommonUtils.showShortToast(context,"图片过大");
        }
        return null;
    }


    public static boolean isContains(Context context,String packageName){
        final PackageManager packageManager=context.getPackageManager();
        List<PackageInfo> packageInfoList=packageManager.getInstalledPackages(0);
        List<String> nameList=new ArrayList<>();
        if(packageInfoList!=null){
            for (int i=0;i<packageInfoList.size();i++){
                String name=packageInfoList.get(i).packageName;
                if(name.equals(packageName)){
                    return true;
                }
            }
        }
        return false;
    }

    public static int getBackColor(){
        if(Contants.THEMETAG==0){
           return  R.color.cyanColorAccent;
        }else if(Contants.THEMETAG==1){
            return  R.color.purpleColorAccent;
        }else if(Contants.THEMETAG==2){
            return  R.color.redColorAccent;
        }else if(Contants.THEMETAG==3){
            return  R.color.pinkColorAccent;
        }else if(Contants.THEMETAG==4){
            return  R.color.greenColorAccent;
        }else if(Contants.THEMETAG==5){
            return  R.color.blueColorAccent;
        }else if(Contants.THEMETAG==6){
            return  R.color.orangeColorAccent;
        }else if(Contants.THEMETAG==7){
            return  R.color.greyColorAccent;
        }
        return  R.color.text;
    }


    public static int getTextColor(){
        if(Contants.THEMETAG==0){
            return  R.color.cyanColorAccent;
        }else if(Contants.THEMETAG==1){
            return  R.color.purpleColorAccent;
        }else if(Contants.THEMETAG==2){
            return  R.color.redColorAccent;
        }else if(Contants.THEMETAG==3){
            return  R.color.pinkColorAccent;
        }else if(Contants.THEMETAG==4){
            return  R.color.greenColorAccent;
        }else if(Contants.THEMETAG==5){
            return  R.color.blueColorAccent;
        }else if(Contants.THEMETAG==6){
            return  R.color.orangeColorAccent;
        }else if(Contants.THEMETAG==7){
            return  R.color.greyColorAccent;
        }
        return  R.color.text;
    }

    /**
     * dp转换成px
     */
    public static int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(Context context,float pxValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
    /**
     * sp转换成px
     */
    public static int sp2px(Context context,float spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }
    /**
     * px转换成sp
     */
    public static int px2sp(Context context,float pxValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue/fontScale+0.5f);
    }

    public static Bitmap compressBitmap(Bitmap bitmap){
        int ratio=3;
        if(bitmap.getHeight()>=1000||bitmap.getWidth()>=1000) {
            Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
            return result;
        }
        return bitmap;
    }

}
