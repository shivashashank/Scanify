package com.majorproject.scanify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class Admincheckout2Activity extends AppCompatActivity {

    TextView total;
    Button pay;
    int number;
    ArrayList<Product> productArrayList=null;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincheckout2);

        total = findViewById(R.id.totalamount);

        Intent intent = getIntent();
        number = intent.getIntExtra(AdmincheckoutActivity.EXTRA_NUMBER,0);
        productArrayList=intent.getExtras().getParcelableArrayList(AdmincheckoutActivity.EXTRA_LIST);
        total.setText("Total Amount: "+number);

        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase();
            }
        });
    }


    private void updateDatabase(){
        reference=FirebaseDatabase.getInstance().getReference().child("Product");
        for(final Product productlist:productArrayList){
            reference.child(productlist.getBarcode()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product dbproduct=dataSnapshot.getValue(Product.class);
                    int unused_quantity=Integer.parseInt(dbproduct.getQuantity());
                    int used_quantity=Integer.parseInt(productlist.getQuantity());
                    int updated_quantity=unused_quantity-used_quantity;
                    if(updated_quantity>=0){
                        dataSnapshot.getRef().child("quantity").setValue(""+(unused_quantity-used_quantity));
                        Toast.makeText(Admincheckout2Activity.this, "Quantity Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Admincheckout2Activity.this,AdmincheckoutActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, AdmincheckoutActivity.class));
    }
}