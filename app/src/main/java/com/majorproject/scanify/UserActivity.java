package com.majorproject.scanify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import javax.annotation.Nullable;

public class UserActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    private Button scan,getbtn;
    EditText e1;
    private DatabaseReference Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        e1 = findViewById(R.id.editText1);

        String barcode = e1.getText().toString();

        getbtn = findViewById(R.id.button2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        Post = FirebaseDatabase.getInstance().getReference().child("Product");


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
                //integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
        /*
        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post.child("-M--7J2zoeu4J1PPBiH9")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String post = "Amount : "+dataSnapshot.child("amount").getValue(String.class)+"\n"
                                        +"Barcode : "+dataSnapshot.child("barcode").getValue(String.class)+"\n"
                                        +"Description : "+dataSnapshot.child("description").getValue(String.class)+"\n"
                                        +"Product Name : "+dataSnapshot.child("pname").getValue(String.class);

                                t1.setText(post);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        });
        */

        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,ProductDetails.class);
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
        finish();
    }
}
