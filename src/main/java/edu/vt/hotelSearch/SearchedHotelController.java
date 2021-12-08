/*
 * Created by Sai Venkat on 2021.10.25
 * Copyright © 2021 Sai Venkat Banda. All rights reserved.
 */
package edu.vt.hotelSearch;

import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("searchedHotelController")
@SessionScoped
public class SearchedHotelController implements Serializable {
  private String searchQuery;

  private List<SearchedHotel> listOfSearchedHotels;

  private SearchedHotel selected;

  /*
    ================
    Instance Methods
    ================

    ---------------------------------------------------------
    Search for hotel for the searchQuery entered by the user
    ---------------------------------------------------------
     */
  public String performSearch() {
    listOfSearchedHotels = new ArrayList<>();

    try {

      // Spaces in search query must be replaced with "+"
      searchQuery = searchQuery.replaceAll(" ", "+");

      String searchHotelsUrl = "https://hotels4.p.rapidapi.com/locations/v2/search?query="+ searchQuery + "&locale=en_US&currency=USD";

      String searchResultsData = Methods.readUrlContent2(searchHotelsUrl);

      // Instantiate a JSON object from the JSON data obtained
      JSONObject resultsJsonObject = new JSONObject(searchResultsData);

      JSONArray jsonArrayFoundHotelSuggessions = resultsJsonObject.getJSONArray("suggestions");

      int index = 0;
      while (jsonArrayFoundHotelSuggessions.length() > index) {

        // Get the hotel JSONObject at index
        JSONObject foundHotelSuggessions = jsonArrayFoundHotelSuggessions.getJSONObject(index);

        String groupName = foundHotelSuggessions.optString("group", "");

        if(groupName.equals("HOTEL_GROUP")) {
          JSONArray jsonArrayEntities = foundHotelSuggessions.getJSONArray("entities");

          //lat//long//name //caption
          int jsonArrayEntitiesIndex = 0;
          while(jsonArrayEntities.length() > jsonArrayEntitiesIndex) {
            JSONObject jsonObject = jsonArrayEntities.getJSONObject(jsonArrayEntitiesIndex);

            Double latitude = jsonObject.optDouble("latitude", 0.0);
            String latitudeSring = latitude.toString();
            if(latitude == 0.0) {
              latitudeSring = "latitude not mentioned";
            }

            Double longitude = jsonObject.optDouble("longitude", 0.0);
            String longitudeSring = longitude.toString();
            if(latitude == 0.0) {
              longitudeSring = "longitude not mentioned";
            }

            String caption = jsonObject.optString("caption", "");
            if(caption.equals("")) {
              caption = "caption not mentioned";
            }

            String name = jsonObject.optString("name", "");
            if(name.equals("")) {
              name = "name not mentioned";
            }

            String redirectPage = jsonObject.optString("redirectPage", "");
            if(redirectPage.equals("")) {
              redirectPage = "redirect page not mentioned";
            }

            SearchedHotel hotel = new SearchedHotel(name, caption, redirectPage, latitudeSring, longitudeSring);

            listOfSearchedHotels.add(hotel);
            jsonArrayEntitiesIndex += 1;
          }
        }

        index += 1;
      }

    } catch (Exception ex) {
      Methods.showMessage("Issue with the server", "Please try again later",
              "See: " + ex.getMessage());
      searchQuery = "";
      return "";
    }

    searchQuery = "";
    return "/hotelSearch/searchResults?faces-redirect=true";
  }

  public String getSearchQuery() {
    return searchQuery;
  }

  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }

  public List<SearchedHotel> getListOfSearchedHotels() {
    return listOfSearchedHotels;
  }

  public void setListOfSearchedHotels(List<SearchedHotel> listOfSearchedHotels) {
    this.listOfSearchedHotels = listOfSearchedHotels;
  }

  public SearchedHotel getSelected() {
    return selected;
  }

  public void setSelected(SearchedHotel selected) {
    this.selected = selected;
  }
}
