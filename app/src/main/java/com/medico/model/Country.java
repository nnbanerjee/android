package com.medico.model;

/**
 * Created by Narendra on 22-03-2017.
 */

public class Country
{
    public String iso2;

    public String capital;

    public String countryName;

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public Integer getIson() {
        return ison;
    }

    public void setIson(Integer ison) {
        this.ison = ison;
    }

    public Integer getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(Integer isdCode) {
        this.isdCode = isdCode;
    }

    public String currency;

    public String currencyCode;


    public String iso3;

    public Integer ison;

    public Integer isdCode;

    public String toString()
    {
        return isdCode.toString();
    }
}
