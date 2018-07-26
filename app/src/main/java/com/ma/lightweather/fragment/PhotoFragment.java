package com.ma.lightweather.fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.ma.lightweather.R;
import com.ma.lightweather.model.Contants;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.PhotoUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;
import com.ma.lightweather.widget.ActionSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by Ma-PC on 2016/12/5.
 */
public class PhotoFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private LinearLayout phoneLayout,loctionLayout,weatherLayout,photoLayout,scoreLayout,shareLayout;
    private TextView defaultPhoneTv,defaultLoctionTv,defaultWeatherTv,phoneTv,loctionTv,weatherTv;
    private ImageView imgView;

    private String strImgPath, filename;
    private Uri imgUrl;
    private File out;
    private Bitmap bitmap;

    private static final int RESULT_PHOTO=1;
    private static final int RESULT_PICTURE=2;
    private static final int SAVE_CODE=200;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAVE_CODE:
                    CommonUtils.showShortToast(getActivity(),"水印照片保存成功");
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
        shareLayout.setOnClickListener(this);


        phoneTv.setText((String) SharedPrefencesUtils.getParam(getActivity(),Contants.MODEL,"Surface Lumia"));
        loctionTv.setText((String) SharedPrefencesUtils.getParam(getActivity(),Contants.LOCTION,"M78星云"));
        weatherTv.setText((String) SharedPrefencesUtils.getParam(getActivity(),Contants.WEATHER,"100℃ 台风地震"));
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
                SharedPrefencesUtils.setParam(getActivity(),Contants.MODEL,phoneTv.getText().toString());
                break;
            case R.id.defaultLoctionTv:
                loctionTv.setText((String) SharedPrefencesUtils.getParam(getActivity(),Contants.CITY,""));
                SharedPrefencesUtils.setParam(getActivity(),Contants.LOCTION,loctionTv.getText().toString());
                break;
            case R.id.defaultWeatherTv:
                weatherTv.setText((String) SharedPrefencesUtils.getParam(getActivity(),Contants.TMP,"")+"℃ "
                        +(String) SharedPrefencesUtils.getParam(getActivity(),Contants.TXT,""));
                SharedPrefencesUtils.setParam(getActivity(),Contants.WEATHER,weatherTv.getText().toString());
                break;
            case R.id.photoLayout:
                showActionSheet();
                break;
            case R.id.scoreLayout:
                goToMarket();
                break;
            case R.id.shareLayout:
                break;

        }

    }


    public void goToMarket() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            if(CommonUtils.isContains(getActivity(),"com.tencent.android.qqdownloader")){
                goToMarket.setPackage("com.tencent.android.qqdownloader");
            }
            if(CommonUtils.isContains(getActivity(),"com.coolapk.market")) {
                goToMarket.setPackage("com.coolapk.market");
            }
            getActivity().startActivity(goToMarket);
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
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.GREY, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        cameraMethod();
                    }
                }).addSheetItem("相册", ActionSheetDialog.SheetItemColor.GREY, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, RESULT_PICTURE);
            }
        }).setCanceledOnTouchOutside(true).show();
    }

    private void showDialog(final int tag){
        final View view =LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog, null);
        final EditText edit=view.findViewById(R.id.editText);//获得输入框对象
        new AlertDialog.Builder(getActivity()).setTitle("请输入")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s=edit.getText().toString();
                        if(tag==1){
                            phoneTv.setText(s);
                            SharedPrefencesUtils.setParam(getActivity(),Contants.MODEL,phoneTv.getText().toString());
                        }if(tag==2){
                            loctionTv.setText(s);
                            SharedPrefencesUtils.setParam(getActivity(),Contants.LOCTION,loctionTv.getText().toString());
                        }if(tag==3){
                            weatherTv.setText(s);
                            SharedPrefencesUtils.setParam(getActivity(),Contants.WEATHER,weatherTv.getText().toString());
                        }
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
        imgUrl = PhotoUtils.getUriForFile(getActivity(),out);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(imageCaptureIntent, RESULT_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver contentResolver = getActivity().getContentResolver();
            switch (requestCode) {
                case RESULT_PHOTO://拍照
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case RESULT_PICTURE://相册
                    try {
                        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.getData()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            bitmap=CommonUtils.drawTextToRightBottom(getActivity(),bitmap,phoneTv.getText().toString(),loctionTv.getText().toString(),weatherTv.getText().toString());
            //imgView.setImageBitmap(bitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isSave=PhotoUtils.saveImageToGallery(getActivity(),bitmap);
                    if(isSave){
                        handler.sendEmptyMessage(SAVE_CODE);
                    }
                }
            }).start();
    }

}