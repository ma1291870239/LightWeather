package com.ma.lightweather.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.activity.SettingActivity;
import com.ma.lightweather.adapter.SelectColorAdapter;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.PhotoUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;
import com.ma.lightweather.widget.ActionSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/5.
 */
public class PhotoFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private LinearLayout phoneLayout,loctionLayout,weatherLayout,photoLayout,scoreLayout,themeLayout,settingLayout,shareLayout;
    private TextView defaultPhoneTv,defaultLoctionTv,defaultWeatherTv,phoneTv,loctionTv,weatherTv;
    private ImageView imgView;

    private String strImgPath, filename;
    private Uri imgUrl;
    private File out;
    private Bitmap bitmap;
    private byte[] bytes;
    private ProgressDialog progressDialog;

    private static final int RESULT_PHOTO=1;
    private static final int RESULT_PICTURE=2;
    private static final int SAVE_CODE=200;
    private static final int TOBYTE_CODE=300;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAVE_CODE:
                    String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "lightweather";
                    CommonUtils.showShortToast(context,"水印照片已成功保存至\n"+storePath);
                    break;
                case TOBYTE_CODE:
                    showImgDialog();
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_photo,null);
        initView();
        return view;
    }

    private void initView() {
        phoneLayout=view.findViewById(R.id.phoneLayout);
        loctionLayout=view.findViewById(R.id.loctionLayout);
        weatherLayout=view.findViewById(R.id.weatherLayout);
        photoLayout=view.findViewById(R.id.photoLayout);
        scoreLayout=view.findViewById(R.id.scoreLayout);
        themeLayout=view.findViewById(R.id.themeLayout);
        settingLayout=view.findViewById(R.id.settingLayout);
        shareLayout=view.findViewById(R.id.shareLayout);
        phoneTv=view.findViewById(R.id.phoneTv);
        loctionTv=view.findViewById(R.id.loctionTv);
        weatherTv=view.findViewById(R.id.weatherTv);
        imgView=view.findViewById(R.id.imgView);
        defaultPhoneTv=view.findViewById(R.id.defaultPhoneTv);
        defaultLoctionTv=view.findViewById(R.id.defaultLoctionTv);
        defaultWeatherTv=view.findViewById(R.id.defaultWeatherTv);
        phoneTv.setOnClickListener(this);
        loctionTv.setOnClickListener(this);
        weatherTv.setOnClickListener(this);
        defaultPhoneTv.setOnClickListener(this);
        defaultLoctionTv.setOnClickListener(this);
        defaultWeatherTv.setOnClickListener(this);
        photoLayout.setOnClickListener(this);
        scoreLayout.setOnClickListener(this);
        themeLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);


        phoneTv.setText((String) SharedPrefencesUtils.getParam(context,Contants.MODEL,"点击左侧设置当前机型"));
        loctionTv.setText((String) SharedPrefencesUtils.getParam(context,Contants.LOCTION,"点击左侧设置当前城市"));
        weatherTv.setText((String) SharedPrefencesUtils.getParam(context,Contants.WEATHER,"点击左侧设置当前天气"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.phoneTv:
                showDialog(1);
                break;
            case R.id.loctionTv:
                showDialog(2);
                break;
            case R.id.weatherTv:
                showDialog(3);
                break;
            case R.id.defaultPhoneTv:
                phoneTv.setText(android.os.Build.BRAND+" "+android.os.Build.MODEL);
                SharedPrefencesUtils.setParam(context,Contants.MODEL,phoneTv.getText().toString());
                break;
            case R.id.defaultLoctionTv:
                loctionTv.setText((String) SharedPrefencesUtils.getParam(context,Contants.CITY,""));
                SharedPrefencesUtils.setParam(context,Contants.LOCTION,loctionTv.getText().toString());
                break;
            case R.id.defaultWeatherTv:
                weatherTv.setText(SharedPrefencesUtils.getParam(context,Contants.TMP,"")+"℃ "
                        +SharedPrefencesUtils.getParam(context,Contants.TXT,""));
                SharedPrefencesUtils.setParam(context,Contants.WEATHER,weatherTv.getText().toString());
                break;
            case R.id.photoLayout:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    CommonUtils.showShortToast(context,"当前没有读写权限");
                    return ;
                }
                showActionSheet();
                break;
            case R.id.scoreLayout:
                goToMarket();
                break;
            case R.id.themeLayout:
                selectColor();
                break;
            case R.id.settingLayout:
                Intent it=new Intent(context, SettingActivity.class);
                startActivity(it);
                break;
            case R.id.shareLayout:
                break;

        }

    }

    private void selectColor(){
        List<String> txtList = new ArrayList();
        txtList.add("青");txtList.add("紫");txtList.add("红");txtList.add("粉");
        txtList.add("绿");txtList.add("蓝");txtList.add("橙");txtList.add("灰");

        List<Integer> colorList = new ArrayList();
        colorList.add(R.color.cyanColorAccent);colorList.add(R.color.purpleColorAccent);
        colorList.add(R.color.redColorAccent);colorList.add(R.color.pinkColorAccent);
        colorList.add(R.color.greenColorAccent);colorList.add(R.color.blueColorAccent);
        colorList.add(R.color.orangeColorAccent);colorList.add(R.color.greyColorAccent);

        SelectColorAdapter adapter = new SelectColorAdapter(getActivity(),txtList,colorList );
        AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPrefencesUtils.setParam(context,Contants.THEME,which);
                        getActivity().recreate();
                    }
                }).create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width  =  CommonUtils.dp2px(context,200);
        //dialog.getWindow().setAttributes(lp);

    }

    private void goToMarket() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            if(CommonUtils.isContains(context,"com.tencent.android.qqdownloader")){
                goToMarket.setPackage("com.tencent.android.qqdownloader");
            }
            if(CommonUtils.isContains(context,"com.coolapk.market")) {
                goToMarket.setPackage("com.coolapk.market");
            }
            context.startActivity(goToMarket);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.ma.lightweather"));
            startActivity(intent);
        }
    }



    private void showActionSheet() {
        new ActionSheetDialog(getActivity()).builder().setCancelable(true).setCanceledOnTouchOutside(true)
                .setTitle("选择照片")
                .addSheetItem("拍照", null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        cameraMethod();
                    }
                }).addSheetItem("相册", null, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, RESULT_PICTURE);
            }
        }).setCanceledOnTouchOutside(true).show();
    }

    private void showDialog(final int tag){
        final View titleView =LayoutInflater.from(context).inflate(R.layout.item_title_dialog, null);
        final TextView textView=titleView.findViewById(R.id.dialogTitle);
        textView.setText("请输入");
        textView.setTextColor(context.getResources().getColor(CommonUtils.getTextColor(context)));

        final View contentView =LayoutInflater.from(context).inflate(R.layout.item_edit_dialog, null);
        final EditText editText=contentView.findViewById(R.id.editText);
        GradientDrawable gradientDrawable = (GradientDrawable) editText.getBackground();
        gradientDrawable.setStroke(CommonUtils.dp2px(context, 1),context.getResources().getColor(CommonUtils.getBackColor(context)));

        new AlertDialog.Builder(getActivity())
                .setCustomTitle(titleView)
                .setView(contentView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s=editText.getText().toString();
                        if(tag==1){
                            phoneTv.setText(s);
                            SharedPrefencesUtils.setParam(context,Contants.MODEL,phoneTv.getText().toString());
                        }if(tag==2){
                            loctionTv.setText(s);
                            SharedPrefencesUtils.setParam(context,Contants.LOCTION,loctionTv.getText().toString());
                        }if(tag==3){
                            weatherTv.setText(s);
                            SharedPrefencesUtils.setParam(context,Contants.WEATHER,weatherTv.getText().toString());
                        }
                    }
                })
                .setNegativeButton("取消",null).show();
    }


    private void showImgDialog(){

        final View titleView =LayoutInflater.from(context).inflate(R.layout.item_title_dialog, null);
        final TextView textView=titleView.findViewById(R.id.dialogTitle);
        textView.setText("是否保存水印照片");
        textView.setTextColor(context.getResources().getColor(CommonUtils.getTextColor(context)));

        final View contentView =LayoutInflater.from(context).inflate(R.layout.item_img_dialog, null);
        final ImageView imageView=contentView.findViewById(R.id.imgView);
        Glide.with(context).load(bytes).into(imageView);

        if(progressDialog!=null) {
            progressDialog.cancel();
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(getActivity()).setTitle("是否保存")
                .setCustomTitle(titleView)
                .setView(contentView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean isSave=PhotoUtils.saveImageToGallery(context,bitmap);
                                bitmap.recycle();
                                if(isSave){
                                    handler.sendEmptyMessage(SAVE_CODE);
                                }
                            }
                        }).start();

                    }
                })
                .setNegativeButton("取消",null).show();

    }

    private void cameraMethod() {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        strImgPath = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
        filename = Calendar.getInstance().getTimeInMillis() + ".png";
        out = new File(strImgPath);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(strImgPath, filename);
        strImgPath = strImgPath + filename;
        imgUrl = PhotoUtils.getUriForFile(context,out);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(imageCaptureIntent, RESULT_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver contentResolver = context.getContentResolver();
        bitmap=null;
            switch (requestCode) {
                case RESULT_PHOTO://拍照
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUrl);
                        int degree=PhotoUtils.readPictureDegree(strImgPath);
                        bitmap=PhotoUtils.toTurn(bitmap,degree);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case RESULT_PICTURE://相册
                    try {
                        if(data!=null){
                            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.getData()));
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            if(bitmap!=null){
                bitmap=CommonUtils.drawTextToRightBottom(context,bitmap,phoneTv.getText().toString(),loctionTv.getText().toString(),weatherTv.getText().toString());
                progressDialog=ProgressDialog.show(getActivity(), null, "正在生成水印照片");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Matrix matrix = new Matrix();
                        matrix.setScale(0.5f, 0.5f);
                        Bitmap small = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        small.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        bytes=baos.toByteArray();
                        small.recycle();
                        handler.sendEmptyMessage(TOBYTE_CODE);
                    }
                }).start();
            }


    }

}
