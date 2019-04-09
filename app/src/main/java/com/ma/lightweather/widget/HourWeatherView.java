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

import java.math.BigDecimal;
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
        //getFoldLine(canvas);
        getCurveLine(canvas);
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

    private void  getFoldLine(Canvas canvas){
        for(int i=0;i<tmpList.size();i++) {
            pointPaint.setColor(getResources().getColor(R.color.temp));
            outPointPaint.setColor(getResources().getColor(R.color.temp));
            pointPaint.setColor(getResources().getColor(R.color.temp));
            outPointPaint.setColor(getResources().getColor(R.color.temp));
            if (i < tmpList.size() - 1) {
                canvas.drawLine((2 * i + 1) * xSpace, (max - tmpList.get(i)) * ySpace + offsetHigh,
                        (2 * i + 3) * xSpace, (max - tmpList.get(i + 1)) * ySpace + offsetHigh, tmpPaint);
            }

//            canvas.drawCircle(getX(2 * i + 1), getY(i, tmpList), pointRadius, pointPaint);
//            canvas.drawCircle(getX(2 * i + 1), getY(i, tmpList), outPointRadius, outPointPaint);
        }
    }

    /**
     * 需要四个点控制   当前点  当前点后控制点  下一个点  下一个点前控制点
     *
     */
    private void  getCurveLine(Canvas canvas){
        path.reset();
        for(int i=0;i<tmpList.size();i++){
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
                y1=getY(i);
                k2=(getY(i+2)-getY(i))/getX(4);
                b2=getY(i+1)-k2*getX(2*i+3);
                x2=getX(2*i+2+r);
                y2=x2*k2+b2;
                path.moveTo(getX(2*i+1),getY(i));
                path.cubicTo(x1,y1, x2,y2, getX(2*i+3),getY(i+1));
            } else if(i<tmpList.size()-2){
                k1=(getY(i+1)-getY(i-1))/getX(4);
                b1=getY(i)-k1*getX(2*i+1);
                x1=getX(2*i+2-r);
                y1=x1*k1+b1;
                k2=(getY(i+2)-getY(i))/getX(4);
                b2=getY(i+1)-k2*getX(2*i+3);
                x2=getX(2*i+2+r);
                y2=x2*k2+b2;
                path.moveTo(getX(2*i+1),getY(i));
                path.cubicTo(x1,y1, x2,y2, getX(2*i+3),getY(i+1));
            }else if(i==tmpList.size()-2){
                k1=(getY(i+1)-getY(i-1))/getX(4);
                b1=getY(i)-k1*getX(2*i+1);
                x1=getX(2*i+2-r);
                y1=x1*k1+b1;
                x2=getX(2*i+2+r);
                y2=getY(i);
                path.moveTo(getX(2*i+1),getY(i));
                path.cubicTo(x1,y1, x2,y2, getX(2*i+3),getY(i+1));
            }
            canvas.drawPath(path,tmpPaint);
            canvas.drawText(tmpList.get(i)+"°",getX(2*i+1),getY(i)-textSpace,textPaint);
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

    private float getX(double i){
        return Float.valueOf(String.valueOf(i*xSpace));
    }

    private float getY(int i){
        return (max-tmpList.get(i))*ySpace+offsetHigh;
    }

}

