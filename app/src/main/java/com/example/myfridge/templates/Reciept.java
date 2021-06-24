package com.example.myfridge.templates;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myfridge.R;
import com.example.myfridge.controllers.RecieptActivity;

public class Reciept extends ConstraintLayout {
    private final TextView title;

    public Reciept(Context context, String title, String FileName) {
        super(context);
        inflate(context, R.layout.reciept, this);
        this.title = findViewById(R.id.reciept_title);
        this.title.setText(title);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecieptActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("file_name", FileName);
                v.getContext().startActivity(intent);
            }

        });

        int colorFrom = getResources().getColor(R.color.secondBackColor);
        int colorTo = getResources().getColor(R.color.primaryBarColor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        // colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                Reciept.this.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        this.setBackground(getResources().getDrawable(R.drawable.food_anim));
        final TransitionDrawable background = (TransitionDrawable) Reciept.this.getBackground();


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
    }

    public String getTitle() {
        return this.title.getText().toString();
    }
}
