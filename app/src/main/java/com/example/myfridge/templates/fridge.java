package com.example.myfridge.templates;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myfridge.controllers.MainActivity.FRIDGES_FILE_NAME;
import static com.example.myfridge.controllers.MainActivity.appCompatActivity;
//import static com.example.myfridge.controllers.MainActivity.activity;


public class fridge extends ConstraintLayout{

    private String name;
    private int id;

    public fridge(Context context, String name, int id) {
        super(context);
        inflate(context, R.layout.fridge_layout, this);
        TextView fridgeName = (TextView) findViewById(R.id.fridgeName);
        fridgeName.setText(name);

        this.name = name;
        this.id = id;

        this.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog();

                return true;
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getClass() == fridge.class) {

                    ArrayList<String> products = new ArrayList<String>();
                    try {
                        BufferedReader food_in = new BufferedReader(new InputStreamReader(
                                getContext().openFileInput(((fridge) v).id+".csv")));
                        String line;
                        while ((line = food_in.readLine()) != null) {
                            products.add(line);
                        }

                    } catch (FileNotFoundException e) {
                        try {
                            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                                getContext().openFileOutput((((fridge) v).id+".csv"), MODE_PRIVATE)));
                            out.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } catch (IOException e) {
                    }


                    Intent intent = new Intent(v.getContext(), FoodActivity.class);
                    intent.putExtra("name", ((fridge) v).name);
                    intent.putExtra("id", ((fridge) v).id);
                    intent.putStringArrayListExtra("products",products);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
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
                    attrs[0] = "" + (Integer.parseInt(attrs[0]) - 1);

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


        TextView drop_fridge_title = (TextView) dialog.findViewById(R.id.drop_dialog_title);
        drop_fridge_title.setText(String.format("%s%s", drop_fridge_title.getText(), " "+this.name+" ?"));
        Button OK = (Button) dialog.findViewById(R.id.drop_dialog_accept);
        Button BACK = (Button) dialog.findViewById(R.id.drop_dialog_reject);

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
        getContext().deleteFile(this.id+".csv");
    }

    public String getName() {
        return name;
    }
}

