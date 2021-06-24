package com.example.myfridge.templates;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.myfridge.R;
import com.example.myfridge.controllers.FoodActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myfridge.controllers.MainActivity.FRIDGES_FILE_NAME;
import static com.example.myfridge.controllers.MainActivity.appCompatActivity;
//import static com.example.myfridge.controllers.MainActivity.activity;


public class fridge extends ConstraintLayout {

    private final String name;
    private final int id;

    public fridge(Context context, String name, int id) {
        super(context);
        inflate(context, R.layout.fridge_layout, this);
        TextView fridgeName = findViewById(R.id.fridgeName);
        fridgeName.setText(name);
        this.setBackground(getResources().getDrawable(R.drawable.fridge_back));
        this.name = name;
        this.id = id;

        int colorFrom = getResources().getColor(R.color.secondBackColor);
        int colorTo = getResources().getColor(R.color.primaryBarColor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        // colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                fridge.this.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        this.setBackground(getResources().getDrawable(R.drawable.anim_drawable));
        final TransitionDrawable background = (TransitionDrawable) fridge.this.getBackground();

        this.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog();
                return true;
            }
        });

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

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getClass() == fridge.class) {
                    Thread thread = new Thread(new Runnable() {
                        public synchronized void run() {
                            try {
                                wait(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(v.getContext(), FoodActivity.class);
                            intent.putExtra("name", ((fridge) v).name);
                            intent.putExtra("id", ((fridge) v).id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);


                        }
                    });
                    thread.start();
                }
            }
        });

    }

    protected void dropFridgeInfo() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    super.getContext().getApplicationContext().openFileInput(FRIDGES_FILE_NAME)));
            String line;
            StringBuilder rows = new StringBuilder();
            while ((line = in.readLine()) != null) {
                String[] attrs = line.split(";");
                if (attrs[1].equals(this.name)) continue;
                if (Integer.parseInt(attrs[0]) > this.id)
                    attrs[0] = String.format("%d", Integer.parseInt(attrs[0]) - 1);

                rows.append(String.format("%s;%s;\n", attrs[0], attrs[1]));
            }

            FileOutputStream fos = super.getContext().openFileOutput(FRIDGES_FILE_NAME, MODE_PRIVATE);
            fos.write(rows.toString().getBytes());
            in.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(appCompatActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.drop_fridge_dialog);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fridge_back, null));
        TextView drop_fridge_title = dialog.findViewById(R.id.drop_dialog_title);
        drop_fridge_title.setText(String.format("%s %s ?", drop_fridge_title.getText(), this.name));
        Button OK = dialog.findViewById(R.id.drop_dialog_accept);
        Button BACK = dialog.findViewById(R.id.drop_dialog_reject);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFridgeView();
                dialog.cancel();
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

    private void removeFridgeView() {
        ((LinearLayout) this.getParent()).removeView(this);
        dropFridgeInfo();
        getContext().deleteFile(String.format("%d.csv", this.id));
    }

    public String getName() {
        return name;
    }
}

