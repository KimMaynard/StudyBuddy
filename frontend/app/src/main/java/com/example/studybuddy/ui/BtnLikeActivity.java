package com.example.studybuddy.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.studybuddy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BtnLikeActivity extends AppCompatActivity {

    private static final String TAG = "BtnLikeActivity";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext = BtnLikeActivity.this;
    private ImageView like;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_btn_like);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupTopNavigationView();
        like = findViewById(R.id.like);

        Intent intent = getIntent();
        String profileUrl = intent.getStringExtra("url");


        switch (profileUrl) {
            case "default" :
                Glide.with(mContext).load(R.drawable.profile).into(like);
            default:
                Glide.with(mContext).load(profileUrl).into(like);
                break;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent mainIntent = new Intent(BtnLikeActivity.this, MainActivity.class);
                startActivity(mainIntent);

            }
        }).start();
    }


    private void setupTopNavigationView() {
        BottomNavigationView tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    public void LikeBtn(View view) {
    }
}