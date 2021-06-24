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


public class Profile extends AppCompatActivity {

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ImageButton backBu, settingsBu;
    private Button saveProfile;
    private TextView surname, name, thirdname, email, password;
    private static final String PROP_USER_FILE_NAME = "user_info.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        surname = findViewById(R.id.editTextTextPersonName);
        name = findViewById(R.id.editTextTextPersonName2);
        thirdname = findViewById(R.id.editTextTextPersonName3);
        email = findViewById(R.id.editTextTextPersonName4);
        password = findViewById(R.id.editTextTextPersonName5);
        backBu = findViewById(R.id.backToSettings);
        saveProfile = findViewById(R.id.EditReciept);


        settingsBu = findViewById(R.id.settings);
        backBu = findViewById(R.id.backToCook);

        backBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        settingsBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Settings.class));
            }
        });


        settingsBu.setOnTouchListener(TouchListener.touchListener);
        backBu.setOnTouchListener(TouchListener.touchListener);
        saveProfile.setOnTouchListener(TouchListener.touchListener);

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                            Profile.this.openFileOutput(PROP_USER_FILE_NAME, MODE_PRIVATE)));
                    out.write(String.format("%s;%s;%s;%s;%s;",
                            surname.getText(),
                            name.getText(),
                            thirdname.getText(),
                            email.getText(),
                            password.getText()));
                    out.close();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.SavedProp), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this,"Hello Javatpoint",Toast.LENGTH_SHORT).show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //загружаем картинку профиля
        //ImageView imageView = findViewById(R.id.profilePic);
        //imageView.setImageURI(Uri.parse("https://yandex.ru/images/search?text=%D0%BA%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%BA%D0%B0%20%D0%BA%D0%BE%D1%82%D0%B0%20vekmnziyfz&isize=small&from=tabbar&pos=6&img_url=https%3A%2F%2Fwww.pinclipart.com%2Fpicdir%2Fbig%2F0-3793_free-cartoon-cat-vector-clip-art-cartoon-cat.png&rpt=simage"));
        backBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        try {
            BufferedReader food_in = new BufferedReader(new InputStreamReader(
                    Profile.this.openFileInput(PROP_USER_FILE_NAME)));
            String line;
            while ((line = food_in.readLine()) != null) {
                String[] row = line.split(";");
                surname.setText(row[0]);
                name.setText(row[1]);
                thirdname.setText(row[2]);
                email.setText(row[3]);
                password.setText(row[4]);
            }

        } catch (FileNotFoundException e) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        Profile.this.openFileOutput(PROP_USER_FILE_NAME, MODE_PRIVATE)));
                out.write(";;;;;");
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
        }
    }
}