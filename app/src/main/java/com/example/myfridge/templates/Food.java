package com.example.myfridge.templates;

import android.content.Context;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myfridge.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Food extends ConstraintLayout {

    private String title;
    private String quantity;
    private Date end_date;

    public Food(Context context, String title, String quantity, String end_date,String start_date) {
        super(context);
        inflate(context, R.layout.food_layout, this);

        this.title = title;
        this.quantity = quantity;

        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy");

        try {
            this.end_date = formatter.parse(end_date);
        } catch (ParseException e) {
            this.end_date = new Date(System.currentTimeMillis());
        }


        TextView title_tv = (TextView) findViewById(R.id.food_title);
        TextView end_date_tv = (TextView) findViewById(R.id.food_end_date);
        TextView quantity_tv = (TextView) findViewById(R.id.food_quantity);

        title_tv.setText(title);
        end_date_tv.setText(quantity);
        quantity_tv.setText(end_date);

    }

    public String getQuantity() {
        return quantity;
    }
}
