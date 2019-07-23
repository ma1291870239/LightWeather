package com.ma.lightweather.fragment


import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ma.lightweather.R
import com.ma.lightweather.activity.SettingActivity
import com.ma.lightweather.adapter.SelectColorAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.PhotoUtils
import com.ma.lightweather.utils.SharedPrefencesUtils
import com.ma.lightweather.widget.ActionSheetDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * Created by Ma-PC on 2016/12/5.
 */
class PhotoFragment : BaseFragment(), View.OnClickListener {

    private var phoneLayout: LinearLayout? = null
    private var loctionLayout: LinearLayout? = null
    private var weatherLayout: LinearLayout? = null
    private var photoLayout: LinearLayout? = null
    private var scoreLayout: LinearLayout? = null
    private var themeLayout: LinearLayout? = null
    private var settingLayout: LinearLayout? = null
    private var shareLayout: LinearLayout? = null
    private var defaultPhoneTv: TextView? = null
    private var defaultLoctionTv: TextView? = null
    private var defaultWeatherTv: TextView? = null
    private var phoneTv: TextView? = null
    private var loctionTv: TextView? = null
    private var weatherTv: TextView? = null
    private var imgView: ImageView? = null

    private var strImgPath: String? = null
    private var filename: String? = null
    private var imgUrl: Uri? = null
    private var out: File? = null
    private var bitmap: Bitmap? = null
    private var bytes: ByteArray? = null
    private var progressDialog: ProgressDialog? = null

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                SAVE_CODE -> {
                    val storePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "lightweather"
                    CommonUtils.showShortToast(context, "水印照片已成功保存至\n$storePath")
                }
                TOBYTE_CODE -> showImgDialog()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.frag_photo, null)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        phoneLayout = view!!.findViewById(R.id.phoneLayout)
        loctionLayout = view!!.findViewById(R.id.loctionLayout)
        weatherLayout = view!!.findViewById(R.id.weatherLayout)
        photoLayout = view!!.findViewById(R.id.photoLayout)
        scoreLayout = view!!.findViewById(R.id.scoreLayout)
        themeLayout = view!!.findViewById(R.id.themeLayout)
        settingLayout = view!!.findViewById(R.id.settingLayout)
        shareLayout = view!!.findViewById(R.id.shareLayout)
        phoneTv = view!!.findViewById(R.id.phoneTv)
        loctionTv = view!!.findViewById(R.id.loctionTv)
        weatherTv = view!!.findViewById(R.id.weatherTv)
        imgView = view!!.findViewById(R.id.imgView)
        defaultPhoneTv = view!!.findViewById(R.id.defaultPhoneTv)
        defaultLoctionTv = view!!.findViewById(R.id.defaultLoctionTv)
        defaultWeatherTv = view!!.findViewById(R.id.defaultWeatherTv)
        phoneTv!!.setOnClickListener(this)
        loctionTv!!.setOnClickListener(this)
        weatherTv!!.setOnClickListener(this)
        defaultPhoneTv!!.setOnClickListener(this)
        defaultLoctionTv!!.setOnClickListener(this)
        defaultWeatherTv!!.setOnClickListener(this)
        photoLayout!!.setOnClickListener(this)
        scoreLayout!!.setOnClickListener(this)
        themeLayout!!.setOnClickListener(this)
        settingLayout!!.setOnClickListener(this)
        shareLayout!!.setOnClickListener(this)


        phoneTv!!.text = SharedPrefencesUtils.getParam(context, Contants.MODEL, "点击左侧设置当前机型") as String?
        loctionTv!!.text = SharedPrefencesUtils.getParam(context, Contants.LOCTION, "点击左侧设置当前城市") as String?
        weatherTv!!.text = SharedPrefencesUtils.getParam(context, Contants.WEATHER, "点击左侧设置当前天气") as String?
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.phoneTv -> showDialog(1)
            R.id.loctionTv -> showDialog(2)
            R.id.weatherTv -> showDialog(3)
            R.id.defaultPhoneTv -> {
                phoneTv!!.text = android.os.Build.BRAND + " " + android.os.Build.MODEL
                SharedPrefencesUtils.setParam(context, Contants.MODEL, phoneTv!!.text.toString())
            }
            R.id.defaultLoctionTv -> {
                loctionTv!!.text = SharedPrefencesUtils.getParam(context, Contants.CITY, "") as String?
                SharedPrefencesUtils.setParam(context, Contants.LOCTION, loctionTv!!.text.toString())
            }
            R.id.defaultWeatherTv -> {
                weatherTv!!.text = (SharedPrefencesUtils.getParam(context, Contants.TMP, "").toString() + "℃ "
                        + SharedPrefencesUtils.getParam(context, Contants.TXT, ""))
                SharedPrefencesUtils.setParam(context, Contants.WEATHER, weatherTv!!.text.toString())
            }
            R.id.photoLayout -> {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    CommonUtils.showShortToast(context, "当前没有读写权限")
                    return
                }
                showActionSheet()
            }
            R.id.scoreLayout -> goToMarket()
            R.id.themeLayout -> selectColor()
            R.id.settingLayout -> {
                val it = Intent(context, SettingActivity::class.java)
                startActivity(it)
            }
            R.id.shareLayout -> {
            }
        }

    }

    private fun selectColor() {
        val txtList:MutableList<String> = ArrayList()
        txtList.add("青")
        txtList.add("紫")
        txtList.add("红")
        txtList.add("粉")
        txtList.add("绿")
        txtList.add("蓝")
        txtList.add("橙")
        txtList.add("灰")

        val colorList:MutableList<Int> = ArrayList()
        colorList.add(R.color.cyanColorAccent)
        colorList.add(R.color.purpleColorAccent)
        colorList.add(R.color.redColorAccent)
        colorList.add(R.color.pinkColorAccent)
        colorList.add(R.color.greenColorAccent)
        colorList.add(R.color.blueColorAccent)
        colorList.add(R.color.orangeColorAccent)
        colorList.add(R.color.greyColorAccent)

        val adapter = SelectColorAdapter(activity, txtList, colorList)
        val dialog = AlertDialog.Builder(activity)
                .setSingleChoiceItems(adapter, 0) { dialog, which ->
                    dialog.dismiss()
                    SharedPrefencesUtils.setParam(context, Contants.THEME, which)
                    activity.recreate()
                }.create()
        dialog.show()
        val window = dialog.window
        val lp = window!!.attributes
        lp.gravity = Gravity.CENTER
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.width = CommonUtils.dp2px(context, 200f)
        //dialog.getWindow().setAttributes(lp);

    }

    private fun goToMarket() {
        val uri = Uri.parse("market://details?id=" + context.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            if (CommonUtils.isContains(context, "com.tencent.android.qqdownloader")) {
                goToMarket.setPackage("com.tencent.android.qqdownloader")
            }
            if (CommonUtils.isContains(context, "com.coolapk.market")) {
                goToMarket.setPackage("com.coolapk.market")
            }
            context.startActivity(goToMarket)
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.ma.lightweather")
            startActivity(intent)
        }

    }


    private fun showActionSheet() {
        ActionSheetDialog(activity).builder().setCancelable(true).setCanceledOnTouchOutside(true)
                .setTitle("选择照片")
                .addSheetItem("拍照", null, object : ActionSheetDialog.OnSheetItemClickListener {
                    override fun onClick(which: Int) {
                        cameraMethod()
                    }
                }).addSheetItem("相册", null, object : ActionSheetDialog.OnSheetItemClickListener {
                    override fun onClick(which: Int) {
                        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                        startActivityForResult(intent, RESULT_PICTURE)
                    }
                }).setCanceledOnTouchOutside(true).show()
    }

    private fun showDialog(tag: Int) {
        val titleView = LayoutInflater.from(context).inflate(R.layout.item_title_dialog, null)
        val textView = titleView.findViewById<TextView>(R.id.dialogTitle)
        textView.text = "请输入"
        textView.setTextColor(context.resources.getColor(CommonUtils.getTextColor(context)))

        val contentView = LayoutInflater.from(context).inflate(R.layout.item_edit_dialog, null)
        val editText = contentView.findViewById<EditText>(R.id.editText)
        val gradientDrawable = editText.background as GradientDrawable
        gradientDrawable.setStroke(CommonUtils.dp2px(context, 1f), context.resources.getColor(CommonUtils.getBackColor(context)))

        AlertDialog.Builder(activity)
                .setCustomTitle(titleView)
                .setView(contentView)
                .setPositiveButton("确定") { dialogInterface, i ->
                    val s = editText.text.toString()
                    if (tag == 1) {
                        phoneTv!!.text = s
                        SharedPrefencesUtils.setParam(context, Contants.MODEL, phoneTv!!.text.toString())
                    }
                    if (tag == 2) {
                        loctionTv!!.text = s
                        SharedPrefencesUtils.setParam(context, Contants.LOCTION, loctionTv!!.text.toString())
                    }
                    if (tag == 3) {
                        weatherTv!!.text = s
                        SharedPrefencesUtils.setParam(context, Contants.WEATHER, weatherTv!!.text.toString())
                    }
                }
                .setNegativeButton("取消", null).show()
    }


    private fun showImgDialog() {

        val titleView = LayoutInflater.from(context).inflate(R.layout.item_title_dialog, null)
        val textView = titleView.findViewById<TextView>(R.id.dialogTitle)
        textView.text = "是否保存水印照片"
        textView.setTextColor(context.resources.getColor(CommonUtils.getTextColor(context)))

        val contentView = LayoutInflater.from(context).inflate(R.layout.item_img_dialog, null)
        val imageView = contentView.findViewById<ImageView>(R.id.imgView)
        Glide.with(context).load(bytes).into(imageView)

        if (progressDialog != null) {
            progressDialog!!.cancel()
            progressDialog!!.dismiss()
        }
        AlertDialog.Builder(activity).setTitle("是否保存")
                .setCustomTitle(titleView)
                .setView(contentView)
                .setPositiveButton("确定") { dialogInterface, i ->
                    Thread(Runnable {
                        val isSave = PhotoUtils.saveImageToGallery(context, bitmap!!)
                        bitmap!!.recycle()
                        if (isSave) {
                            handler.sendEmptyMessage(SAVE_CODE)
                        }
                    }).start()
                }
                .setNegativeButton("取消", null).show()

    }

    private fun cameraMethod() {
        val imageCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        strImgPath = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/"
        filename = Calendar.getInstance().timeInMillis.toString() + ".png"
        out = File(strImgPath!!)
        if (!out!!.exists()) {
            out!!.mkdirs()
        }
        out = File(strImgPath, filename!!)
        strImgPath = strImgPath!! + filename!!
        imgUrl = PhotoUtils.getUriForFile(context, out!!)
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl)
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        startActivityForResult(imageCaptureIntent, RESULT_PHOTO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val contentResolver = context.contentResolver
        bitmap = null
        when (requestCode) {
            RESULT_PHOTO//拍照
            -> try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUrl)
                val degree = PhotoUtils.readPictureDegree(strImgPath!!)
                bitmap = PhotoUtils.toTurn(bitmap!!, degree)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            RESULT_PICTURE//相册
            -> try {
                if (data != null) {
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.data!!))
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }
        if (bitmap != null) {
            bitmap = CommonUtils.drawTextToRightBottom(context, bitmap!!, phoneTv!!.text.toString(), loctionTv!!.text.toString(), weatherTv!!.text.toString())
            progressDialog = ProgressDialog.show(activity, null, "正在生成水印照片")
            Thread(Runnable {
                val matrix = Matrix()
                matrix.setScale(0.5f, 0.5f)
                val small = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap!!.height, matrix, true)
                val baos = ByteArrayOutputStream()
                small.compress(Bitmap.CompressFormat.PNG, 100, baos)
                bytes = baos.toByteArray()
                small.recycle()
                handler.sendEmptyMessage(TOBYTE_CODE)
            }).start()
        }


    }

    companion object {

        private val RESULT_PHOTO = 1
        private val RESULT_PICTURE = 2
        private val SAVE_CODE = 200
        private val TOBYTE_CODE = 300
    }

}
