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


    public List<Integer> hourTmpList=new ArrayList<>();
    public List<Integer> hourPopList=new ArrayList<>();
    public List<String> hourTxtList=new ArrayList<>();
    public List<String> hourDirList=new ArrayList<>();
    public List<String> hourDateList=new ArrayList<>();

    public String airBrf;
    public String airTxt;
    public String comfBrf;
    public String comfTxt;
    public String cwBrf;
    public String cwTxt;
    public String drsgBrf;
    public String drsgTxt;
    public String fluBrf;
    public String fluTxt;
    public String sportBrf;
    public String sportTxt;
    public String travBrf;
    public String travTxt;
    public String uvBrf;
    public String uvTxt;




}
