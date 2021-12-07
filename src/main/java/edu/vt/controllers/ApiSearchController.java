/*
 * Created by Vrajesh Shah on 2021.10.23
 * Copyright © 2021 Vrajesh Shah. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static edu.vt.globals.Methods.readUrlContent;

@SessionScoped
@Named("apiSearchController")

public class ApiSearchController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    // Provided by the User
    private String locationQuery;
    private SearchedLocation selected;
    private List<SearchedLocation> listOfWeatherDetails;
    private List<SearchedWeather> listOfLocationWeather;
    private Integer forecastDays;

    public Integer getForecastDays() {
        return forecastDays;
    }

    public void setForecastDays(Integer forecastDays) {
        this.forecastDays = forecastDays;
    }

    public String getLocationQuery() {
        return locationQuery;
    }

    public void setLocationQuery(String locationQuery) {
        this.locationQuery = locationQuery;
    }

    public SearchedLocation getSelected() {
        return selected;
    }

    public void setSelected(SearchedLocation selected) {
        this.selected = selected;
    }

    public List<SearchedWeather> getListOfLocationWeather() {
        return listOfLocationWeather;
    }

    public void setListOfLocationWeather(List<SearchedWeather> listOfLocationWeather) {
        this.listOfLocationWeather = listOfLocationWeather;
    }

    public List<SearchedLocation> getListOfWeatherDetails() {
        return listOfWeatherDetails;
    }

    public void setListOfWeatherDetails(List<SearchedLocation> listOfWeatherDetails) {
        this.listOfWeatherDetails = listOfWeatherDetails;
    }

    /*
            ================
            Instance Methods
            ================
             */
    public String performSearch() {

        selected = null;

        // This sets the necessary flag to ensure the messages are preserved.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        listOfWeatherDetails = new ArrayList();
        listOfLocationWeather = new ArrayList();

        // Spaces in search query must be replaced with "+"
        locationQuery = locationQuery.replaceAll(" ", "+");


        try {
                    String weatherApiUrl = "https://api.weatherapi.com/v1/forecast.json?key=71d43d8a0f4e45c3a9a13750212411&q="+
                    locationQuery+"&days="+forecastDays;

            // Obtain the JSON file (String of characters) containing the search results
            // The readUrlContent() method is given below
            String searchResultsJsonData = readUrlContent(weatherApiUrl);
            ////////////////System.out.println("searchResultsJsonData : "+searchResultsJsonData);

        /*
        Redirecting to show a JSF page involves more than one subsequent requests and
        the messages would die from one request to another if not kept in the Flash scope.
        Since we will redirect to show the search Results page, we invoke preserveMessages().
         */
            JSONObject searchResultsJsonObject = new JSONObject(searchResultsJsonData);
            ////////////////System.out.println("searchResultsJsonObject"+searchResultsJsonObject);
            JSONObject jsonArrayFoundLocations = searchResultsJsonObject.getJSONObject("location");
            JSONObject jsonArrayFoundCurrent = searchResultsJsonObject.getJSONObject("current");
            JSONObject jsonArrayFoundCondition = jsonArrayFoundCurrent.getJSONObject("condition");
            ////////////////System.out.println("jsonArrayFoundCondition : "+jsonArrayFoundCondition);
            JSONObject jsonArrayFoundForecast= searchResultsJsonObject.getJSONObject("forecast");
            ////////////////System.out.println("jsonArrayFoundForecast : "+jsonArrayFoundForecast);
            JSONArray jsonArrayForecastDay= jsonArrayFoundForecast.getJSONArray("forecastday");
            ////////////////System.out.println("jsonArrayForecastDay : "+jsonArrayForecastDay);


            String name = jsonArrayFoundLocations.getString("name");
            String country = jsonArrayFoundLocations.getString("country");
            Double temp_c = jsonArrayFoundCurrent.getDouble("temp_c");
            Double temp_f = jsonArrayFoundCurrent.getDouble("temp_f");
            Double humidity = jsonArrayFoundCurrent.getDouble("humidity");
            Double wind_mph = jsonArrayFoundCurrent.getDouble("wind_mph");
            Double precip_mm = jsonArrayFoundCurrent.getDouble("precip_mm");
            String text = jsonArrayFoundCondition.getString("text");
            String localtime = jsonArrayFoundLocations.getString("localtime");
            String icon = jsonArrayFoundCondition.getString("icon");


            String cent = temp_c.toString();
            String far = temp_f.toString();
            String humid = humidity.toString();
            String wind = wind_mph.toString();
            String precip = precip_mm.toString();


            ////////////////System.out.println(name + country + cent + far + text + icon + humid + wind + precip + localtime);
            SearchedWeather weather = new SearchedWeather(name, country, cent, far, text, icon, humid, wind, precip, localtime);

            listOfLocationWeather.add(weather);
            int i = 0;
            while(i<= forecastDays) {
                ////////////////System.out.println("Inside");
                JSONObject jsonArrayDayDetails = jsonArrayForecastDay.getJSONObject(i);


                JSONObject jsonObjectDay = jsonArrayDayDetails.getJSONObject("day");
                //                ////////////////System.out.println("jsonObjectDay : " + jsonObjectDay);
                JSONObject jsonObjectCondition = jsonObjectDay.getJSONObject("condition");
                //                ////////////////System.out.println("jsonObjectCondition : "+jsonObjectCondition);
                JSONObject jsonObjectAstro = jsonArrayDayDetails.getJSONObject("astro");
                //                ////////////////System.out.println("jsonObjectAstro : "+jsonObjectAstro);


                // Forecast
                String date = jsonArrayDayDetails.getString("date");
                Double maxtemp_c = jsonObjectDay.getDouble("maxtemp_c");
                Double maxtemp_f = jsonObjectDay.getDouble("maxtemp_f");
                Double mintemp_c = jsonObjectDay.getDouble("mintemp_c");
                Double mintemp_f = jsonObjectDay.getDouble("mintemp_f");
                Double avghumidity = jsonObjectDay.getDouble("avghumidity");
                Double maxwind_mph = jsonObjectDay.getDouble("maxwind_mph");
                Double totalprecip_mm = jsonObjectDay.getDouble("totalprecip_mm");
                Integer daily_will_it_rain = jsonObjectDay.getInt("daily_will_it_rain");
                Integer daily_will_it_snow = jsonObjectDay.getInt("daily_will_it_snow");
                String foreIcon = jsonObjectCondition.getString("icon");
                String sunrise =jsonObjectAstro.getString("sunrise");
                String sunset = jsonObjectAstro.getString("sunset");
                String rain, snow;


                //////////////////System.out.println(name+country+temp_c+temp_f+text+icon+humidity+wind_mph+precip_mm+localtime);
                String max_c = maxtemp_c.toString();
                String max_f = maxtemp_f.toString();
                String min_c = mintemp_c.toString();
                String min_f = mintemp_f.toString();
                String avg_humid = avghumidity.toString();
                String max_wind = maxwind_mph.toString();
                String tot_precip = totalprecip_mm.toString();

                if (daily_will_it_rain == 1) {
                    rain = "Yes";

                } else if (daily_will_it_rain == 0) {
                    /* Round the calories value to 2 decimal places */
                    rain = "No";
                } else {
                    rain = "NA";
                }

                if (daily_will_it_snow == 1) {
                    snow = "Yes";

                } else if (daily_will_it_snow == 0) {
                    /* Round the calories value to 2 decimal places */
                    snow = "No";
                } else {
                    snow = "NA";
                }


                //////////////////System.out.println(max_c + max_f + min_c + min_f + avg_humid + max_wind + tot_precip);
                //////////////////System.out.println(name + country + cent + far + text + icon + humid + wind + precip + localtime + max_c + max_f + min_c + min_f + avg_humid + max_wind + tot_precip + rain + snow);
                SearchedLocation location = new SearchedLocation(date,max_c, max_f, min_c, min_f, avg_humid, max_wind, tot_precip, rain, snow, foreIcon, sunrise, sunset);

                // Add the newly created weather object to the list of searchedLocation
                listOfWeatherDetails.add(location);


                i++;

            }

        } catch (Exception ex) {
            //Methods.showMessage("Information", "No Results!", "No weather information found for the search location!");
            clear();
        }

        locationQuery = "";
        return "/search/ApiSearchResults?faces-redirect=true";
    }


    public void clear() {
        locationQuery = null;
    }

}
