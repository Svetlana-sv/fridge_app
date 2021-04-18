package com.example.myfridge;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class fridge extends ConstraintLayout {

    private String name;
    private int id;

    public fridge(Context context,String name,int id){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflate(context,R.layout.fridge_layout,this);
        TextView fridgeName = (TextView) findViewById(R.id.fridgeName);
        fridgeName.setText(name);
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getFridgeId() {
        return this.id;
    }

    public void setFridgeId(int id) {
        this.id = id;
    }
}
