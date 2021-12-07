package edu.vt.covidDetails;

import edu.vt.globals.Methods;
import org.primefaces.model.chart.*;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Named("covidDetailsByCountryController")
@SessionScoped
public class CovidDetailsByCountryController implements Serializable {

  private BarChartModel covidChart;
  private List<CovidDetailsByCountry> covidDetailsByCountrywithDateList;

  private String countryName = "united-states";
  private String status = "New Cases";

  private Date fromDate = Date.from(OffsetDateTime.now().minus(Period.ofWeeks(5)).toInstant());
  private Date toDate = Date.from(OffsetDateTime.now().minus(Period.ofDays(1)).toInstant());

//  private OffsetDateTime fromDate = Date.from(OffsetDateTime.now().minus(Period.ofWeeks(5)).toInstant());
//  private OffsetDateTime toDate = OffsetDateTime.now().minus(Period.ofDays(1));


  /*
     ---------------------------------------------------------------------------------------------------------
     "The PostConstruct annotation is used on a method that needs to be executed after dependency injection
     is done to perform any initialization. This method MUST be invoked before the class is put into service."
     See for further info: https://docs.oracle.com/javaee/7/api/javax/annotation/PostConstruct.html
     ---------------------------------------------------------------------------------------------------------
     */
  @PostConstruct
  public void init() {
    // Display default covid chart
    obtainCountryCovidData();
    createCountryCovidModel();
  }

  public BarChartModel getCovidChart() {
    return covidChart;
  }

  public void setCovidChart(BarChartModel covidChart) {
    this.covidChart = covidChart;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public void obtainCountryCovidData() {
    Methods.preserveMessages();
//    https://api.covid19api.com/total/country/united-states?from=2021-10-01T00:00:00Z&to=2021-12-05T00:00:00Z
    String apiUrl = "https://api.covid19api.com/total/country/" + countryName + "?from=" + OffsetDateTime.ofInstant(fromDate.toInstant(), ZoneId.of("US/Eastern")) + "&to=" + OffsetDateTime.ofInstant(toDate.toInstant(), ZoneId.of("US/Eastern"));

    try {
      String jsonData = Methods.readUrlContent(apiUrl);
      JSONArray covidDataByCountry = new JSONArray(jsonData);

      covidDetailsByCountrywithDateList = new ArrayList<>();
      for(int index = 0; index < covidDataByCountry.length(); index += 1) {
        JSONObject covidDataByCountryByDate = covidDataByCountry.getJSONObject(index);
//        {
        //  "ID": "b9560105-1f28-4052-9ab9-d127936dd7b3",
        //  "Country": "South Africa",
        //  "CountryCode": "ZA",
        //  "Province": "",
        //  "City": "",
        //  "CityCode": "",
        //  "Lat": "-30.56",
        //  "Lon": "22.94",
        //  "Confirmed": 3038075,
        //  "Deaths": 89975,
        //  "Recovered": 0,
        //  "Active": 2948100,
        //  "Date": "2021-12-07T00:00:00Z"
        //}

        String name = covidDataByCountryByDate.optString("Country", "");


        String date = covidDataByCountryByDate.optString("Date", "");

        if(name.equals("") || date.equals("")) {
          continue;
        }
        CovidDetailsByCountry covidDetailsByCountry = new CovidDetailsByCountry();
        covidDetailsByCountry.setName(name);
        OffsetDateTime odt = OffsetDateTime.parse(date);
        Instant instant = odt.toInstant();

        covidDetailsByCountry.setDate(Date.from(instant));

        covidDetailsByCountry.setCode(covidDataByCountryByDate.optString("CountryCode", ""));

        covidDetailsByCountry.setLat(covidDataByCountryByDate.optDouble("Lat", 0.0));

        covidDetailsByCountry.setLon(covidDataByCountryByDate.optDouble("Lon", 0.0));

        covidDetailsByCountry.setConfirmed(covidDataByCountryByDate.optInt("Confirmed", 0));

        covidDetailsByCountry.setDeaths(covidDataByCountryByDate.optInt("Deaths", 0));

        covidDetailsByCountry.setRecovered(covidDataByCountryByDate.optInt("Recovered", 0));

        covidDetailsByCountry.setActive(covidDataByCountryByDate.optInt("Active", 0));

        covidDetailsByCountrywithDateList.add(covidDetailsByCountry);
      }
    } catch (Exception ex) {
      // Reset the chart to display default country united-states
      countryName = "united-states";
      displayCovidHistoryChart();

      Methods.showMessage("Error", "Unrecognized country!!",
              "Please select a valid country");
    }
  }

  /*
    ================
    Instance Method
    ================
     */
  public String displayCovidHistoryChart() {
    obtainCountryCovidData();
    createCountryCovidModel();
    return "/covid/HistoryChart?faces-redirect=true";
  }

  public List<CovidDetailsByCountry> getCovidDetailsByCountrywithDateList() {
    return covidDetailsByCountrywithDateList;
  }

  public void setCovidDetailsByCountrywithDateList(List<CovidDetailsByCountry> covidDetailsByCountrywithDateList) {
    this.covidDetailsByCountrywithDateList = covidDetailsByCountrywithDateList;
  }

  /*
  ================
  Instance Method
  ================
   */
  private void createCountryCovidModel() {
    covidChart = new BarChartModel();

    ChartSeries series = new ChartSeries();

    series.setLabel(countryName);

    int maxRecord = -99999999;
    int minRecord = 0;

    for(int index = 1; index < covidDetailsByCountrywithDateList.size(); index += 1) {
      if(status.equals("New Cases")) {
        series.set(
                covidDetailsByCountrywithDateList.get(index).date.toString(),
                covidDetailsByCountrywithDateList.get(index).confirmed - covidDetailsByCountrywithDateList.get(index - 1).confirmed
        );
        maxRecord = max(maxRecord, covidDetailsByCountrywithDateList.get(index).confirmed - covidDetailsByCountrywithDateList.get(index - 1).confirmed);
      } else if(status.equals("Deaths")) {
        series.set(
                covidDetailsByCountrywithDateList.get(index).date,
                covidDetailsByCountrywithDateList.get(index).deaths - covidDetailsByCountrywithDateList.get(index - 1).deaths
        );
        maxRecord = max(maxRecord, covidDetailsByCountrywithDateList.get(index).deaths - covidDetailsByCountrywithDateList.get(index - 1).deaths);
      }

    }

    covidChart.addSeries(series);
    covidChart.setLegendPosition("ne");

    Axis xAxis = covidChart.getAxis(AxisType.X);
    xAxis.setLabel("Dates");

    Axis yAxis = covidChart.getAxis(AxisType.Y);
    yAxis.setLabel(status);
    yAxis.setMax(maxRecord + maxRecord/10);
    yAxis.setMin(minRecord);

    covidChart.setExtender("barChartExtender");
  }

  public void clear() {
    countryName = "";
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }
}
