package com.ma.lightweather.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma-PC on 2016/12/13.
 */
public class Weather implements Serializable{
    public String tmp;//温度
    public String feel;//体感
    public String hum;//相对湿度
    public String pcpn;//降水量
    public String city;//城市
    public String cnty;//国家
    public String dir;//风向
    public String txt;//天气
    public String wind;//风速
    public String pres;//气压
    public String vis;//能见度
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

    public List<String> lifeTypeList=new ArrayList<>();
    public List<String> lifeTxtList=new ArrayList<>();
    public List<String> lifeBrfList=new ArrayList<>();


    private List<WeatherBean> HeWeather6;

    public List<WeatherBean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<WeatherBean> heWeather6) {
        HeWeather6 = heWeather6;
    }

    class WeatherBean implements Serializable{
        private NowWeather now;
        private BasicWeather basic;
        private List<DailyWeather> daily_forecast;
        private List<HourlyWeather> hourly;
        private List<LifeWeather> lifestyle;

        public NowWeather getNow() {
            return now;
        }

        public void setNow(NowWeather now) {
            this.now = now;
        }

        public BasicWeather getBasic() {
            return basic;
        }

        public void setBasic(BasicWeather basic) {
            this.basic = basic;
        }

        public List<DailyWeather> getDaily_forecast() {
            return daily_forecast;
        }

        public void setDaily_forecast(List<DailyWeather> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public List<HourlyWeather> getHourly() {
            return hourly;
        }

        public void setHourly(List<HourlyWeather> hourly) {
            this.hourly = hourly;
        }

        public List<LifeWeather> getLifestyle() {
            return lifestyle;
        }

        public void setLifestyle(List<LifeWeather> lifestyle) {
            this.lifestyle = lifestyle;
        }
    }
    class NowWeather implements Serializable{
        private String cond_code;
        private String cond_txt;
        private String fl;
        private String hum;
        private String pcpn;
        private String pres;
        private String tmp;
        private String vis;
        private String wind_deg;
        private String wind_dir;
        private String wind_sc;
        private String wind_spd;

        public String getCond_code() {
            return cond_code;
        }

        public void setCond_code(String cond_code) {
            this.cond_code = cond_code;
        }

        public String getCond_txt() {
            return cond_txt;
        }

        public void setCond_txt(String cond_txt) {
            this.cond_txt = cond_txt;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public String getWind_deg() {
            return wind_deg;
        }

        public void setWind_deg(String wind_deg) {
            this.wind_deg = wind_deg;
        }

        public String getWind_dir() {
            return wind_dir;
        }

        public void setWind_dir(String wind_dir) {
            this.wind_dir = wind_dir;
        }

        public String getWind_sc() {
            return wind_sc;
        }

        public void setWind_sc(String wind_sc) {
            this.wind_sc = wind_sc;
        }

        public String getWind_spd() {
            return wind_spd;
        }

        public void setWind_spd(String wind_spd) {
            this.wind_spd = wind_spd;
        }
    }
    class BasicWeather implements Serializable{
        private String cid;
        private String location;
        private String parent_city;
        private String admin_area;
        private String cnty;
        private String lat;
        private String lon;
        private String tz;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getParent_city() {
            return parent_city;
        }

        public void setParent_city(String parent_city) {
            this.parent_city = parent_city;
        }

        public String getAdmin_area() {
            return admin_area;
        }

        public void setAdmin_area(String admin_area) {
            this.admin_area = admin_area;
        }

        public String getCnty() {
            return cnty;
        }

        public void setCnty(String cnty) {
            this.cnty = cnty;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getTz() {
            return tz;
        }

        public void setTz(String tz) {
            this.tz = tz;
        }
    }
    class DailyWeather implements Serializable{
        private String cond_code_d;
        private String cond_code_n;
        private String cond_txt_d;
        private String cond_txt_n;
        private String date;
        private String hum;
        private String pcpn;
        private String pop;
        private String pres;
        private String tmp_max;
        private String tmp_min;
        private String uv_index;
        private String vis;
        private String wind_deg;
        private String wind_dir;
        private String wind_sc;
        private String wind_spd;

        public String getCond_code_d() {
            return cond_code_d;
        }

        public void setCond_code_d(String cond_code_d) {
            this.cond_code_d = cond_code_d;
        }

        public String getCond_code_n() {
            return cond_code_n;
        }

        public void setCond_code_n(String cond_code_n) {
            this.cond_code_n = cond_code_n;
        }

        public String getCond_txt_d() {
            return cond_txt_d;
        }

        public void setCond_txt_d(String cond_txt_d) {
            this.cond_txt_d = cond_txt_d;
        }

        public String getCond_txt_n() {
            return cond_txt_n;
        }

        public void setCond_txt_n(String cond_txt_n) {
            this.cond_txt_n = cond_txt_n;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTmp_max() {
            return tmp_max;
        }

        public void setTmp_max(String tmp_max) {
            this.tmp_max = tmp_max;
        }

        public String getTmp_min() {
            return tmp_min;
        }

        public void setTmp_min(String tmp_min) {
            this.tmp_min = tmp_min;
        }

        public String getUv_index() {
            return uv_index;
        }

        public void setUv_index(String uv_index) {
            this.uv_index = uv_index;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public String getWind_deg() {
            return wind_deg;
        }

        public void setWind_deg(String wind_deg) {
            this.wind_deg = wind_deg;
        }

        public String getWind_dir() {
            return wind_dir;
        }

        public void setWind_dir(String wind_dir) {
            this.wind_dir = wind_dir;
        }

        public String getWind_sc() {
            return wind_sc;
        }

        public void setWind_sc(String wind_sc) {
            this.wind_sc = wind_sc;
        }

        public String getWind_spd() {
            return wind_spd;
        }

        public void setWind_spd(String wind_spd) {
            this.wind_spd = wind_spd;
        }
    }
    class HourlyWeather implements Serializable{
        private String cloud;
        private String cond_code;
        private String cond_txt;
        private String hum;
        private String pop;
        private String pres;
        private String time;
        private String tmp;
        private String wind_deg;
        private String wind_dir;
        private String wind_sc;
        private String wind_spd;

        public String getCloud() {
            return cloud;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }

        public String getCond_code() {
            return cond_code;
        }

        public void setCond_code(String cond_code) {
            this.cond_code = cond_code;
        }

        public String getCond_txt() {
            return cond_txt;
        }

        public void setCond_txt(String cond_txt) {
            this.cond_txt = cond_txt;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public String getWind_deg() {
            return wind_deg;
        }

        public void setWind_deg(String wind_deg) {
            this.wind_deg = wind_deg;
        }

        public String getWind_dir() {
            return wind_dir;
        }

        public void setWind_dir(String wind_dir) {
            this.wind_dir = wind_dir;
        }

        public String getWind_sc() {
            return wind_sc;
        }

        public void setWind_sc(String wind_sc) {
            this.wind_sc = wind_sc;
        }

        public String getWind_spd() {
            return wind_spd;
        }

        public void setWind_spd(String wind_spd) {
            this.wind_spd = wind_spd;
        }
    }
    class LifeWeather implements Serializable{
        private String brf;
        private String txt;
        private String type;

        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
