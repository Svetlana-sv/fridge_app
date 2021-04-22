package com.example.myfridge.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.myfridge.templates.Food;
import com.example.myfridge.R;

import java.util.ArrayList;
import java.util.Calendar;

public class FoodActivity extends AppCompatActivity {

    private ArrayList<Food> food;
    private int id;
    private String fridge_name;
    private LinearLayout food_layout;
    private SearchView input;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_layout);
        Intent intent = getIntent();

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ImageButton backToMain = (ImageButton) findViewById(R.id.backToMain);
        this.fridge_name = intent.getStringExtra("name");
        this.id = intent.getIntExtra("id", -1);
        this.food = new ArrayList<>();
        this.food_layout = (LinearLayout) findViewById(R.id.food_layout);
        this.input = (SearchView) findViewById(R.id.food_search);
        Button addFoodBtn = (Button) findViewById(R.id.createFood);
        TextView title_fridge_name = (TextView) findViewById(R.id.frigde_name_title);
        ArrayList<String> products = intent.getStringArrayListExtra("products");

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                for (Food f : food) {
                    food_layout.addView(f);
                }
                progressBar.setVisibility(View.GONE);
            }
        };

        thread = new Thread(new Runnable() {
            public synchronized void run() {
                for (String str : products) {
                    String[] row = str.split(";");
                    //id; name; quantity; end_date;start_date;smth
                    try {
                        food.add(new Food(getContext(), row[1], row[2], row[3],row[4]));
                    } catch (Exception e) {
                        return;
                    }

                }
//                while (food.size()<1000){
//                    food.add(new Food(getContext(), "Молоко", "1 шт.", "12.10.2021","12.10.2021"));
//                }
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();

        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (fridge_name.length() >= 15)
            title_fridge_name.setText(String.format("%s...", fridge_name.toUpperCase().

                    substring(0, 15)));
        else title_fridge_name.setText(fridge_name.toUpperCase());

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private boolean is_start_date_set = false;

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_food);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fridge_back, null));
        EditText input_name = (EditText) dialog.findViewById(R.id.dialog_input_food_name);
        EditText input_quantity = (EditText) dialog.findViewById(R.id.dialog_input_quantity);
        Button OK = (Button) dialog.findViewById(R.id.dialog_add_food_accept);
        Button BACK = (Button) dialog.findViewById(R.id.dialog_add_food_reject);
        Button DateStartBtn = (Button) dialog.findViewById(R.id.dialog_input_start_date);
        Button DateEndBtn = (Button) dialog.findViewById(R.id.dialog_input_end_date);
        ImageButton fotoBtn = (ImageButton) dialog.findViewById(R.id.dialog_foto_btn);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_name.getText().length() > 0
                        && !input_name.getText().toString().contains("\"")
                        && !input_name.getText().toString().contains("'")
                        && !input_name.getText().toString().contains(";")
                        && DateStartBtn.getText().toString().contains(".")
                        && DateEndBtn.getText().toString().contains(".")) {
                    addNewFood(input_name.getText().toString(),DateStartBtn.getText().toString(),DateEndBtn.getText().toString(),input_quantity.getText().toString());
                    dialog.cancel();
                } else {
                    input_name.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.errorColor, null));
                    input_name.setTypeface(input_name.getTypeface(), Typeface.BOLD);
                    DateStartBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorColor, null));
                    DateStartBtn.setTypeface(input_name.getTypeface(), Typeface.BOLD);
                    DateEndBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorColor, null));
                    DateEndBtn.setTypeface(input_name.getTypeface(), Typeface.BOLD);
                }
            }
        });
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        DateStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date_set = true;
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        DateEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date_set = false;
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                if (is_start_date_set)
                    DateStartBtn.setText(date);
                else
                    DateEndBtn.setText(date);
            }
        };

        fotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    private void addNewFood(String name, String start_date, String end_date, String quantity) {
        Food fd = new Food(getContext(),name,quantity,end_date,start_date);
        food.add(fd);
        food_layout.addView(fd);
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        thread.interrupt();
        food = null;
        food_layout = null;
        System.gc();
        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
    }
}
