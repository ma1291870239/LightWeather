package com.ma.lightweather.fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.ma.lightweather.R;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.TakePhotoUtils;
import com.ma.lightweather.widget.ActionSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ma-PC on 2016/12/5.
 */
public class PhotoFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private LinearLayout phoneLayout,weatherLayout,photoLayout,scoreLayout,shareLayout;
    private TextView phoneTv,weatherTv;
    private ImageView imgView;

    private String strImgPath, filename;
    private Uri imgUrl;
    private File out;
    private Bitmap bitmap;

    private static final int RESULT_PHOTO=1;
    private static final int RESULT_PICTURE=2;
    private static final int RESULT_CUT=3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_photo,null);
        initView();
        return view;
    }

    private void initView() {
        phoneLayout=view.findViewById(R.id.phoneLayout);
        weatherLayout=view.findViewById(R.id.weatherLayout);
        photoLayout=view.findViewById(R.id.photoLayout);
        scoreLayout=view.findViewById(R.id.scoreLayout);
        shareLayout=view.findViewById(R.id.shareLayout);
        phoneTv=view.findViewById(R.id.phoneTv);
        weatherTv=view.findViewById(R.id.weatherTv);
        imgView=view.findViewById(R.id.imgView);
        phoneLayout.setOnClickListener(this);
        weatherLayout.setOnClickListener(this);
        photoLayout.setOnClickListener(this);
        scoreLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.phoneLayout:
                showDialog(1);
                break;
            case R.id.weatherLayout:
                showDialog(2);
                break;
            case R.id.photoLayout:
                showActionSheet();
                break;
            case R.id.scoreLayout:
                break;
            case R.id.shareLayout:
                break;

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
                        }if(tag==2){
                            weatherTv.setText(s);
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
        imgUrl = TakePhotoUtils.getUriForFile(getActivity(),out);
        //imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(imageCaptureIntent, RESULT_PHOTO);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_PHOTO://拍照
                if(data!=null){
                    Bundle bundle = data.getExtras();   //获取data数据集合
                    bitmap= (Bitmap) bundle.get("data");        //获得data数据
                    //imgView.setImageBitmap(bitmap);
                }
                break;
            case RESULT_PICTURE://相册
                if(data!=null) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    try {
                        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.getData()));
                        //imgView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        bitmap=CommonUtils.drawTextToLeftBottom(getActivity(),bitmap,phoneTv.getText().toString(),10,R.color.white,15,15);
        bitmap=CommonUtils.drawTextToRightBottom(getActivity(),bitmap,weatherTv.getText().toString(),10,R.color.white,15,15);
        imgView.setImageBitmap(bitmap);
    }
}
