package com.example.myfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button createFridge;
    private ImageButton mailBu;
    private ImageButton profileBu;
    private ImageButton cookBu;
    private LinearLayout fridges_layout;
    private ArrayList<fridge> fridges;
    private ScrollView fridgesSV;

    //private final static String FRIDGES_SYNC_FILE_NAME = "fridgesSyncInfo.csv";
    private final static String FRIDGES_FILE_NAME = "fridgesInfo.csv";
    private LinearLayout.LayoutParams layoutParams;
    private String fridgeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fridges = new ArrayList<fridge>();
        this.cookBu = (ImageButton) findViewById(R.id.cook);
        this.profileBu = (ImageButton) findViewById(R.id.profile);
        this.mailBu = (ImageButton) findViewById(R.id.mail);
        this.fridges_layout = (LinearLayout) findViewById(R.id.fridges);
        this.createFridge = (Button) findViewById(R.id.createFridge);
        this.fridgesSV = (ScrollView) findViewById(R.id.fridgesSV);


        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 10, 0, 30);


        createFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        cookBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "go to cook", Toast.LENGTH_SHORT).show();
            }
        });
        mailBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "go to mail", Toast.LENGTH_SHORT).show();
            }
        });
        profileBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "go to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });
        ReadFridgeFiles(FRIDGES_FILE_NAME);
        //ReadFridgeFiles(FRIDGES_SYNC_FILE_NAME);
    }

    private void addNewFridge() {

        fridge myFridge = null;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getApplicationContext().openFileInput(FRIDGES_FILE_NAME)));
            String line;
            String lastAppendedRow = null;
            StringBuilder rows = new StringBuilder();
            while ((line = in.readLine()) != null) {
                rows.append(line + "\n");
                lastAppendedRow = line;
            }
            if (rows.length() == 0) {
                myFridge = new fridge(getApplicationContext(), fridgeName, 0);
                rows.append("0," + fridgeName + ",");
            } else {
                String[] attrs = lastAppendedRow.split(",");
                myFridge = new fridge(getApplicationContext(), fridgeName, Integer.parseInt(attrs[0]) + 1);
                rows.append((Integer.parseInt(attrs[0]) + 1) + "," + fridgeName + ",");
            }
            in.close();

            FileOutputStream fos = openFileOutput(FRIDGES_FILE_NAME, MODE_PRIVATE);
            fos.write(rows.toString().getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "ACCESS ERROR", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ACCESS ERROR", Toast.LENGTH_SHORT).show();
        }
        myFridge.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v.getClass() == fridge.class) {

                    removeFridge(((fridge) v).getFridgeId());
                }
                return false;
            }
        });
        fridges.add(myFridge);
        fridges_layout.addView(myFridge, layoutParams);

    }

    private void removeFridge(int fridgeId) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getApplicationContext().openFileInput(FRIDGES_FILE_NAME)));
            String line;
            StringBuilder rows = new StringBuilder();
            while ((line = in.readLine()) != null) {
                String[] attrs = line.split(",");
                if (Integer.parseInt(attrs[0]) == fridgeId) continue;
                if (Integer.parseInt(attrs[0]) > fridgeId)
                    attrs[0] = "" + (Integer.parseInt(attrs[0]) - 1);

                rows.append(attrs[0] + "," + attrs[1] + "," + "\n");
            }

            FileOutputStream fos = openFileOutput(FRIDGES_FILE_NAME, MODE_PRIVATE);
            fos.write(rows.toString().getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (fridge item : fridges) {

            if (item.getFridgeId() == fridgeId) {
                fridges.remove(item);

                continue;
            }

            if (item.getFridgeId() > fridgeId) item.setFridgeId(item.getFridgeId() - 1);
            //fridges_layout.addView(item,layoutParams);
        }
        Toast.makeText(getApplicationContext(), ""+fridgeId, Toast.LENGTH_SHORT).show();
        //fridges_layout.removeViewAt(fridgeId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void ReadFridgeFiles(String FILE_NAME) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getApplicationContext().openFileInput(FILE_NAME)));
            String line;
            while ((line = in.readLine()) != null) {
                String[] attrs = line.split(",");
                fridge myFridge = new fridge(getApplicationContext(), attrs[1], Integer.parseInt(attrs[0]));

                myFridge.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (v.getClass() == fridge.class)
                            removeFridge(((fridge) v).getFridgeId());
                        return false;
                    }
                });

                fridges.add(myFridge);
                fridges_layout.addView(myFridge, layoutParams);
            }
            in.close();
        } catch (FileNotFoundException e) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        getApplicationContext().openFileOutput(FILE_NAME, MODE_PRIVATE)));
                out.close();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "ACCESS ERROR", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ACCESS ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setOwnerActivity(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_activity);
        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);

        EditText input_name = (EditText) dialog.findViewById(R.id.DialogInput);
        Button OK = (Button) dialog.findViewById(R.id.dialog_accept);
        Button BACK = (Button) dialog.findViewById(R.id.dialog_reject);


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_name.getText().length() > 0) {
                    fridgeName = "" + input_name.getText();
                    addNewFridge();
                    dialog.cancel();
                } else {
                    title.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorColor, null));
                    title.setTypeface(title.getTypeface(), Typeface.BOLD);
                }
            }
        });
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fridge_back, null));
        dialog.show();
    }


}