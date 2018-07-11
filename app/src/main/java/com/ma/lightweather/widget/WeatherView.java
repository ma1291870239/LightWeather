package com.ma.lightweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.ma.lightweather.R;

import java.util.ArrayList;
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

        textPaint.setColor(Color.parseColor("#979797"));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewhigh=getMeasuredHeight();
        int viewwidth=getMeasuredWidth();
        textPaint.setTextSize(viewwidth/35);
        int x=viewwidth/14;
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int texthigh=(int)Math.ceil(fm.bottom - fm.top);
        int tmpspace=(viewhigh-4*texthigh-40)/100;

        //最高温度折线
        for(int i=0;i<maxList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp_high));
            outPointPaint.setColor(getResources().getColor(R.color.temp_high));
            if(i<maxList.size()-1){
                canvas.drawLine((2*i+1)*x,viewhigh/2-(maxList.get(i)*tmpspace),
                        (2*i+3)*x,viewhigh/2-(maxList.get(i+1)*tmpspace),maxPaint);
            }
            canvas.drawCircle((2*i+1)*x,viewhigh/2-(maxList.get(i)*tmpspace),pointRadius,pointPaint);
            canvas.drawCircle((2*i+1)*x,viewhigh/2-(maxList.get(i)*tmpspace),outPointRadius,outPointPaint);
            canvas.drawText(maxList.get(i)+"°",(2*i+1)*x,viewhigh/2-(maxList.get(i)*tmpspace)-20,textPaint);
        }
        //最低温度折线
        for(int i=0;i<minList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp_low));
            outPointPaint.setColor(getResources().getColor(R.color.temp_low));
            if(i<minList.size()-1){
                canvas.drawLine((2*i+1)*x,viewhigh/2-(minList.get(i)*tmpspace),
                        (2*i+3)*x,viewhigh/2-(minList.get(i+1)*tmpspace),minPaint);
            }
            canvas.drawCircle((2*i+1)*x,viewhigh/2-(minList.get(i)*tmpspace),pointRadius,pointPaint);
            canvas.drawCircle((2*i+1)*x,viewhigh/2-(minList.get(i)*tmpspace),outPointRadius,outPointPaint);
            canvas.drawText(minList.get(i)+"°",(2*i+1)*x,viewhigh/2-(minList.get(i)*tmpspace)+texthigh+5,textPaint);
        }
        //日期
        for (int i=0;i<dateList.size();i++){
            String[] data1=dateList.get(i).split("-");
            canvas.drawText(data1[1]+"/"+data1[2],(2*i+1)*x,texthigh,textPaint);
        }
        //天气状况
        for (int i=0;i<txtList.size();i++){
            canvas.drawText(txtList.get(i),(2*i+1)*x,viewhigh-(int)Math.ceil(fm.bottom - fm.leading),textPaint);
        }
        //分割线
        for (int i=1;i<dateList.size();i++){
            canvas.drawLine(2*i*x,50,2*i*x,viewhigh-texthigh,textPaint);
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
