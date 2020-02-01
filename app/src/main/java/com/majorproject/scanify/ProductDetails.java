package com.majorproject.scanify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

public class ProductDetails extends AppCompatActivity {

    TextView t1;
    private ListView mListView;
    private DatabaseReference Post;
    private static final String TAG = "ProductDetails";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        t1 = findViewById(R.id.textView);
        String temp = "9780143428848";

        Post = FirebaseDatabase.getInstance().getReference().child("Product");

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        Post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*String post = "Amount : " + dataSnapshot.child("amount").getValue(String.class) + "\n"
                        + "Barcode : " + dataSnapshot.child("barcode").getValue(String.class) + "\n"
                        + "Description : " + dataSnapshot.child("description").getValue(String.class) + "\n"
                        + "Product Name : " + dataSnapshot.child("pname").getValue(String.class);

                t1.setText(post);*/
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String amount=data.child("amount").getValue().toString();
                    String description=data.child("description").getValue().toString();
                    String pname=data.child("pname").getValue().toString();
                    t1.setText("Amount: "+amount+"\n"+"Description: "+description+"\n"+"Product Name: "+pname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, UserActivity.class));
    }
}