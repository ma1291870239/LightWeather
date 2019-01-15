package com.ma.lightweather.widget;

import android.content.Context;
import android.graphics.Canvas;
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
    private Path maxPath=new Path();
    private Path minPath=new Path();

    private List<Integer> maxList=new ArrayList<>();
    private List<Integer> minList=new ArrayList<>();
    private List<Float> midelMinList=new ArrayList<>();
    private List<String> dateList=new ArrayList<>();
    private List<String> txtList=new ArrayList<>();
    private List<String> dirList=new ArrayList<>();

    private static int lineWidth=3;
    private static int pointWidth=3;
    private static int outPointWidth=3;
    private static int pointRadius=5;
    private static int outPointRadius=10;

    private int xSpace;
    private int offsetHigh;
    private int ySpace;
    private int max;
    private int min;
    private int textHigh;
    private int textSpace;

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
        maxPaint.setStyle(Paint.Style.STROKE);

        minPaint.setColor(getResources().getColor(R.color.temp_low));
        minPaint.setAntiAlias(true);
        minPaint.setStrokeWidth(lineWidth);
        minPaint.setStyle(Paint.Style.STROKE);

        textPaint.setColor(getResources().getColor(R.color.text));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewhigh=getMeasuredHeight();
        int viewwidth=getMeasuredWidth();
        textPaint.setTextSize(viewwidth/30);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHigh =(int)Math.ceil(fm.bottom - fm.top);
        textSpace=(int)Math.ceil(fm.bottom - fm.leading)+(int)Math.ceil(fm.ascent - fm.top);
        offsetHigh=(int)(3.5* textHigh);
        xSpace=viewwidth/14;
        ySpace=(viewhigh-2*offsetHigh) /50;
        if(maxList.size()>0&&minList.size()>0) {
            max = Collections.max(maxList);
            min = Collections.min(minList);
            ySpace = (viewhigh-2*offsetHigh) / (max - min);
        }
        maxPath.reset();
        minPath.reset();
        //最高温度折线
        for(int i=0;i<maxList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp_high));
            outPointPaint.setColor(getResources().getColor(R.color.temp_high));
//            if(i<maxList.size()-1){
//                canvas.drawLine(getX(2*i+1),getY(i,maxList),
//                        getX(2*i+3),getY(i+1,maxList),maxPaint);
//            }
            float x=getX(2*i+2);
            float k1=0;
            float k2=0;
            float y1=0;
            float y2=0;
            if(i==0){
                k2=(getY(i+2,maxList)-getY(i,maxList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,maxList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,maxList);
                maxPath.moveTo(getX(2*i+1),getY(i,maxList));
                maxPath.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,maxList));
            } else if(i<maxList.size()-2){
                k1=(getY(i+1,maxList)-getY(i-1,maxList))/getX(4);
                k2=(getY(i+2,maxList)-getY(i,maxList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,maxList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,maxList);
                maxPath.moveTo(getX(2*i+1),getY(i,maxList));
                maxPath.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,maxList));
            }else if(i==maxList.size()-2){
                k1=(getY(i+1,maxList)-getY(i-1,maxList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,maxList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,maxList);
                maxPath.moveTo(getX(2*i+1),getY(i,maxList));
                maxPath.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,maxList));
            }
            canvas.drawPath(maxPath,maxPaint);
//            canvas.drawCircle(getX(2*i+1),getY(i,maxList),pointRadius,pointPaint);
//            canvas.drawCircle(getX(2*i+1),getY(i,maxList),outPointRadius,outPointPaint);
            canvas.drawText(maxList.get(i)+"°",getX(2*i+1),getY(i,maxList)-textSpace,textPaint);
        }
        //最低温度折线
        for(int i=0;i<minList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp_low));
            outPointPaint.setColor(getResources().getColor(R.color.temp_low));
//            if(i<minList.size()-1){
//                canvas.drawLine((2*i+1)*xSpace,(max-minList.get(i))*ySpace+offsetHigh,
//                        (2*i+3)*xSpace,(max-minList.get(i+1))*ySpace+offsetHigh,minPaint);
//            }
            float x=getX(2*i+2);
            float k1=0;
            float k2=0;
            float y1=0;
            float y2=0;
            if(i==0){
                k2=(getY(i+2,minList)-getY(i,minList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,minList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,minList);
                minPath.moveTo(getX(2*i+1),getY(i,minList));
                minPath.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,minList));
            } else if(i<minList.size()-2){
                k1=(getY(i+1,minList)-getY(i-1,minList))/getX(4);
                k2=(getY(i+2,minList)-getY(i,minList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,minList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,minList);
                minPath.moveTo(getX(2*i+1),getY(i,minList));
                minPath.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,minList));
            }else if(i==minList.size()-2){
                k1=(getY(i+1,minList)-getY(i-1,minList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,minList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,minList);
                minPath.moveTo(getX(2*i+1),getY(i,minList));
                minPath.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,minList));
            }
            canvas.drawPath(minPath,minPaint);
            //canvas.drawCircle(getX(2*i+1),getY(i,minList),pointRadius,pointPaint);
            //canvas.drawCircle(getX(2*i+1),getY(i,minList),outPointRadius,outPointPaint);
            canvas.drawText(minList.get(i)+"°",getX(2*i+1),getY(i,minList)+ textHigh,textPaint);
        }
        //日期
        for (int i=0;i<dateList.size();i++){
            String[] data1=dateList.get(i).split("-");
            canvas.drawText(data1[1]+"/"+data1[2],getX(2*i+1), textHigh,textPaint);
        }
        //天气状况
        for (int i=0;i<txtList.size();i++){
            canvas.drawText(txtList.get(i),getX(2*i+1),viewhigh-textSpace,textPaint);
        }
        //分割线
        for (int i=1;i<dateList.size();i++){
            canvas.drawLine(getX(2*i),50,getX(2*i),viewhigh- textHigh,textPaint);
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

    private float getX(int i){
        return i*xSpace;
    }

    private float getY(int i,List<Integer> list){
        return (max-list.get(i))*ySpace+offsetHigh;
    }

}
