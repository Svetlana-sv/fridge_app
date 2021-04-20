package com.example.myfridge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myfridge.MainActivity.FRIDGES_FILE_NAME;
import static com.example.myfridge.MainActivity.appCompatActivity;
//import static com.example.myfridge.MainActivity.activity;


public class fridge extends ConstraintLayout {

    private String name;
    private int id;

    public fridge(Context context, String name, int id) {
        super(context);
        inflate(context, R.layout.fridge_layout, this);
        TextView fridgeName = (TextView) findViewById(R.id.fridgeName);
        fridgeName.setText(name);

        this.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog();

                return true;
            }
        });
        this.name = name;
        this.id = id;
    }

    protected void dropFridgeInfo() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    super.getContext().getApplicationContext().openFileInput(FRIDGES_FILE_NAME)));
            String line;
            StringBuilder rows = new StringBuilder();
            while ((line = in.readLine()) != null) {
                String[] attrs = line.split(",");
                if (attrs[1].equals(this.name)) continue;
                if (Integer.parseInt(attrs[0]) > this.id)
                    attrs[0] = "" + (Integer.parseInt(attrs[0]) - 1);

                rows.append(attrs[0] + "," + attrs[1] + "," + "\n");
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
        drop_fridge_title.setText(String.format("%s%s", drop_fridge_title.getText(), " " + this.name));
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
    }
}

