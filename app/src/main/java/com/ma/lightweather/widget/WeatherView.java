package com.ma.lightweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ma.lightweather.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/14.
 */
public class WeatherView extends View {

    private Paint pointPaint=new Paint();
    private Paint outPointPaint=new Paint();
    private Paint textPaint=new Paint();
    private Paint maxPaint=new Paint();
    private Paint minPaint=new Paint();
    private Path path=new Path();

    private List<Integer> maxList=new ArrayList<>();
    private List<Integer> minList=new ArrayList<>();
    private List<String> dateList=new ArrayList<>();
    private List<String> txtList=new ArrayList<>();
    private List<String> dirList=new ArrayList<>();

    private static int lineWidth=3;
    private static int pointWidth=3;
    private static int outPointWidth=3;
    private static int pointRadius=5;
    private static int outPointRadius=10;
    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(pointWidth);
        pointPaint.setStyle(Paint.Style.FILL);

        outPointPaint.setAntiAlias(true);
        outPointPaint.setStrokeWidth(outPointWidth);
        outPointPaint.setStyle(Paint.Style.STROKE);

        maxPaint.setColor(getResources().getColor(R.color.temp_high));
        maxPaint.setAntiAlias(true);
        maxPaint.setStrokeWidth(lineWidth);
        maxPaint.setStyle(Paint.Style.FILL);

        minPaint.setColor(getResources().getColor(R.color.temp_low));
        minPaint.setAntiAlias(true);
        minPaint.setStrokeWidth(lineWidth);
        minPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(getResources().getColor(R.color.text));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewhigh=getMeasuredHeight();
        int viewwidth=getMeasuredWidth();
        textPaint.setTextSize(viewwidth/30);
        int xSpace=viewwidth/14;
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int texthigh=(int)Math.ceil(fm.bottom - fm.top);
        int offsetHigh=4*texthigh+10;
        int ySpace=(viewhigh-6*texthigh-40) /50;
        int max =0;
        int min=0;
        if(maxList.size()>0&&minList.size()>0) {
            max = Collections.max(maxList);
            min = Collections.min(minList);
            ySpace = (viewhigh-8*texthigh-40) / (max - min);
        }

        //最高温度折线
        for(int i=0;i<maxList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp_high));
            outPointPaint.setColor(getResources().getColor(R.color.temp_high));
            if(i<maxList.size()-1){
                canvas.drawLine((2*i+1)*xSpace,(max-maxList.get(i))*ySpace+offsetHigh,
                        (2*i+3)*xSpace,(max-maxList.get(i+1))*ySpace+offsetHigh,maxPaint);
            }
            canvas.drawCircle((2*i+1)*xSpace,(max-maxList.get(i))*ySpace+offsetHigh,pointRadius,pointPaint);
            canvas.drawCircle((2*i+1)*xSpace,(max-maxList.get(i))*ySpace+offsetHigh,outPointRadius,outPointPaint);
            canvas.drawText(maxList.get(i)+"°",(2*i+1)*xSpace,(max-maxList.get(i))*ySpace+offsetHigh-20,textPaint);
        }
        //最低温度折线
        for(int i=0;i<minList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp_low));
            outPointPaint.setColor(getResources().getColor(R.color.temp_low));
            if(i<minList.size()-1){
                canvas.drawLine((2*i+1)*xSpace,(max-minList.get(i))*ySpace+offsetHigh,
                        (2*i+3)*xSpace,(max-minList.get(i+1))*ySpace+offsetHigh,minPaint);
            }
            canvas.drawCircle((2*i+1)*xSpace,(max-minList.get(i))*ySpace+offsetHigh,pointRadius,pointPaint);
            canvas.drawCircle((2*i+1)*xSpace,(max-minList.get(i))*ySpace+offsetHigh,outPointRadius,outPointPaint);
            canvas.drawText(minList.get(i)+"°",(2*i+1)*xSpace,(max-minList.get(i))*ySpace+offsetHigh+texthigh+5,textPaint);
        }
        //日期
        for (int i=0;i<dateList.size();i++){
            String[] data1=dateList.get(i).split("-");
            canvas.drawText(data1[1]+"/"+data1[2],(2*i+1)*xSpace,texthigh,textPaint);
        }
        //天气状况
        for (int i=0;i<txtList.size();i++){
            canvas.drawText(txtList.get(i),(2*i+1)*xSpace,viewhigh-(int)Math.ceil(fm.bottom - fm.leading),textPaint);
        }
        //分割线
        for (int i=1;i<dateList.size();i++){
            Log.e("abc", "onDraw: "+i+dateList.size() );
            canvas.drawLine(2*i*xSpace,50,2*i*xSpace,viewhigh-texthigh,textPaint);
        }
    }

    public void loadViewData(List<Integer> maxList, List<Integer> minList, List<String> dateList , List<String> txtList, List<String> dirList) {
        this.maxList=maxList;
        this.minList=minList;
        this.dateList=dateList;
        this.txtList=txtList;
        this.dirList=dirList;
        postInvalidate();
    }

}
