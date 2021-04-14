package com.example.myfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button createFridge;
    private ImageButton mailBu;
    private ImageButton profileBu;
    private ImageButton cookBu;
    private LinearLayout fridges;
    private ScrollView fridgesSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cookBu = (ImageButton) findViewById(R.id.cook);
        this.profileBu = (ImageButton) findViewById(R.id.profile);
        this.mailBu = (ImageButton) findViewById(R.id.mail);
        this.fridges = (LinearLayout) findViewById(R.id.fridges);

        fridge myFridge = new fridge(getApplicationContext(),"ФРИДГЕ");

        fridges.addView(myFridge);


        cookBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "go to cook", Toast.LENGTH_SHORT).show();
            }
        });
        mailBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "go to mail", Toast.LENGTH_SHORT).show();
            }
        });
        profileBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "go to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}