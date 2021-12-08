/*
 * Created by Sai Venkat on 2021.11.25
 * Copyright © 2021 Sai Venkat Banda. All rights reserved.
 */
package edu.vt.covidDetails;

// country class for performing COVID search
public class Country {
  /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
  String name;
  String slug;
  String ISO2;

  //constructor
  public Country(String name, String slug, String ISO2) {
    this.name = name;
    this.slug = slug;
    this.ISO2 = ISO2;
  }

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
