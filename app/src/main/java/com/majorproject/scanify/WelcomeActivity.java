package com.majorproject.scanify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }
    public void admin(View view){
        Intent intent = new Intent(WelcomeActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
    public void user(View view){
        Intent intent = new Intent(WelcomeActivity.this, Login2.class);
        startActivity(intent);
        finish();
    }
    public void onBackPressed() {
        finish();
    }


}
