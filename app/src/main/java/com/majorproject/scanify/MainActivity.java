package com.majorproject.scanify;

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
    private Button scan,saveBtn;
    FirebaseDatabase database;
    EditText e1,e2,e3,e4;

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
        saveBtn = findViewById(R.id.save);

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
                Product PO = new Product();
                PO.setBarcode(B);
                PO.setDescription(PD);
                PO.setPname(PN);
                PO.setAmount(PA);
                reference.push().setValue(PO);
                Toast.makeText(getApplicationContext(),"Data Successfully Saved",Toast.LENGTH_SHORT).show();;
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

    //Logout
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    public void onBackPressed() {
        finish();
    }
}
