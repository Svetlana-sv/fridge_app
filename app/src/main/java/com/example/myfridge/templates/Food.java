package com.example.myfridge.templates;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myfridge.R;
import com.example.myfridge.controllers.FoodCardActivity;

public class Food extends ConstraintLayout {

    private final String title;
    private final String quantity;
    private final String end_date;
    private final String start_date;
    private final int fridgeId;
    private int foodid;

    public Food(Context context, String title, String quantity, String end_date, String start_date, int id, int foodid) {
        super(context);
        inflate(context, R.layout.food_layout, this);

        this.title = title;
        this.quantity = quantity;
        this.start_date = start_date;
        this.end_date = end_date;
        this.fridgeId = id;
        this.foodid = foodid;

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
        TextView title_tv = findViewById(R.id.food_title);
        TextView end_date_tv = findViewById(R.id.food_quantity);
        TextView quantity_tv = findViewById(R.id.food_end_date);

//        if (end_date.contains("."))
//            this.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    int eventAction = event.getAction();
//
//                    switch (eventAction) {
//                        case MotionEvent.ACTION_DOWN:
//                            v.setBackground(getResources().getDrawable(R.drawable.food_click_style));
//                            end_date_tv.setBackground(null);
//                            //v.getBackground().setColorFilter(getResources().getColor(R.color.primaryBarColor), PorterDuff.Mode.SRC_ATOP);
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            v.setBackground(getResources().getDrawable(R.drawable.food_back_style));
//                            end_date_tv.setBackground(getResources().getDrawable(R.drawable.border_left_right));
//                            //v.getBackground().clearColorFilter();
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            break;
//                    }
//
//                    return false;
//                }
//            });

        int colorFrom = getResources().getColor(R.color.secondBackColor);
        int colorTo = getResources().getColor(R.color.primaryBarColor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        // colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                Food.this.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        this.setBackground(getResources().getDrawable(R.drawable.food_anim));
        final TransitionDrawable background = (TransitionDrawable) Food.this.getBackground();


        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                switch (eventAction) {
                    case MotionEvent.ACTION_DOWN:

                        Thread thread = new Thread(new Runnable() {
                            public synchronized void run() {
                                try {

                                    background.startTransition(300);
                                    wait(300);
                                    background.reverseTransition(300);


                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return false;
            }
        });

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
                    intent.putExtra("id", ((Food) v).getFoodId());
                    intent.putExtra("fridgeId", ((Food) v).getFridgeId());

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            }
        });

    }

    private int getFoodId() {
        return this.foodid;
    }

    public int getFridgeId() {
        return fridgeId;
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

    public void setFoodId(int id) {
        this.foodid = id;
    }

}
