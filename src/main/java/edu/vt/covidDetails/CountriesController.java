package edu.vt.covidDetails;


import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.json.JsonArray;
import java.io.Serializable;
import java.util.*;

@Named("countriesController")
@SessionScoped
public class CountriesController implements Serializable {
  List<Country> countryList;
  List<String> COUNTRY_PARAMETERS = Arrays.asList("Country", "Slug", "ISO2");
  Map<String, String> countryNameSlugMap;
  String[] countryNames;

  /*
     ---------------------------------------------------------------------------------------------------------
     "The PostConstruct annotation is used on a method that needs to be executed after dependency injection
     is done to perform any initialization. This method MUST be invoked before the class is put into service."
     See for further info: https://docs.oracle.com/javaee/7/api/javax/annotation/PostConstruct.html
     ---------------------------------------------------------------------------------------------------------
     */
  @PostConstruct
  public void init() {
    // gets all countries data
    obtainCountriesPrimaryData();

  }

  public void obtainCountriesPrimaryData() {
    Methods.preserveMessages();

    String apiUrl = "https://api.covid19api.com/countries";

    countryList = new ArrayList<>();
    countryNameSlugMap = new HashMap<>();
    try{
      String jsonData = Methods.readUrlContent(apiUrl);
      JSONArray countriesData = new JSONArray(jsonData);
      for(int index = 0; index <countriesData.length(); index++) {
        JSONObject countryData = countriesData.getJSONObject(index);
    //    {
    //        "Country": "Algeria",
    //        "Slug": "algeria",
    //        "ISO2": "DZ"
    //    },

        String name = countryData.optString(COUNTRY_PARAMETERS.get(0), "");
        if(name.equals("")) {
          continue;
        }

        String slug = countryData.optString(COUNTRY_PARAMETERS.get(1), "");
        String iso2 = countryData.optString(COUNTRY_PARAMETERS.get(2), "");

        Country country = new Country(name, slug, iso2);

        countryList.add(country);
        countryNameSlugMap.put(name, slug);
      }
      countryNames = countryNameSlugMap.keySet().toArray(String[]::new);
      Arrays.sort(countryNames);
    } catch (Exception e) {
      Methods.showMessage("Error", "Get Country Details Failed!",
              "See: " + e.getMessage());
    }
  }

  public String[] getListOfCountries() {
    return countryNames;
  }
}
