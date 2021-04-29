package com.example.myfridge.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.myfridge.R;
import com.example.myfridge.templates.Food;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import static java.lang.Integer.*;

public class FoodActivity extends AppCompatActivity {

    private int id;
    private String fridge_name;
    private LinearLayout food_layout;
    private SearchView input;
    private Thread thread;
    private int LL_CONST_CHILD;
    private ConstraintLayout inputBar;
    private View.OnTouchListener myTouch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_layout);
        Intent intent = getIntent();

        //Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ImageButton backToMain = (ImageButton) findViewById(R.id.backToFridgeActivity);
        this.fridge_name = intent.getStringExtra("name");
        this.id = intent.getIntExtra("id", -1);
        this.food_layout = (LinearLayout) findViewById(R.id.food_layout);
        this.input = (SearchView) findViewById(R.id.food_search);
        this.inputBar = findViewById(R.id.inputBar);
        ImageButton cook = findViewById(R.id.cook);
        Button addFoodBtn = (Button) findViewById(R.id.save);
        TextView title_fridge_name = (TextView) findViewById(R.id.frigde_name_title_food_card);

         myTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                switch (eventAction) {
                    case MotionEvent.ACTION_DOWN:
                        v.getBackground().setColorFilter(getResources().getColor(R.color.primaryBarColor), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getBackground().clearColorFilter();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return false;
            }
        };
        backToMain.setOnTouchListener(myTouch);
        addFoodBtn.setOnTouchListener(myTouch);
        cook.setOnTouchListener(myTouch);
//        ArrayList<String> products = intent.getStringArrayListExtra("products");



//        for (String str : products) {
//            String[] row = str.split(";");
//            //id; name; quantity; end_date;start_date;smth
//            food_layout.addView(new Food(getContext(), row[1], row[2], row[3], row[4]));
//        }

 //       readFridgeData();

        input.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for (int i = LL_CONST_CHILD; i < food_layout.getChildCount(); i++) {
                    Food fd = ((Food) food_layout.getChildAt(i));
                    String fd_text = String.format("%s%s%s", fd.getTitle(), fd.getEnd_date(), fd.getQuantity()).toLowerCase();
                    if (!fd_text.contains(newText.toLowerCase())) fd.setVisibility(View.GONE);
                    else fd.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                for (Food f : food) {
//                    food_layout.addView(f);
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//        };


        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((ConstraintLayout) input.getParent()).setVisibility(View.GONE);
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

        //readFridgeData();
        progressBar.setVisibility(View.GONE);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private boolean is_start_date_set = false;

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setOwnerActivity(this);
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
                        //&& DateStartBtn.getText().toString().contains(".")
                        && DateEndBtn.getText().toString().contains(".")) {
                    addNewFood(input_name.getText().toString(), DateStartBtn.getText().toString(), DateEndBtn.getText().toString(), input_quantity.getText().toString());
                    dialog.cancel();
                } else {
                    input_name.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.errorColor, null));
                    input_name.setTypeface(input_name.getTypeface(), Typeface.BOLD);
                    //DateStartBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorColor, null));
                    //DateStartBtn.setTypeface(input_name.getTypeface(), Typeface.BOLD);
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
                        FoodActivity.this,
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
                        FoodActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                DateEndBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.primaryTextColor, null));
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
        input_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_name.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.primaryTextColor, null));
            }
        });

        dialog.show();
    }

    private void addNewFood(String name, String start_date, String end_date, String quantity) {
        Food fd = new Food(getContext(), name, quantity, end_date, start_date,id,-1);
        food_layout.addView(fd);

        thread = new Thread(new Runnable() {
            public synchronized void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            getApplicationContext().openFileInput(id + ".csv")));
                    String line;
                    String lastAppendedRow = null;
                    StringBuilder rows = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        rows.append(line + "\n");
                        lastAppendedRow = line;
                    }
                    if (rows.length() == 0) {
                        //id; name; quantity; end_date;start_date;smth
                        rows.append(String.format("0;%s;%s;%s;%s;", name, quantity, end_date, start_date));
                        fd.setFoodId(0);
                    } else {
                        String[] attrs = lastAppendedRow.split(";");

                        rows.append(String.format("%d;%s;%s;%s;%s;", parseInt(attrs[0]) + 1, name, quantity,end_date, start_date));
                        fd.setFoodId(parseInt(attrs[0]) + 1);
                    }
                    in.close();

                    FileOutputStream fos = openFileOutput(id + ".csv", MODE_PRIVATE);
                    fos.write(rows.toString().getBytes());
                    fos.close();

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "ACCESS ERROR", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "ACCESS ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
        thread.start();
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //thread.interrupt();
        //Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        food_layout.removeAllViews();
        System.gc();
        //Toast.makeText(getApplicationContext(), "DESTROY", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        for (int i = LL_CONST_CHILD; i < food_layout.getChildCount() + 1; i++) {
//            food_layout.removeViewAt(i);
//        }
        //if (removeViewId != - 1) food_layout.getChildAt(removeViewId + LL_CONST_CHILD).setVisibility(View.GONE);
        //else
        readFridgeData();
        //Toast.makeText(getApplicationContext(), "RESUMED", Toast.LENGTH_SHORT).show();
    }

    private void readFridgeData(){
        //for (int i = LL_CONST_CHILD; i<food_layout.getChildCount();i++)
        food_layout.removeAllViews();
        food_layout.addView(inputBar);
        Food columns = new Food(getContext(), getResources().getString(R.string.TITLE_COLUMN),
                getResources().getString(R.string.QUANTITI_COLUMN),
                getResources().getString(R.string.DATE_COLUMN),
                "",-1,-1);
        columns.setClickable(false);
        food_layout.addView(columns);

        LL_CONST_CHILD = food_layout.getChildCount();



        try {
            BufferedReader food_in = new BufferedReader(new InputStreamReader(
                    getContext().openFileInput(id+".csv")));
            String line;
            while ((line = food_in.readLine()) != null) {
                String[] row = line.split(";");

                //id; name; quantity; end_date;start_date;smth
                food_layout.addView(new Food(getContext(), row[1], row[2], row[3], row[4],id,Integer.parseInt(row[0])));
            }

        } catch (FileNotFoundException e) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        getContext().openFileOutput(id+".csv", MODE_PRIVATE)));
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
        }
    }
}
