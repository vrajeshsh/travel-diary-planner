/*
 * Created by Vrajesh Shah on 2021.10.25
 * Copyright © 2021 Vrajesh Shah. All rights reserved.
 */
package edu.vt.controllers.weatherSearch;

public class SearchedLocation {
    /*
    ==========================================================
    Instance variables representing the attributes of a weather.
    ==========================================================
     */

    private String date;
    private String maxtemp_c;
    private String maxtemp_f;
    private String mintemp_c;
    private String mintemp_f;
    private String avghumidity;
    private String maxwind_mph;
    private String totalprecip_mm;
    private String daily_will_it_rain;
    private String daily_will_it_snow;
    private String foreIcon;
    private String sunrise;
    private String sunset;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getForeIcon() {
        return foreIcon;
    }

    public void setForeIcon(String foreIcon) {
        this.foreIcon = foreIcon;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getMaxtemp_c() {
        return maxtemp_c;
    }

    public void setMaxtemp_c(String maxtemp_c) {
        this.maxtemp_c = maxtemp_c;
    }

    public String getMaxtemp_f() {
        return maxtemp_f;
    }

    public void setMaxtemp_f(String maxtemp_f) {
        this.maxtemp_f = maxtemp_f;
    }

    public String getMintemp_c() {
        return mintemp_c;
    }

    public void setMintemp_c(String mintemp_c) {
        this.mintemp_c = mintemp_c;
    }

    public String getMintemp_f() {
        return mintemp_f;
    }

    public void setMintemp_f(String mintemp_f) {
        this.mintemp_f = mintemp_f;
    }

    public String getAvghumidity() {
        return avghumidity;
    }

    public void setAvghumidity(String avghumidity) {
        this.avghumidity = avghumidity;
    }

    public String getMaxwind_mph() {
        return maxwind_mph;
    }

    public void setMaxwind_mph(String maxwind_mph) {
        this.maxwind_mph = maxwind_mph;
    }

    public String getTotalprecip_mm() {
        return totalprecip_mm;
    }

    public void setTotalprecip_mm(String totalprecip_mm) {
        this.totalprecip_mm = totalprecip_mm;
    }

    public String getDaily_will_it_rain() {
        return daily_will_it_rain;
    }

    public void setDaily_will_it_rain(String daily_will_it_rain) {
        this.daily_will_it_rain = daily_will_it_rain;
    }

    public String getDaily_will_it_snow() {
        return daily_will_it_snow;
    }

    public void setDaily_will_it_snow(String daily_will_it_snow) {
        this.daily_will_it_snow = daily_will_it_snow;
    }

    //constructor
    public SearchedLocation(String date,String maxtemp_c, String maxtemp_f, String mintemp_c, String mintemp_f, String avghumidity, String totalprecip_mm, String maxwind_mph, String daily_will_it_rain, String daily_will_it_snow, String foreIcon, String sunrise, String sunset) {
        this.date = date;
        this.maxtemp_c = maxtemp_c;
        this.maxtemp_f = maxtemp_f;
        this.mintemp_c = mintemp_c;
        this.mintemp_f = mintemp_f;
        this.avghumidity = avghumidity;
        this.daily_will_it_rain = daily_will_it_rain;
        this.daily_will_it_snow = daily_will_it_snow;
        this.maxwind_mph = maxwind_mph;
        this.totalprecip_mm = totalprecip_mm;
        this.foreIcon = foreIcon;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
}
