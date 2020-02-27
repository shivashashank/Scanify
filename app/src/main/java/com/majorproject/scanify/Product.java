package com.majorproject.scanify;

public class Product {
    String Barcode;
    String Pname;
    String Description;
    String Amount;
    String Quantity;

    public Product()
    {
    }
    public Product(String Barcode,String Pname,String Description,String Amount,String Quantity){
        this.Barcode=Barcode;
        this.Pname=Pname;
        this.Description=Description;
        this.Amount=Amount;
        this.Quantity=Quantity;
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
    public void setQuantity(String Quantity){
        this.Quantity=Quantity;
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
    public String getQuantity(){
        return Quantity;
    }
}
