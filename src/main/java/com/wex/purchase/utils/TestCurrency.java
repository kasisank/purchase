package com.wex.purchase.utils;

import java.util.Currency;
import java.util.Locale;

public class TestCurrency {
    public static void main(String[] args) {
        String countryCode = "US";
        Locale locale = new Locale("", countryCode);

        String countryName = locale.getDisplayCountry();

        Currency currency = Currency.getInstance(locale);
        String currencySymbol = currency.getSymbol(locale);
        String currencyCode = currency.getCurrencyCode();

        System.out.println("Country: " + countryName);
        System.out.println("Currency Symbol: " + currencySymbol);
        System.out.println("Currency Code: " + currencyCode);
    }
}
