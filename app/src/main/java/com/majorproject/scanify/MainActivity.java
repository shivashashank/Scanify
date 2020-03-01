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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    TextView fullName,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference reference;
    String userId;
    Button scan,saveBtn,checkout,viewall;
    FirebaseDatabase database;
    EditText e1,e2,e3,e4,e5;
    int quantity=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        e1 = findViewById(R.id.editText1);
        e2 = findViewById(R.id.editText2);
        e3 = findViewById(R.id.editText3);
        e4 = findViewById(R.id.editText4);
        e5 = findViewById(R.id.editText5);
        saveBtn = findViewById(R.id.save);
        checkout = findViewById(R.id.addtocart);
        viewall = findViewById(R.id.view);

        //Firebase
        database=FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        reference= database.getReference().child("Product");
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phone.setText(documentSnapshot.getString("phone"));
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }
        });
        //

        //Scan
        scan = (Button) findViewById(R.id.scan);
        final Activity activity = this;
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
                //integrator.setOrientationLocked(false);
            }
        });

        //Save
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v )
            {
                String B = e1.getText().toString();
                String PN = e2.getText().toString();
                String PD = e3.getText().toString();
                String PA = e4.getText().toString();
                String PQ = e5.getText().toString();
                if(B.equals("")||PN.equals("")||PD.equals("")||PA.equals("")||PQ.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all the fields" , Toast.LENGTH_SHORT).show();
                else {
                    if(Integer.parseInt(PQ)>quantity) {
                        Product PO = new Product();
                        PO.setBarcode(B);
                        PO.setDescription(PD);
                        PO.setPname(PN);
                        PO.setAmount(PA);
                        PO.setQuantity(PQ);
                        reference.child(B).setValue(PO);
                        e1.setEnabled(true);
                        e2.setEnabled(true);
                        e3.setEnabled(true);
                        e4.setEnabled(true);

                        e1.setText("");
                        e2.setText("");
                        e3.setText("");
                        e4.setText("");
                        e5.setText("");
                        Toast.makeText(getApplicationContext(), "Data Successfully Saved", Toast.LENGTH_SHORT).show();
                    }
                    else if(Integer.parseInt(PQ)==quantity){
                        Toast.makeText(MainActivity.this, "Quantity can't be the same" , Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Quantity can't be less than "+quantity , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ViewAllProductsActivity.class);
                startActivity(i);
                finish();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AdmincheckoutActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode  , resultCode,data);

        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "You Cancelled Scanning", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Scanned Successfully" , Toast.LENGTH_SHORT).show();
                e1.setText(result.getContents());
                Query getData=reference.child(result.getContents());
                getData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null) {
                            Product product = dataSnapshot.getValue(Product.class);
                            if (product != null) {
                                Toast.makeText(MainActivity.this, "Product Exists" , Toast.LENGTH_SHORT).show();
                                e1.setEnabled(false);
                                e2.setText(product.getPname());
                                e2.setEnabled(false);
                                e3.setText(product.getDescription());
                                e3.setEnabled(false);
                                e4.setText(product.getAmount());
                                e4.setEnabled(false);
                                e5.setText(product.getQuantity());
                                quantity = Integer.parseInt(product.getQuantity());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Logout
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    public void onBackPressed() {
      //  finish();
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
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }
}
