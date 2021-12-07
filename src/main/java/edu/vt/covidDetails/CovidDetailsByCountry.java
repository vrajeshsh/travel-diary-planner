package edu.vt.covidDetails;

import java.util.Date;

//{"ID":"b9560105-1f28-4052-9ab9-d127936dd7b3","Country":"South Africa","CountryCode":"ZA","Province":"","City":"","CityCode":"","Lat":"-30.56","Lon":"22.94","Confirmed":3038075,"Deaths":89975,"Recovered":0,"Active":2948100,"Date":"2021-12-07T00:00:00Z"}
public class CovidDetailsByCountry {
  String name;
  String code;

  Double lat;
  Double lon;

  Integer confirmed;
  Integer deaths;
  Integer recovered;
  Integer active;

  Date date;

  public CovidDetailsByCountry(String name, String code, Double lat, Double lon, Integer confirmed, Integer deaths, Integer recovered, Integer active, Date date) {
    this.name = name;
    this.code = code;
    this.lat = lat;
    this.lon = lon;
    this.confirmed = confirmed;
    this.deaths = deaths;
    this.recovered = recovered;
    this.active = active;
    this.date = date;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLon() {
    return lon;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public Integer getConfirmed() {
    return confirmed;
  }

  public void setConfirmed(Integer confirmed) {
    this.confirmed = confirmed;
  }

  public Integer getDeaths() {
    return deaths;
  }

  public void setDeaths(Integer deaths) {
    this.deaths = deaths;
  }

  public Integer getRecovered() {
    return recovered;
  }

  public void setRecovered(Integer recovered) {
    this.recovered = recovered;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public CovidDetailsByCountry() {
  }
}
