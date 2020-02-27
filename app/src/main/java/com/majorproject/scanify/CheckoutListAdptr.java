package com.majorproject.scanify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutListAdptr extends ArrayAdapter<Product>  {

    private Activity context;
    private List<Product> productList;



    public CheckoutListAdptr(Activity context , List<Product> productList)
    {
        super(context, R.layout.list_view_checkout, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        final View productitem = inflater.inflate(R.layout.list_view_checkout, null,true);

        final  TextView name = (TextView) productitem.findViewById(R.id.textView17);
        final  TextView description = (TextView) productitem.findViewById(R.id.textView18);
        final TextView price = (TextView) productitem.findViewById(R.id.textView19);
        final  TextView quantity = (TextView) productitem.findViewById(R.id.textView20);

        Button plus = (Button) productitem.findViewById(R.id.plus);
        Button minus = (Button) productitem.findViewById(R.id.minus);



        Product product =  productList.get(position);

        name.setText(product.getPname());
        description.setText(product.getDescription());
        price.setText(  product.getAmount());
        if(product.getQuantity().equals("1"))
            quantity.setText("1");
        else
            quantity.setText(product.getQuantity());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Product prod  =  productList.get(position);
             int quant = Integer.parseInt(quantity.getText().toString());
              quant++;
              prod.setQuantity(String.valueOf(quant));
              quantity.setText(String.valueOf(quant));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product prod  =  productList.get(position);
                int quant = Integer.parseInt(quantity.getText().toString());
                if(quant>=2) {
                    quant--;
                    prod.setQuantity(String.valueOf(quant));
                    quantity.setText(String.valueOf(quant));
                }
                else{
                    Toast.makeText(context, "Quantity Cannot be Less than 1", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(context, prod.getQuantity().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return productitem;
    }


}
