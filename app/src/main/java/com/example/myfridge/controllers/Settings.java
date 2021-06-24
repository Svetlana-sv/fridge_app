package com.example.myfridge.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfridge.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings extends AppCompatActivity {
    private ImageButton backBu;


    private Button saveSettings;
    private EditText editTextNumber;
    private ImageButton backToSettings;
    public static final String PROP_FILE_NAME = "prop_info.csv";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editTextNumber = findViewById(R.id.editTextNumber);
        saveSettings = findViewById(R.id.EditReciept);
        backToSettings = findViewById(R.id.backToSettings);
//        NumberPicker numberPicker = findViewById(R.id.numberPicker);
//        numberPicker.setMaxValue(9);
//        numberPicker.setMinValue(0);
//
//        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                //Toast.makeText(Settings.this,newVal,Toast.LENGTH_SHORT).show();
//            }
//        });


        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                            Settings.this.openFileOutput(PROP_FILE_NAME, MODE_PRIVATE)));
                    out.write(editTextNumber.getText().toString());
                    out.close();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.SavedProp), Toast.LENGTH_SHORT).show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        backToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            BufferedReader food_in = new BufferedReader(new InputStreamReader(
                    Settings.this.openFileInput(PROP_FILE_NAME)));
            String line;
            while ((line = food_in.readLine()) != null) {
                editTextNumber.setText(line);
            }

        } catch (FileNotFoundException e) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        Settings.this.openFileOutput(PROP_FILE_NAME, MODE_PRIVATE)));
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
        }
        backToSettings.setOnTouchListener(TouchListener.touchListener);
        saveSettings.setOnTouchListener(TouchListener.touchListener);
    }
}