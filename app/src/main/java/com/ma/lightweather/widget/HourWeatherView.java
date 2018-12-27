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
    private List<Integer> popList=new ArrayList<>();
    private List<String> dateList=new ArrayList<>();
    private List<String> txtList=new ArrayList<>();
    private List<String> dirList=new ArrayList<>();

    private static int lineWidth=3;
    private static int pointWidth=3;
    private static int outPointWidth=3;
    private static int pointRadius=5;
    private static int outPointRadius=10;

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
        tmpPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(getResources().getColor(R.color.text));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewhigh=getMeasuredHeight();
        int viewwidth=getMeasuredWidth();
        textPaint.setTextSize(viewwidth/30);
        int xSpace=viewwidth/16;
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int texthigh=(int)Math.ceil(fm.bottom - fm.top);

        int ySpace=(viewhigh-4*texthigh-40)/50;
        int offsetHigh=4*texthigh+10;
        int max =0;
        int min=0;
        if(tmpList.size()>0) {
            max = Collections.max(tmpList);
            min = Collections.min(tmpList);
            ySpace = (viewhigh-8*texthigh-40) / (max - min);
        }
        //实时温度折线
        for(int i=0;i<tmpList.size();i++){
            pointPaint.setColor(getResources().getColor(R.color.temp));
            outPointPaint.setColor(getResources().getColor(R.color.temp));
            if(i==0){
//                canvas.drawLine((2*i+1)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh,
//                        (2*i+3)*xSpace,(max-tmpList.get(i+1))*ySpace+offsetHigh, tmpPaint);
                path.moveTo((2*i+1)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh);
                path.quadTo((2*i+2)*xSpace,(max-tmpList.get(i+1))*ySpace+offsetHigh,
                        (2*i+3)*xSpace,(max-tmpList.get(i+1))*ySpace+offsetHigh);
            }else if(i<tmpList.size()-2){
                path.moveTo((2*i+1)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh);
                path.cubicTo((2*i+2)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh,
                        (2*i+2)*xSpace,(max-tmpList.get(i+1))*ySpace+offsetHigh,
                        (2*i+3)*xSpace,(max-tmpList.get(i+1))*ySpace+offsetHigh);
            }else if(i==tmpList.size()-2){
                path.moveTo((2*i+1)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh);
                path.quadTo((2*i+2)*xSpace,(max-tmpList.get(i+1))*ySpace+offsetHigh,
                        (2*i+3)*xSpace,(max-tmpList.get(i+1))*ySpace+offsetHigh);
            }
            canvas.drawPath(path,tmpPaint);
            canvas.drawCircle((2*i+1)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh,pointRadius,pointPaint);
            canvas.drawCircle((2*i+1)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh,outPointRadius,outPointPaint);
            canvas.drawText(tmpList.get(i)+"°",(2*i+1)*xSpace,(max-tmpList.get(i))*ySpace+offsetHigh-20,textPaint);
        }
        //日期
        for (int i=0;i<dateList.size();i++){
            String[] data1=dateList.get(i).split(" ");
            canvas.drawText(data1[1],(2*i+1)*xSpace,texthigh,textPaint);
        }
        //天气状况
        for (int i=0;i<txtList.size();i++){
            canvas.drawText(txtList.get(i),(2*i+1)*xSpace,viewhigh-(int)Math.ceil(fm.bottom - fm.leading),textPaint);
        }
        //降水概率
        for (int i=0;i<popList.size();i++){
            canvas.drawText(popList.get(i)+"%",(2*i+1)*xSpace,viewhigh-(int)Math.ceil(fm.bottom - fm.leading)-texthigh,textPaint);
        }
        //分割线
        for (int i=1;i<dateList.size();i++){
            canvas.drawLine(2*i*xSpace,50,2*i*xSpace,viewhigh-texthigh,textPaint);
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

}

