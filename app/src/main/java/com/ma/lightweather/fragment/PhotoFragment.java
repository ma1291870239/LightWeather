package com.ma.lightweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma.lightweather.R;

/**
 * Created by Ma-PC on 2016/12/5.
 */
public class PhotoFragment extends BaseFragment{

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_photo,null);
        return view;
    }
}
