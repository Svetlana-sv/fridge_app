package com.example.myfridge.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfridge.R;

public class FoodCardActivity extends AppCompatActivity {

    private EditText title;
    private EditText quantity;
    private EditText end_date;
    private EditText start_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_card_layout);
        Intent intent = getIntent();

        title = findViewById(R.id.food_card_title);
        quantity = findViewById(R.id.food_card_quantity);
        end_date = findViewById(R.id.food_card_end_date);
        start_date = findViewById(R.id.food_card_prod_date);

        title.setText(intent.getStringExtra("title"));
        quantity.setText(intent.getStringExtra("quantity"));
        end_date.setText(intent.getStringExtra("end_date"));
        start_date.setText(intent.getStringExtra("start_date"));
    }


}
