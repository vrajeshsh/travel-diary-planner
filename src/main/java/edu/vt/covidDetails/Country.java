package edu.vt.covidDetails;

public class Country {
  String name;
  String slug;
  String ISO2;

  public Country(String name, String slug, String ISO2) {
    this.name = name;
    this.slug = slug;
    this.ISO2 = ISO2;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getISO2() {
    return ISO2;
  }

  public void setISO2(String ISO2) {
    this.ISO2 = ISO2;
  }
}
