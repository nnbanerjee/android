package com.medico.model;

import java.math.BigDecimal;

/**
 * Created by Narendra on 07-02-2017.
 */

public class Medicine
{
    public Integer idMedicine;
    public String name;
    public Byte category;
    public Byte subCategory;
    public String SKU;
    public String description;
    public Byte OTC;
    public Byte  size;
    public String unit;
    public BigDecimal price;
    public BigDecimal mrp;
    public String url;
    public String brandName;
    public Byte generic;
    public Integer genericId;
    public String country;
    public String isoCountry;
    public Integer type;

    @Override
    public String toString()
    {
        return name;
    }
}
