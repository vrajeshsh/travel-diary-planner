package edu.vt.hotelSearch;

public class SearchedHotel {

  private String name;

  private String caption;

  private String redirtectPage;

  private String latitude;

  private String longitude;

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

  public SearchedHotel(String name, String caption, String redirtectPage, String latitude, String longitude) {
    this.name = name;
    this.caption = caption;
    this.redirtectPage = redirtectPage;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
