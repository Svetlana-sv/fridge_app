package com.example.myfridge.controllers;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfridge.R;


public class Profile extends AppCompatActivity {

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ImageButton backBu,settingsBu;
    private Button saveProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        settingsBu = findViewById(R.id.settings);
        backBu = findViewById(R.id.back);
        saveProfile = findViewById(R.id.saveProfile);

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

        View.OnTouchListener myTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                switch (eventAction) {
                    case MotionEvent.ACTION_DOWN:
                        v.getBackground().setColorFilter(getResources().getColor(R.color.primaryBarColor), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getBackground().clearColorFilter();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return false;
            }
        };

        settingsBu.setOnTouchListener(myTouch);
        backBu.setOnTouchListener(myTouch);
        saveProfile.setOnTouchListener(myTouch);

        //загружаем картинку профиля
        //ImageView imageView = findViewById(R.id.profilePic);
        //imageView.setImageURI(Uri.parse("https://yandex.ru/images/search?text=%D0%BA%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%BA%D0%B0%20%D0%BA%D0%BE%D1%82%D0%B0%20vekmnziyfz&isize=small&from=tabbar&pos=6&img_url=https%3A%2F%2Fwww.pinclipart.com%2Fpicdir%2Fbig%2F0-3793_free-cartoon-cat-vector-clip-art-cartoon-cat.png&rpt=simage"));

    }
}