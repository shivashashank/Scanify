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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckoutListAdptr extends ArrayAdapter<Product>  {

    private Activity context;
    private List<Product> productList;
    private int dbquantity=1;
    private boolean found=false;
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
              final Product prod  =  productList.get(position);
              int quant = Integer.parseInt(quantity.getText().toString());
              FirebaseDatabase.getInstance().getReference().child("Product").child(prod.getBarcode()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Product p = dataSnapshot.getValue(Product.class);
                            dbquantity = Integer.parseInt(p.getQuantity());
                            found=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
              });
              if (++quant>dbquantity && found) {
                  --quant;
                  Toast.makeText(context, "Maximum stock limit reached", Toast.LENGTH_SHORT).show();
                  prod.setQuantity(String.valueOf(quant));
                  quantity.setText(String.valueOf(quant));
              }
              else {
                  found=false;
                  prod.setQuantity(String.valueOf(quant));
                  quantity.setText(String.valueOf(quant));
              }
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
                //Toast.makeText(context, prod.getQuantity().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return productitem;
    }
}