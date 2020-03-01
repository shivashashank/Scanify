package com.majorproject.scanify;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    String Barcode;
    String Pname;
    String Description;
    String Amount;
    String Quantity;

    public Product()
    {
    }
    protected Product(Parcel in) {
        // put your data using = in.readString();
        this.Barcode = in.readString();;
        this.Pname = in.readString();;
        this.Description = in.readString();;
        this.Amount=in.readString();
        this.Quantity=in.readString();
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
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Barcode);
        dest.writeString(Pname);
        dest.writeString(Description);
        dest.writeString(Amount);
        dest.writeString(Quantity);
    }
}
