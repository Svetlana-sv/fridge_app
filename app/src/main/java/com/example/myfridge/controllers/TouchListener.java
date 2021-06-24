package com.example.myfridge.controllers;

import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

import com.example.myfridge.R;

public class TouchListener implements View.OnTouchListener {
    public static TouchListener touchListener = new TouchListener();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventAction = event.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                v.getBackground().setColorFilter(v.getResources().getColor(R.color.primaryBarColor), PorterDuff.Mode.SRC_ATOP);
                break;
            case MotionEvent.ACTION_UP:
                v.getBackground().clearColorFilter();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }

        return false;
    }
}
