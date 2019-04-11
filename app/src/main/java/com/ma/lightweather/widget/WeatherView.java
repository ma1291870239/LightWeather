package com.ma.lightweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ma.lightweather.R;
import com.ma.lightweather.model.HeFengWeather;

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

    private List<HeFengWeather.Daily> dailyList=new ArrayList<>();
    private List<Integer> maxList=new ArrayList<>();
    private List<Integer> minList=new ArrayList<>();
    private List<String> dateList=new ArrayList<>();
    private List<String> txtList=new ArrayList<>();
    private List<String> dirList=new ArrayList<>();

    private static int lineWidth=4;
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

            float k1=0;//当前点斜率
            float k2=0;//下一个点斜率
            float b1=0;//当前点常量
            float b2=0;//下一个点常量
            float x1=0;// 当前点后控制点x坐标
            float y1=0;// 当前点后控制点y坐标
            float x2=0;// 下一个点前控制点x坐标
            float y2=0;// 下一个点前控制点y坐标
            double r=0.5;//控制点与经过点的距离  取值0-1  越大距离越近
            if(i==0){
                x1=getX(2*i+2-r);
                y1=getMaxY(i);
                k2=(getMaxY(i+2)-getMaxY(i))/getX(4);
                b2=getMaxY(i+1)-k2*getX(2*i+3);
                x2=getX(2*i+2+r);
                y2=x2*k2+b2;
                maxPath.moveTo(getX(2*i+1),getMaxY(i));
                maxPath.cubicTo(x1,y1, x2,y2, getX(2*i+3),getMaxY(i+1));
            } else if(i<maxList.size()-2){
                k1=(getMaxY(i+1)-getMaxY(i-1))/getX(4);
                b1=getMaxY(i)-k1*getX(2*i+1);
                x1=getX(2*i+2-r);
                y1=x1*k1+b1;
                k2=(getMaxY(i+2)-getMaxY(i))/getX(4);
                b2=getMaxY(i+1)-k2*getX(2*i+3);
                x2=getX(2*i+2+r);
                y2=x2*k2+b2;
                maxPath.moveTo(getX(2*i+1),getMaxY(i));
                maxPath.cubicTo(x1,y1, x2,y2, getX(2*i+3),getMaxY(i+1));
            }else if(i==maxList.size()-2){
                k1=(getMaxY(i+1)-getMaxY(i-1))/getX(4);
                b1=getMaxY(i)-k1*getX(2*i+1);
                x1=getX(2*i+2-r);
                y1=x1*k1+b1;
                x2=getX(2*i+2+r);
                y2=getMaxY(i);
                maxPath.moveTo(getX(2*i+1),getMaxY(i));
                maxPath.cubicTo(x1,y1, x2,y2, getX(2*i+3),getMaxY(i+1));
            }

            canvas.drawPath(maxPath,maxPaint);
//            if(i<maxList.size()-1){
//                canvas.drawLine(getX(2*i+1),getY(i,maxList),
//                        getX(2*i+3),getY(i+1,maxList),maxPaint);
//            }
//            canvas.drawCircle(getX(2*i+1),getY(i,maxList),pointRadius,pointPaint);
//            canvas.drawCircle(getX(2*i+1),getY(i,maxList),outPointRadius,outPointPaint);
            canvas.drawText(maxList.get(i)+"°",getX(2*i+1),getMaxY(i)-textSpace,textPaint);
        }
        //最低温度折线
        for(int i=0;i<minList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp_low));
            outPointPaint.setColor(getResources().getColor(R.color.temp_low));

            float k1=0;//当前点斜率
            float k2=0;//下一个点斜率
            float b1=0;//当前点常量
            float b2=0;//下一个点常量
            float x1=0;// 当前点后控制点x坐标
            float y1=0;// 当前点后控制点y坐标
            float x2=0;// 下一个点前控制点x坐标
            float y2=0;// 下一个点前控制点y坐标
            double r=0.5;//控制点与经过点的距离  取值0-1  越大距离越近
            if(i==0){
                x1=getX(2*i+2-r);
                y1=getMinY(i);
                k2=(getMinY(i+2)-getMinY(i))/getX(4);
                b2=getMinY(i+1)-k2*getX(2*i+3);
                x2=getX(2*i+2+r);
                y2=x2*k2+b2;
                minPath.moveTo(getX(2*i+1),getMinY(i));
                minPath.cubicTo(x1,y1, x2,y2, getX(2*i+3),getMinY(i+1));
            } else if(i<minList.size()-2){
                k1=(getMinY(i+1)-getMinY(i-1))/getX(4);
                b1=getMinY(i)-k1*getX(2*i+1);
                x1=getX(2*i+2-r);
                y1=x1*k1+b1;
                k2=(getMinY(i+2)-getMinY(i))/getX(4);
                b2=getMinY(i+1)-k2*getX(2*i+3);
                x2=getX(2*i+2+r);
                y2=x2*k2+b2;
                minPath.moveTo(getX(2*i+1),getMinY(i));
                minPath.cubicTo(x1,y1, x2,y2, getX(2*i+3),getMinY(i+1));
            }else if(i==minList.size()-2){
                k1=(getMinY(i+1)-getMinY(i-1))/getX(4);
                b1=getMinY(i)-k1*getX(2*i+1);
                x1=getX(2*i+2-r);
                y1=x1*k1+b1;
                x2=getX(2*i+2+r);
                y2=getMinY(i);
                minPath.moveTo(getX(2*i+1),getMinY(i));
                minPath.cubicTo(x1,y1, x2,y2, getX(2*i+3),getMinY(i+1));
            }
            canvas.drawPath(minPath,minPaint);
//            if(i<minList.size()-1){
//                canvas.drawLine((2*i+1)*xSpace,(max-minList.get(i))*ySpace+offsetHigh,
//                        (2*i+3)*xSpace,(max-minList.get(i+1))*ySpace+offsetHigh,minPaint);
//            }
//            canvas.drawCircle(getX(2*i+1),getY(i,minList),pointRadius,pointPaint);
//            canvas.drawCircle(getX(2*i+1),getY(i,minList),outPointRadius,outPointPaint);
            canvas.drawText(minList.get(i)+"°",getX(2*i+1),getMinY(i)+ textHigh,textPaint);
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

    public void loadViewData(List<HeFengWeather.Daily> dailyList) {
        this.dailyList=dailyList;
        maxList.clear();
        minList.clear();
        dateList.clear();
        txtList.clear();
        dirList.clear();
        for (int i=0;i<dailyList.size();i++){
            maxList.add(dailyList.get(i).getTmp_max());
            minList.add(dailyList.get(i).getTmp_min());
            dateList.add(dailyList.get(i).getDate());
            txtList.add(dailyList.get(i).getCond_txt_d());
            dirList.add(dailyList.get(i).getWind_dir());
        }
        postInvalidate();
    }

    private float getX(double i){
        return Float.valueOf(String.valueOf(i*xSpace));
    }

    private float getMaxY(int i){
        return (max-maxList.get(i))*ySpace+offsetHigh;
    }

    private float getMinY(int i){
        return (max-minList.get(i))*ySpace+offsetHigh;
    }

}
