package com.example.myfridge;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class fridge extends ConstraintLayout {

    private TextView fridgeName;

    public fridge(Context context,String name){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflate(context,R.layout.fridge_layout,this);
        fridgeName = (TextView) findViewById(R.id.fridgeName);
        fridgeName.setText(name);
    }
    
}
