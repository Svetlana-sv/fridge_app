package com.example.myfridge.templates;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myfridge.R;
import com.example.myfridge.controllers.FoodActivity;
import com.example.myfridge.controllers.FoodCardActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Food extends ConstraintLayout {

    private String title;
    private String quantity;
    private String end_date;
    private String start_date;

    public Food(Context context, String title, String quantity, String end_date, String start_date) {
        super(context);
        inflate(context, R.layout.food_layout, this);

        while (end_date.length()<10)
            end_date= String.format(" %s ", end_date);

        this.title = title;
        this.quantity = quantity;
        this.start_date = start_date;
        this.end_date = end_date;



        //SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy");

//        try {
//            this.end_date = formatter.parse(end_date);
//            this.start_date = formatter.parse(start_date);
//        } catch (ParseException e) {
//
//            try {
//                this.end_date =  formatter.parse( new Date(System.currentTimeMillis()).toString());
//                this.start_date =  formatter.parse( new Date(System.currentTimeMillis()).toString());
//            } catch (ParseException parseException) {
//                parseException.printStackTrace();
//            }
//        }


        TextView title_tv = (TextView) findViewById(R.id.food_title);
        TextView end_date_tv = (TextView) findViewById(R.id.food_quantity);
        TextView quantity_tv = (TextView) findViewById(R.id.food_end_date);
        if (title.length() > 12)
            title_tv.setText(String.format("%s..", title.substring(0, 10)));
        else
            title_tv.setText(title);
        end_date_tv.setText(end_date);
        if (quantity.length() > 16)
            quantity_tv.setText(String.format("%s..", quantity.substring(0, 14)));
        else
            quantity_tv.setText(quantity);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getClass() == Food.class) {
                    Intent intent = new Intent(v.getContext(), FoodCardActivity.class);
                    intent.putExtra("title", ((Food) v).getTitle());
                    intent.putExtra("quantity", ((Food) v).getQuantity());
                    intent.putExtra("start_date", ((Food) v).getStart_date());
                    intent.putExtra("end_date", ((Food) v).getEnd_date());

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            }
        });

    }

    public String getQuantity() {
        return quantity;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getTitle() {
        return title;
    }

    public String getStart_date() {
        return start_date;
    }
}
