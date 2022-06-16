package com.ma.lightweather.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ma-PC on 2018/7/7.
 */

object PhotoUtils {


    fun saveSplashImage(context: Context, bmp: Bitmap): String {
        val sdf = SimpleDateFormat("_yyyyMMdd_hhmmss")
        val currentDate = sdf.format(Date())
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), "Bing${currentDate}.jpg")
        val fos =FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file.path
    }
}
