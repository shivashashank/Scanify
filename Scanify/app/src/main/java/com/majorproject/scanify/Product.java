package com.majorproject.scanify;

public class Product {
    String Barcode;
    String Pname;
    String Description;
    String Amount;

    public Product()
    {

    }
    public void setBarcode(String Barcode)
    {
        this.Barcode=Barcode;
    }

    public void setPname(String Pname)
    {
        this.Pname=Pname;
    }
    public void setDescription(String Description)
    {
        this.Description=Description;
    }
    public void setAmount(String Amount)
    {
        this.Amount=Amount;
    }
    public String getBarcode()
    {
        return Barcode;
    }

    public String getPname()
    {
        return Pname;
    }
    public String getDescription()
    {
        return Description;
    }
    public String getAmount()
    {
        return Amount;
    }
}
