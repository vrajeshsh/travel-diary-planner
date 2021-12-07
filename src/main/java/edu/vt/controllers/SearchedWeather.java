/*
 * Created by Vrajesh Shah on 2021.10.25
 * Copyright © 2021 Vrajesh Shah. All rights reserved.
 */
package edu.vt.controllers;

// class used for storing weather object during wetaher search
public class SearchedWeather {
    /*
    ==========================================================
    Instance variables representing the attributes of a weather.
    ==========================================================
     */

    private String date;
    private String name;
    private String country;
    private String temp_c;
    private String temp_f;
    private String text;
    private String icon;
    private String humidity;
    private String precip_mm;
    private String wind_mph;
    private String localtime;

    /*
     ========================================================
     Getter and Setter methods for the attributes of a weather.
     ========================================================
    */

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrecip_mm() {
        return precip_mm;
    }

    public void setPrecip_mm(String precip_mm) {
        this.precip_mm = precip_mm;
    }

    public String getWind_mph() {
        return wind_mph;
    }

    public void setWind_mph(String wind_mph) {
        this.wind_mph = wind_mph;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(String temp_c) {
        this.temp_c = temp_c;
    }

    public String getTemp_f() {
        return temp_f;
    }

    public void setTemp_f(String temp_f) {
        this.temp_f = temp_f;
    }

    /*
    ===================================================
    Class constructor for instantiating a SearchedWeather
    object to represent a particular weather.
    ===================================================
     */

    public SearchedWeather(String name, String country, String temp_c, String temp_f, String text, String icon, String humidity, String wind_mph, String precip_mm, String localtime) {

        this.name = name;
        this.country = country;
        this.temp_c = temp_c;
        this.temp_f = temp_f;
        this.text = text;
        this.icon = icon;
        this.humidity = humidity;
        this.wind_mph = wind_mph;
        this.precip_mm = precip_mm;
        this.localtime = localtime;

    }
}
