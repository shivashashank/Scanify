package com.majorproject.scanify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class UserActivity extends AppCompatActivity {
    public static final String EXTRA_NUMBER = "com.majorproject.scanify.EXTRA_NUMBER";
    public static final String EXTRA_LIST = "com.majorproject.scanify.EXTRA_LIST";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button add,done;
    TextView total;
    private DatabaseReference Post;
    ArrayList<Product> productList;
    ValueEventListener valueEventListener;
    ListView listViewAllProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        total = findViewById(R.id.total);
        done = findViewById(R.id.done);

        productList=new ArrayList<>();
        listViewAllProducts = (ListView) findViewById(R.id.listViewAllProducts);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        Post = FirebaseDatabase.getInstance().getReference().child("Product");


        add = (Button) findViewById(R.id.add);
        final Activity activity = this;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(true);
                    //integrator.setOrientationLocked(false);
                    integrator.initiateScan();
                }
                catch(Exception e){
                    Toast.makeText(UserActivity.this,"Exception: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        //checkout button
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productList.size()>0)
                    checkout();
                else{
                    Toast.makeText(UserActivity.this,"Please add atleast one item to checkout",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void checkout() {
        int totalAmount=0;
        for(Product product:productList){
            int price=Integer.parseInt(product.getAmount());
            int quantity=Integer.parseInt(product.getQuantity());
            totalAmount+=price*quantity;
        }
        total.setText("Total Amount: "+totalAmount);
        //Toast.makeText(UserActivity.this, "Final Amount = "+totalAmount, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,ProductDetails.class);
        intent.putExtra(EXTRA_NUMBER,totalAmount);
        intent.putParcelableArrayListExtra(EXTRA_LIST,productList);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode  , resultCode,data);

        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "You Cancelled Scanning", Toast.LENGTH_SHORT).show();
            }
            else{
               // Toast.makeText(this, "Scanned Successfully "+result.getContents() , Toast.LENGTH_SHORT).show();

                //e1.setText(result.getContents());
                Query query=Post.child(result.getContents());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               /* String post = "Amount : "+dataSnapshot.child("amount").getValue(Product.class)+"\n"
                        +"Barcode : "+dataSnapshot.child("barcode").getValue(Product.class)+"\n"
                        +"Description : "+dataSnapshot.child("description").getValue(Product.class)+"\n"
                        +"Product Name : "+dataSnapshot.child("pname").getValue(String.class);*/
                        boolean found=false;
                        //productList.clear();
                        if (dataSnapshot.getValue()!=null) {
                            found=true;
                            Product product = dataSnapshot.getValue(Product.class);
                            int quantity=Integer.parseInt(product.getQuantity());
                            if(quantity>0) {
                                product.setQuantity("1");
                                productList.add(product);
                            }
                            else if(quantity==0){
                                Toast.makeText(UserActivity.this,"Product Out of Stock",Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(found==false) {
                            Toast.makeText(UserActivity.this, "This Product is not in Your Inventory", Toast.LENGTH_SHORT).show();
                        }
                        CheckoutListAdptr adapter = new CheckoutListAdptr(UserActivity.this, productList);
                        listViewAllProducts.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UserActivity.this,"Something Went Wrong, Please Try Again!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login2.class));
        finish();
    }
    public void onBackPressed() {
        //finish();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure, you want to Logout?");
        builder.setNegativeButton("Yes", null);
        builder.setPositiveButton("No",null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        // override the text color of negative button
        negativeButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        // provides custom implementation to negative button click
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegativeButtonClicked(alertDialog);
            }
            private void onNegativeButtonClicked(AlertDialog alertDialog) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),Login2.class));
                finish();
                Toast.makeText(UserActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }
}