package com.example.myfridge.controllers;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.myfridge.R;
import com.example.myfridge.templates.Reciept;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CookBook extends AppCompatActivity {
    private LinearLayout cook_layout;
    private SearchView cook_search;
    private ImageButton backToFridgeActivity;
    private Button addNewReciept;
    private SearchView input;
    private int lastIndex = 1000;
    private static final String BOOK_FILE_NAME = "cook_book_info.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_book);
        cook_layout = findViewById(R.id.reciepts);
        cook_search = findViewById(R.id.cook_search);
        backToFridgeActivity = findViewById(R.id.backToSettings);
        addNewReciept = findViewById(R.id.EditReciept);
        input = findViewById(R.id.cook_search);
        ImageButton backToMain= findViewById(R.id.backToSettings);

        backToMain.setOnTouchListener(TouchListener.touchListener);
        addNewReciept.setOnTouchListener(TouchListener.touchListener);
        addNewReciept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        input.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for (int i = 0; i < cook_layout.getChildCount(); i++) {
                    Reciept fd = ((Reciept) cook_layout.getChildAt(i));
                    if (!fd.getTitle().contains(newText.toLowerCase())) fd.setVisibility(View.GONE);
                    else fd.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    CookBook.this.openFileInput(BOOK_FILE_NAME)));
            String line;
            while ((line = in.readLine()) != null) {
                //filename;title;
                String[] row = line.split(";");
                this.lastIndex = Integer.parseInt(row[0]);
                this.cook_layout.addView(new Reciept(this, row[1], row[0]));
            }

        } catch (FileNotFoundException e) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        CookBook.this.openFileOutput(BOOK_FILE_NAME, MODE_PRIVATE)));
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(CookBook.this);
        dialog.setOwnerActivity(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_activity);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fridge_back, null));
        TextView title = dialog.findViewById(R.id.drop_dialog_title);
        title.setText(getResources().getText(R.string.CookDialogAddReciept));
        EditText input_name = dialog.findViewById(R.id.dialog_input_quantity);
        Button OK = dialog.findViewById(R.id.dialog_add_food_accept);
        Button BACK = dialog.findViewById(R.id.dialog_add_food_reject);


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewCook(input_name.getText().toString());
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

    private void writeNewCook(String title) {
        String FileName = "" + (lastIndex++);

        Reciept r = new Reciept(this, title, FileName);
        cook_layout.addView(r);

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    CookBook.this.openFileOutput(BOOK_FILE_NAME, MODE_APPEND)));

            out.append(String.format("%s;%s;\n", FileName, title));
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
