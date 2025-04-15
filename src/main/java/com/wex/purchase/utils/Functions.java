package com.wex.purchase.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.wex.purchase.utils.dataformat.Datum;
import com.wex.purchase.utils.dataformat.Root;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.stream;


public class Functions {
    private static final String EXCHANGE_URI ="https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
    private static String[] countryCodes = {"IN","MX","BE","BW","CA","CN","EG","DE","IL","HU","IT"};

    static Logger logger = LoggerFactory.getLogger(Functions.class);
    public static double roundToCents(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // Country code should be in "US","IN","MX" format
    public static double convertToLocalCurrency(double amount, Date purchaseDate,
                                                String countryCode) {

        // get the date from 6 months ago.
        Calendar now = Calendar.getInstance();
        now.setTime(purchaseDate);

        now.add(Calendar.MONTH, -6);

        // get the date from now

        Date previousDate = now.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(previousDate);


        /*
         https://api.fiscaldata.treasury.gov/services/api/
         fiscal_service/v1/accounting/od/rates_of_exchange?
         fields=country_currency_desc,exchange_rate,record_date&filter=country_currency_desc:in:(Canada-Dollar,Mexico-Peso),record_date:gte:2024-01-01
         */

        //Hack as we will always get US so get a random country code
        if (countryCode.equals("US")) {
            countryCode=getRandomCountryCode();
        }
        String getCurrencyForCountry= getCurrencyForCountry(countryCode);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(EXCHANGE_URI)
                .queryParam("fields", "country_currency_desc,exchange_rate,record_date")
                .queryParam("filter","country_currency_desc:in:("+getCurrencyForCountry+"),record_date:gte:"+formattedDate);

        RestTemplate restTemplate=new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(
                builder.build().encode().toUri(), String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        Root root =null;
        try {
            root = objectMapper.readValue(response.getBody(), new TypeReference<Root>() {
            });
        } catch (IOException e) {
           logger.error("Error parsing JSON", e);
        }

        if (root != null) {
            // get the data with the currencies
            // get the latest Data element
            if (root.data.toArray().length > 0) {
                Datum datum = root.data.get((root.data.toArray().length - 1));
                return roundToCents(Double.parseDouble(datum.exchange_rate) * amount);
            }
        }
        return 0.0;
    }

    private static String getRandomCountryCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1;
        return countryCodes[randomNumber];
    }

    public static String getCurrencyForCountry(String countryCode) {
        // need to build a map based on the country.

        Locale locale = new Locale("", countryCode);

        // TODO null check
        String countryName = locale.getDisplayCountry();

        Currency currency = Currency.getInstance(locale);

        List<String> displayName =Arrays.stream(currency.getDisplayName().split(" "))
                .toList();
        String currencyName;
        if (displayName.size() > 1) {
           currencyName=displayName.get(1);
        } else{
           currencyName=displayName.get(0);
        }

        return(countryName+"-"+currencyName);
    }

}
