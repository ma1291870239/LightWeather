package com.ma.lightweather.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Ma-PC on 2018/7/7.
 */

object PhotoUtils {

    fun getUriForFile(context: Context, file: File): Uri? {
        var fileUri: Uri? = null
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file)
        } else {
            fileUri = Uri.fromFile(file)
        }
        return fileUri
    }

    fun getUriForFile24(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context,
                context.packageName + ".fileprovider",
                file)
    }


    fun setIntentDataAndType(uri1: Uri,
                             intent: Intent,
                             type: String) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(uri1, type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            intent.setDataAndType(uri1, type)
        }
    }


    fun getFileFromMediaUri(ac: Context, uri: Uri): File? {
        if (uri.scheme!!.toString().compareTo("content") == 0) {
            val cr = ac.contentResolver
            val cursor = cr.query(uri, null, null, null, null)// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst()
                val filePath = cursor.getString(cursor.getColumnIndex("_data"))// 获取图片路径
                cursor.close()
                if (filePath != null) {
                    return File(filePath)
                }
            }
        } else if (uri.scheme!!.toString().compareTo("file") == 0) {
            return File(uri.toString().replace("file://", ""))
        }
        return null
    }

    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }


    fun toTurn(img: Bitmap, degree: Int): Bitmap {
        var img = img
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())//翻转90度
        val width = img.width
        val height = img.height
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true)
        return img
    }

    fun saveImageToGallery(context: Context, bmp: Bitmap): Boolean {
        // 首先保存图片
        val storePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "lightweather"
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            //通过io流的方式来压缩保存图片
            val isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos)
            fos.flush()
            fos.close()

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            val uri = Uri.fromFile(file)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return isSuccess
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }
}
