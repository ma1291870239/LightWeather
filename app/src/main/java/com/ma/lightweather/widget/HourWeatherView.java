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
 * Created by Aeolus on 2018/8/24.
 */

public class HourWeatherView extends View {

    private Paint pointPaint=new Paint();
    private Paint outPointPaint=new Paint();
    private Paint textPaint=new Paint();
    private Paint tmpPaint =new Paint();
    private Path path=new Path();

    private List<Integer> tmpList=new ArrayList<>();
    private List<Float> midelTmpList=new ArrayList<>();
    private List<Integer> popList=new ArrayList<>();
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

    public HourWeatherView(Context context, AttributeSet attrs) {
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

        tmpPaint.setColor(getResources().getColor(R.color.temp));
        tmpPaint.setAntiAlias(true);
        tmpPaint.setStrokeWidth(lineWidth);
        tmpPaint.setStyle(Paint.Style.STROKE);

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
        offsetHigh=3*textHigh;
        xSpace=viewwidth/16;
        ySpace=(viewhigh-2*offsetHigh)/50;
        if(tmpList.size()>0) {
            max = Collections.max(tmpList);
            min = Collections.min(tmpList);
            ySpace = (viewhigh-2*offsetHigh) / (max - min);
        }
        path.reset();
        //实时温度折线
        for(int i=0;i<tmpList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp));
            outPointPaint.setColor(getResources().getColor(R.color.temp));
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
                k2=(getY(i+2,tmpList)-getY(i,tmpList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,tmpList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,tmpList);
                path.moveTo(getX(2*i+1),getY(i,tmpList));
                path.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,tmpList));
            } else if(i<tmpList.size()-2){
                k1=(getY(i+1,tmpList)-getY(i-1,tmpList))/getX(4);
                k2=(getY(i+2,tmpList)-getY(i,tmpList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,tmpList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,tmpList);
                path.moveTo(getX(2*i+1),getY(i,tmpList));
                path.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,tmpList));
            }else if(i==tmpList.size()-2){
                k1=(getY(i+1,tmpList)-getY(i-1,tmpList))/getX(4);
                y1=(x-getX(2*i+1))*k1+getY(i,tmpList);
                y2=(x-getX(2*i+3))*k2+getY(i+1,tmpList);
                path.moveTo(getX(2*i+1),getY(i,tmpList));
                path.cubicTo(x,y1, x,y2, getX(2*i+3),getY(i+1,tmpList));
            }
            canvas.drawPath(path,tmpPaint);
//            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),pointRadius,pointPaint);
//            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),outPointRadius,outPointPaint);
            canvas.drawText(tmpList.get(i)+"°",getX(2*i+1),getY(i,tmpList)-textSpace,textPaint);
        }
        //日期
        for (int i=0;i<dateList.size();i++){
            String[] data1=dateList.get(i).split(" ");
            canvas.drawText(data1[1],getX(2*i+1), textHigh,textPaint);
        }
        //天气状况
        for (int i=0;i<txtList.size();i++){
            canvas.drawText(txtList.get(i),getX(2*i+1),viewhigh-textSpace,textPaint);
        }
        //降水概率
        for (int i=0;i<popList.size();i++){
            canvas.drawText(popList.get(i)+"%",getX(2*i+1),viewhigh-textSpace- textHigh,textPaint);
        }
        //分割线
        for (int i=1;i<dateList.size();i++){
            canvas.drawLine(getX(2*i),50,getX(2*i),viewhigh- textHigh,textPaint);
        }
    }

    public void loadViewData(List<Integer> tmpList, List<Integer> popList, List<String> dateList , List<String> txtList, List<String> dirList) {
        this.tmpList=tmpList;
        this.popList=popList;
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

