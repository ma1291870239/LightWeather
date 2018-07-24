package com.ma.lightweather.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by Ma-PC on 2018/7/7.
 */

public class TakePhotoUtils {

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static Uri getUriForFile24(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider",
                file);
        return fileUri;
    }


    public static void setIntentDataAndType(Uri uri1,
                                            Intent intent,
                                            String type) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(uri1, type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.setDataAndType(uri1, type);
        }
    }
}
