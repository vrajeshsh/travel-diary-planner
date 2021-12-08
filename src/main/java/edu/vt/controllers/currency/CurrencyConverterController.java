/*
 * Created by Arpit Thool on 2021.11.30
 * Copyright © 2021 Arpit Thool. All rights reserved.
 */
package edu.vt.controllers.currency;

import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONObject;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Named("currencyConverterController")
@SessionScoped

public class CurrencyConverterController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    // Provided by the user
    private String currencyNameWithIdFrom;
    private String currencyNameWithIdTo;
    private Double amountToConvert;

    // Computed in this class
    private String conversionResult;

    // Used for processing
    private List<String> currencyNamesWithIds = new ArrayList<>();

    /*
    HashMap object stores Key-Value pairs, where the first entry is the key.
    Given the Key, the corresponding value is obtained with the GET method.
    The Key-Value pairs are stored in HashMap in no particular order.
     */
    HashMap<String, String> currencyName_currencyId = new HashMap<>();

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String getCurrencyNameWithIdFrom() {
        return currencyNameWithIdFrom;
    }

    public void setCurrencyNameWithIdFrom(String currencyNameWithIdFrom) {
        this.currencyNameWithIdFrom = currencyNameWithIdFrom;
    }

    public String getCurrencyNameWithIdTo() {
        return currencyNameWithIdTo;
    }

    public void setCurrencyNameWithIdTo(String currencyNameWithIdTo) {
        this.currencyNameWithIdTo = currencyNameWithIdTo;
    }

    public Double getAmountToConvert() {
        return amountToConvert;
    }

    public void setAmountToConvert(Double amountToConvert) {
        this.amountToConvert = amountToConvert;
    }

    public String getConversionResult() {
        return conversionResult;
    }

    public void setConversionResult(String conversionResult) {
        this.conversionResult = conversionResult;
    }

    public List<String> getCurrencyNamesWithIds() {
        return currencyNamesWithIds;
    }

    public void setCurrencyNamesWithIds(List<String> currencyNamesWithIds) {
        this.currencyNamesWithIds = currencyNamesWithIds;
    }

    /*
    ==================
    Constructor Method
    ==================
     */
    public CurrencyConverterController() {
        /*
        This constructor method is called first right after the object is
        instantiated from this class. The code included in the constructor
        method is intended to initialize and dress up the newly created object.
         */

        List<String> currencyNamesWithDuplicates = new ArrayList<>();

        /*
        JSON uses the following notation:
        { }    represents a JavaScript object as a Dictionary with Key:Value pairs
        [ ]    represents Array
        [{ }]  represents an Array of JavaScript objects (dictionaries)
          :    separates Key from the Value
         */
        String apiUrl = "https://free.currconv.com/api/v7/currencies?apiKey=" + Constants.FREE_CURRENCY_CONVERTER_API_KEY;

        try {
            // Obtain the JSON file from the apiUrl
            String jsonData = Methods.readUrlContent(apiUrl);

            /*
            The following JSON file is obtained:
            
            {                   <-- jsonObjectOfJsonData
                "results":      
                    {           <-- resultsJsonObject
                        "ALL":{"currencyName":"Albanian Lek","currencySymbol":"Lek","id":"ALL"},         <-- countryJsonObject
                        "XCD":{"currencyName":"East Caribbean Dollar","currencySymbol":"$","id":"XCD"},
                        "EUR":{"currencyName":"Euro","currencySymbol":"€","id":"EUR"},
                        "BBD":{"currencyName":"Barbadian Dollar","currencySymbol":"$","id":"BBD"},
                                :
                                :
                        "ZMK":{"currencyName":"Old Zambian Kwacha","id":"ZMK"},
                        "XAG":{"currencyName":"Silver (troy ounce)","id":"XAG"},
                        "ZWL":{"currencyName":"Zimbabwean Dollar","id":"ZWL"}
                    }
            }
             */
            // Convert the JSON data into a JSON object at the top level
            JSONObject jsonObjectOfJsonData = new JSONObject(jsonData);

            // Obtain the JSON object for the "results" key
            JSONObject resultsJsonObject = jsonObjectOfJsonData.getJSONObject("results");

            // keys() returns all of the keys (country codes) of the resultsJsonObject as an Iterator object
            Iterator<String> keysIterator = resultsJsonObject.keys();

            while (keysIterator.hasNext()) {
                // The key is the 3-letter country code
                String countryCode = keysIterator.next();

                // Get the JSON object for the country code key
                JSONObject countryJsonObject = resultsJsonObject.getJSONObject(countryCode);

                // Obtain currency id and name for that country
                String currencyId = countryJsonObject.getString("id");
                String currencyName = countryJsonObject.getString("currencyName");

                // Add currency name to the list, which will have duplicates
                currencyNamesWithDuplicates.add(currencyName);

                // Add the name and id into the hash map
                currencyName_currencyId.put(currencyName, currencyId);
            }

            /*
            Remove duplicates from the list with case sensitivity. This means that
            two names like 'European Euro' and 'European euro' will stay in the list.
             */
            List<String> currencyNamesCaseSensitive = currencyNamesWithDuplicates.stream().distinct().sorted().collect(Collectors.toList());

            // Sort the list in alphabetical order

            String nameOfCurrency;
            String currencyNameWithId;

            nameOfCurrency = currencyNamesCaseSensitive.get(0);
            currencyNameWithId = nameOfCurrency + " (" + currencyName_currencyId.get(nameOfCurrency) + ")";
            currencyNamesWithIds.add(currencyNameWithId);

            // Skip duplicate name such as 'European Euro' and 'European euro' or 'Swiss Franc' and 'Swiss franc'.
            for (int i = 1; i < currencyNamesCaseSensitive.size(); i++) {

                nameOfCurrency = currencyNamesCaseSensitive.get(i);

                // Skip the too-long currency name
                if (nameOfCurrency.equals("Bosnia and Herzegovina konvertibilna marka")) {
                    continue;
                }

                // i --> current,   i-1 --> previous
                // Convert the previous and current case sensitive values to lower case
                String previous = currencyNamesCaseSensitive.get(i - 1).toLowerCase();
                String current = nameOfCurrency.toLowerCase();

                if (!current.equals(previous)) {
                    currencyNameWithId = nameOfCurrency + " (" + currencyName_currencyId.get(nameOfCurrency) + ")";
                    currencyNamesWithIds.add(currencyNameWithId);
                }
            }

        } catch (Exception e) {
            Methods.showMessage("Fatal Error", "Initialization Failed!",
                    "The Currency Converter API is Unreachable!");
        }
    }

    /*
    ================
    Instance Methods
    ================
     */
    public void convertCurrency() {

        String[] nameId;

        /*
        Split "European Euro (EUR)" before " (" and after:
            nameId[0] = European Euro
            nameId[1] = EUR)
         */
        nameId = currencyNameWithIdFrom.split(" \\(");
        String nameOfCurrencyToConvertFrom = nameId[0];

        nameId = currencyNameWithIdTo.split(" \\(");
        String nameOfCurrencyToConvertTo = nameId[0];

        String currencyIdFrom = currencyName_currencyId.get(nameOfCurrencyToConvertFrom);
        String currencyIdTo = currencyName_currencyId.get(nameOfCurrencyToConvertTo);

        // conversionQuery = EUR_USD implies conversion from EUR to USD
        String conversionQuery = currencyIdFrom + "_" + currencyIdTo;

        // Example query: https://free.currencyconverterapi.com/api/v5/convert?q=EUR_USD&compact=ultra
        String apiUrl = "https://free.currencyconverterapi.com/api/v5/convert?q=" + conversionQuery +
                "&compact=ultra&apiKey=" + Constants.FREE_CURRENCY_CONVERTER_API_KEY;

        try {
            // Obtain the JSON file from the apiUrl
            String jsonData = Methods.readUrlContent(apiUrl);

            /*
            The following JSON file is obtained converting from EUR to USD:
                {"EUR_USD":1.165446}
             */
            // Convert the JSON data into a JSON object
            JSONObject jsonObject = new JSONObject(jsonData);

            Double conversionRate = jsonObject.optDouble(conversionQuery, 0.0);
            if (conversionRate.equals(0.0)) {
                conversionResult = "Currency API was unable to perform the conversion!";
            } else {
                Double result = amountToConvert * conversionRate;

                String conversionResultString = String.format("%.3f", result);

                conversionResult = amountToConvert + " " + currencyNameWithIdFrom + " = "
                        + conversionResultString + " " + currencyNameWithIdTo;
            }
        } catch (Exception e) {
            Methods.showMessage("Fatal Error", "Currency Conversion Failed!",
                    "Unable to convert currency with data obtained from the Currency API!");
        }
    }

    public void clear() {
        currencyNameWithIdFrom = "";
        currencyNameWithIdTo = "";
        amountToConvert = null;
        conversionResult = "";
    }

}
