package com.example.studybuddy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studybuddy.Cards.arrayAdapter;
import com.example.studybuddy.Cards.cards;
import com.example.studybuddy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String FIRST_START_KEY = "firstStart";
    private static final String TAG = "MainActivity";

    BottomNavigationView topNav;
    private SwipeFlingAdapterView flingContainer;
    private arrayAdapter arrayAdapter;
    private List<cards> rowItems;
    private ImageButton likeButton, dislikeButton, chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        boolean isFirstStart = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getBoolean(FIRST_START_KEY, true);

        setupTopNavigationView(isFirstStart);

        if (isFirstStart) {
            markFirstStartComplete();
        }

        setupCards();
        setupButtons();
    }

    private void setupTopNavigationView(boolean isFirstStart) {
        topNav = findViewById(R.id.topNavViewBar);

        if (topNav != null) {
            TopNavigationViewHelper.setupTopNavigationView(topNav);
            TopNavigationViewHelper.enableNavigation(this, topNav);

            if (topNav.getMenu().size() > 1) {
                topNav.getMenu().getItem(1).setChecked(true);
            } else {
                Log.e(TAG, "Menu does not have at least two items!");
            }
        } else {
            Log.e(TAG, "BottomNavigationView (topNavViewBar) not found!");
        }

        if (isFirstStart) {
            View profileView = findViewById(R.id.ic_profile);

            if (profileView != null) {
                showToolTipProfile(profileView);
            } else {
                Log.e(TAG, "Profile view with ID 'ic_profile' not found!");
            }
        }

        chatButton = findViewById(R.id.ai_chat_button);
        if (chatButton != null) {
            chatButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            });
        }

    }

    private void markFirstStartComplete() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(FIRST_START_KEY, false);
        editor.apply();
    }

    private void showToolTipProfile(View profileView) {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(profileView)
                .setPrimaryText("Upload your profile picture")
                .setSecondaryText("Click Confirm after uploading for the first time.")
                .show();
    }

    private void setupCards() {
        flingContainer = findViewById(R.id.frame);
        rowItems = new ArrayList<>();

        // Dummy Data - You can load from database later
        rowItems.add(new cards("1", "Alice", "Harvard", "Computer Science", "Coding, Music", "Dog", "https://placekitten.com/400/400"));
        rowItems.add(new cards("2", "Bob", "Stanford", "Mathematics", "Gaming, Reading", "Cat", "https://placekitten.com/401/401"));
        rowItems.add(new cards("3", "Charlie", "MIT", "Engineering", "Robotics", "Parrot", "https://placekitten.com/402/402"));

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);
        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                // Disliked
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                // Liked
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Load more users if needed
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        flingContainer.setOnItemClickListener((itemPosition, dataObject) -> {

        });
    }

    private void setupButtons() {
        likeButton = findViewById(R.id.likebtn);
        dislikeButton = findViewById(R.id.dislikebtn);

        likeButton.setOnClickListener(v -> {
            if (flingContainer.getChildCount() > 0) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        dislikeButton.setOnClickListener(v -> {
            if (flingContainer.getChildCount() > 0) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });
    }
}
