package com.example.myfridge.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.myfridge.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import static com.example.myfridge.controllers.MainActivity.FRIDGES_FILE_NAME;
import static com.example.myfridge.controllers.MainActivity.appCompatActivity;
import static java.lang.Integer.parseInt;

public class FoodCardActivity extends AppCompatActivity {

    private EditText title;
    private EditText quantity;
    private Button end_date;
    private Button start_date;
    private int id;
    private int fridgeId;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private boolean is_start_date_set = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_card_layout);
        Intent intent = getIntent();

        this.id = intent.getIntExtra("id", -1);
        this.fridgeId = intent.getIntExtra("fridgeId", -1);
        title = findViewById(R.id.food_card_title);
        quantity = findViewById(R.id.food_card_quantity);
        end_date = findViewById(R.id.food_card_end_date);
        start_date = findViewById(R.id.food_card_prod_date);
        ImageButton deleteFood = findViewById(R.id.delete_food);
        ImageButton undo = findViewById(R.id.undo_food);
        ImageButton back = findViewById(R.id.backToFridgeActivity);
        Button save = findViewById(R.id.save);

        title.setText(intent.getStringExtra("title"));
        title.setTag(title.getText().toString());
        quantity.setText(intent.getStringExtra("quantity"));
        quantity.setTag(quantity.getText().toString());
        end_date.setText(intent.getStringExtra("end_date"));
        end_date.setTag(end_date.getText().toString());
        if (intent.getStringExtra("start_date").contains("."))
            start_date.setText(intent.getStringExtra("start_date"));
        start_date.setTag(start_date.getText().toString());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View.OnTouchListener myTouch = new View.OnTouchListener() {
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

        undo.setOnTouchListener(myTouch);
        back.setOnTouchListener(myTouch);
        deleteFood.setOnTouchListener(myTouch);
        save.setOnTouchListener(myTouch);

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date_set = false;
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        FoodCardActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date_set = true;
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        FoodCardActivity.this,
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
                    start_date.setText(date);
                else
                    end_date.setText(date);
            }
        };

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText(title.getTag().toString());
                quantity.setText(quantity.getTag().toString());
                end_date.setText(end_date.getTag().toString());
                start_date.setText(start_date.getTag().toString());
            }
        });
        deleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(FoodCardActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.drop_fridge_dialog);
                dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fridge_back, null));
                TextView drop_food_title = (TextView) dialog.findViewById(R.id.drop_dialog_title);
                drop_food_title.setText(String.format("%s %s ?", drop_food_title.getText(),FoodCardActivity.this.title.getText()));
                Button OK = (Button) dialog.findViewById(R.id.drop_dialog_accept);
                Button BACK = (Button) dialog.findViewById(R.id.drop_dialog_reject);
                OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    getApplicationContext().openFileInput(String.format("%d.csv", fridgeId))));
                            String line;
                            StringBuilder rows = new StringBuilder();
                            while ((line = in.readLine()) != null) {
                                String[] attrs = line.split(";");

                                if (attrs[0].equals("" + id)) {
                                    continue;
                                }
                                if (Integer.parseInt(attrs[0]) > id)
                                    attrs[0] = "" + (Integer.parseInt(attrs[0]) - 1);

                                rows.append(String.format("%d;%s;%s;%s;%s;\n", parseInt(attrs[0]), attrs[1], attrs[2], attrs[3], attrs[4]));
                            }

                            FileOutputStream fos = getApplicationContext().openFileOutput(String.format("%d.csv", fridgeId), MODE_PRIVATE);
                            fos.write(rows.toString().getBytes());
                            in.close();
                            fos.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                        finish();
                    }
                });
                BACK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

    }


}
