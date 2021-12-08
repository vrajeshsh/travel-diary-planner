/*
 * Created by Sai Venkat on 2021.10.25
 * Copyright © 2021 Sai Venkat Banda. All rights reserved.
 */
package edu.vt.hotelSearch;

// this class is used to create object to store the result from Hotel API search.
public class SearchedHotel {

  /*
    ==========================================================
    Instance variables representing the attributes of a weather.
    ==========================================================
     */
  private String name;

  private String caption;

  private String redirtectPage;

  private String latitude;

  private String longitude;

  /*
    =========================
    Getter and Setter Methods
    =========================
     */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getRedirtectPage() {
    return redirtectPage;
  }

  public void setRedirtectPage(String redirtectPage) {
    this.redirtectPage = redirtectPage;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  //constructor
  public SearchedHotel(String name, String caption, String redirtectPage, String latitude, String longitude) {
    this.name = name;
    this.caption = caption;
    this.redirtectPage = redirtectPage;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
