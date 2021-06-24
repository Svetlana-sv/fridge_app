package com.example.myfridge.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfridge.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RecieptActivity extends AppCompatActivity {
    private String title;
    private String FileName;
    private TextView RecieptTitle, RecieptDescription;
    private Button EditReciept;
    private ImageButton backToCook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciept_layout);
        Intent intent = getIntent();
        this.title = intent.getStringExtra("title");
        this.FileName = intent.getStringExtra("file_name");

        this.RecieptTitle = findViewById(R.id.RecieptTitle);
        this.RecieptDescription = findViewById(R.id.RecieptDescription);
        this.EditReciept = findViewById(R.id.EditReciept);
        this.backToCook = findViewById(R.id.backToCook);

        EditReciept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                            RecieptActivity.this.openFileOutput(FileName, MODE_PRIVATE)));
                    out.write(RecieptDescription.getText().toString());
                    out.close();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Saved), Toast.LENGTH_SHORT).show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        backToCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String description = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    RecieptActivity.this.openFileInput(FileName)));
            String line;

            while ((line = in.readLine()) != null) {
                //filename;title;
                description += line+"\n";
            }

        } catch (FileNotFoundException e) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        RecieptActivity.this.openFileOutput(FileName, MODE_PRIVATE)));
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
        }





        EditReciept.setOnTouchListener(TouchListener.touchListener);
        backToCook.setOnTouchListener(TouchListener.touchListener);
        this.RecieptTitle.setText(title);
        this.RecieptDescription.setText(description);
    }
}
