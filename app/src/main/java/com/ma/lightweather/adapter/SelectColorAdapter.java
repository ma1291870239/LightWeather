package com.ma.lightweather.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ma.lightweather.R;

import java.util.List;

/**
 * Created by Aeolus on 2018/12/25.
 */

public class SelectColorAdapter extends BaseAdapter {

    private List<String> textList;
    private List<Integer> colorList;
    private Context context;

    public SelectColorAdapter(Context context, List<String> textList,List<Integer> colorList) {
        this.textList = textList;
        this.colorList=colorList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return textList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= getCount() || textList == null) {
            return null;
        }
        return textList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView =LayoutInflater.from(context).inflate(R.layout.item_selectcolor_dialog, null);
            holder.txtTextView =convertView.findViewById(R.id.txtTextView);
            holder.colorTextView =convertView.findViewById(R.id.colorTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTextView.setText(textList.get(position));
        //holder.colorTextView.setBackgroundResource(R.drawable.bg_weather_round_grey);
        GradientDrawable gradientDrawable = (GradientDrawable) holder.colorTextView.getBackground();
        gradientDrawable.setColor(context.getResources().getColor(colorList.get(position)));
        return convertView;
    }

    public static class ViewHolder {
        public TextView txtTextView;
        public TextView colorTextView;
    }
}
