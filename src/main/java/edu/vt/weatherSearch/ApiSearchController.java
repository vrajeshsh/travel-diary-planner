/*
 * Created by Vrajesh Shah on 2021.10.23
 * Copyright © 2021 Vrajesh Shah. All rights reserved.
 */
package edu.vt.weatherSearch;

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

    /*
    =========================
    Getter and Setter Methods
    =========================
     */

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

            /*
            Redirecting to show a JSF page involves more than one subsequent requests and
            the messages would die from one request to another if not kept in the Flash scope.
            Since we will redirect to show the search Results page, we invoke preserveMessages().
             */
            JSONObject searchResultsJsonObject = new JSONObject(searchResultsJsonData);
            JSONObject jsonArrayFoundLocations = searchResultsJsonObject.getJSONObject("location");
            JSONObject jsonArrayFoundCurrent = searchResultsJsonObject.getJSONObject("current");
            JSONObject jsonArrayFoundCondition = jsonArrayFoundCurrent.getJSONObject("condition");
            JSONObject jsonArrayFoundForecast= searchResultsJsonObject.getJSONObject("forecast");
            JSONArray jsonArrayForecastDay= jsonArrayFoundForecast.getJSONArray("forecastday");

            // extract the data we want from the fetched JSON object
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

            // create the SearchedWeather object from the data
            SearchedWeather weather = new SearchedWeather(name, country, cent, far, text, icon, humid, wind, precip, localtime);

            // add the object to the list
            listOfLocationWeather.add(weather);

            int i = 0;
            while(i<= forecastDays) {
                // extract the data from the JSON object
                JSONObject jsonArrayDayDetails = jsonArrayForecastDay.getJSONObject(i);
                JSONObject jsonObjectDay = jsonArrayDayDetails.getJSONObject("day");
                JSONObject jsonObjectCondition = jsonObjectDay.getJSONObject("condition");
                JSONObject jsonObjectAstro = jsonArrayDayDetails.getJSONObject("astro");

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

                String max_c = maxtemp_c.toString();
                String max_f = maxtemp_f.toString();
                String min_c = mintemp_c.toString();
                String min_f = mintemp_f.toString();
                String avg_humid = avghumidity.toString();
                String max_wind = maxwind_mph.toString();
                String tot_precip = totalprecip_mm.toString();

                // process the various parameters to get meaningful result
                if (daily_will_it_rain == 1) {
                    rain = "Yes";
                } else if (daily_will_it_rain == 0) {
                    rain = "No";
                } else {
                    rain = "NA";
                }

                if (daily_will_it_snow == 1) {
                    snow = "Yes";
                } else if (daily_will_it_snow == 0) {
                    snow = "No";
                } else {
                    snow = "NA";
                }

                // create the SearchedLocation object from the data
                SearchedLocation location = new SearchedLocation(date,max_c, max_f, min_c, min_f, avg_humid, max_wind, tot_precip, rain, snow, foreIcon, sunrise, sunset);

                // Add the newly created weather object to the list of searchedLocation
                listOfWeatherDetails.add(location);

                i++;
            }

        } catch (Exception ex) {
            // print exception
            System.out.println("Exception :"+ex);
        }

        // clear the search parameters
        this.clearLocationQuery();

        // return the redirect the string
        return "/search/ApiSearchResults?faces-redirect=true";
    }

    // this method clears the search parameters given by the user
    public void clearLocationQuery() {
        locationQuery = "";
        forecastDays = null;
    }

}
