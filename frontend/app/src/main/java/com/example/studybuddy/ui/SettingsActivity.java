package com.example.studybuddy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import com.example.studybuddy.R;

public class SettingsActivity extends AppCompatActivity {

    private EditText mNameField, mUniversityField, mMajorField;
    private ProgressBar spinner;
    private Button mConfirmButton;
    private ImageButton mBack;
    private Spinner mInterestsSpinner, mPetsSpinner;
    private String userId, name, university, major, interests = "", pets = "";
    private Uri resultUrl;
    private ImageView mProfileImagePic;
    ActivityResultLauncher<Intent> imagePickerActivityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fields
        mNameField = findViewById(R.id.name);
        mUniversityField = findViewById(R.id.university);
        mMajorField = findViewById(R.id.major);
        mInterestsSpinner = findViewById(R.id.spinner_interests_settings);
        mPetsSpinner = findViewById(R.id.spinner_pets_settings);
        mConfirmButton = findViewById(R.id.confirm);
        mBack = findViewById(R.id.settingsBack); // ✅ Corrected ID
        mProfileImagePic = findViewById(R.id.profileImage);

        // Load existing data
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mNameField.setText(prefs.getString("name", ""));
        mUniversityField.setText(prefs.getString("university", ""));
        mMajorField.setText(prefs.getString("major", ""));
        selectSpinnerValue(mInterestsSpinner, prefs.getString("interests", ""));
        selectSpinnerValue(mPetsSpinner, prefs.getString("pets", ""));

        // Spinner selection listeners
        mInterestsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                interests = parent.getItemAtPosition(position).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                interests = "";
            }
        });

        mPetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pets = parent.getItemAtPosition(position).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                pets = "";
            }
        });

        // Image picker setup
        imagePickerActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    resultUrl = data.getData();
                    mProfileImagePic.setImageURI(resultUrl);
                }
            }
        });

        // Back button
        mBack.setOnClickListener(v -> finish());

        // Confirm button
        mConfirmButton.setOnClickListener(v -> {
            name = mNameField.getText().toString().trim();
            university = mUniversityField.getText().toString().trim();
            major = mMajorField.getText().toString().trim();

            if (name.isEmpty() || university.isEmpty() || major.isEmpty() || interests.isEmpty() || pets.isEmpty()) {
                Toast.makeText(SettingsActivity.this, "Please fill in all information", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit();
                editor.putString("name", name);
                editor.putString("university", university);
                editor.putString("major", major);
                editor.putString("interests", interests);
                editor.putString("pets", pets);
                editor.apply();
                Toast.makeText(SettingsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
            }
        });

        // Profile image click to open gallery
        mProfileImagePic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerActivityResult.launch(intent);
        });
    }


    private void selectSpinnerValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}