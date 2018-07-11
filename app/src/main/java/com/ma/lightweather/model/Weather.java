package com.ma.lightweather.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/13.
 */
public class Weather implements Serializable{
    public String tmp;
    public String feel;
    public String hum;
    public String pcpn;
    public String city;
    public String cnty;
    public String dir;
    public String txt;
    public String pm;
    public String pres;
    public String vis;
    public List<Integer> maxList=new ArrayList<>();
    public List<Integer> minList=new ArrayList<>();
    public List<String> txtList=new ArrayList<>();
    public List<String> dirList=new ArrayList<>();
    public List<String> dateList=new ArrayList<>();


}
