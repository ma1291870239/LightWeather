package com.ma.lightweather.utils;

import android.content.Context;
import android.widget.Toast;

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
}
