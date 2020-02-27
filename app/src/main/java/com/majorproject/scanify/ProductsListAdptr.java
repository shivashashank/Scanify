package com.majorproject.scanify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductsListAdptr extends ArrayAdapter<Product> {

    private Activity context;
    private List<Product> productList;


    public ProductsListAdptr(Activity context , List<Product> productList)
    {
        super(context, R.layout.list_view_all_products, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View productitem = inflater.inflate(R.layout.list_view_all_products, null,true);

        TextView name = (TextView) productitem.findViewById(R.id.name);
        TextView description = (TextView) productitem.findViewById(R.id.description);
        TextView price = (TextView) productitem.findViewById(R.id.price);
        TextView quantity = (TextView) productitem.findViewById(R.id.quantity);

        Product product =  productList.get(position);

        name.setText("Name : " + product.getPname());
        description.setText("Description : "+ product.getDescription());
        quantity.setText( "Quantity : " +product.getQuantity());
        price.setText( "Price : " + product.getAmount());

        return productitem;
    }
}
