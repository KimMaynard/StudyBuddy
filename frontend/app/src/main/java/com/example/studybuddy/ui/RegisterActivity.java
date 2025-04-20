package com.example.studybuddy.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studybuddy.R;
import com.example.studybuddy.models.UserDTO;
import com.example.studybuddy.models.StudentUserEntity;
import com.example.studybuddy.retrofitNetwork.BackendClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameEt, middleNameEt, lastNameEt;
    private EditText usernameEt, emailEt, passwordEt;
    private Button pickPhotoBtn, registerBtn;
    private TextView existingUserTv;

    // For pfp photo url if applicable later...
    private Uri profilePhotoUri;

    private final ActivityResultLauncher<String> photoPicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> profilePhotoUri = uri
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind views
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        firstNameEt    = findViewById(R.id.first_name);
        middleNameEt   = findViewById(R.id.middle_name);
        lastNameEt     = findViewById(R.id.last_name);
        usernameEt     = findViewById(R.id.username);
        emailEt        = findViewById(R.id.email);
        passwordEt     = findViewById(R.id.password);
        pickPhotoBtn   = findViewById(R.id.pick_photo);
        registerBtn    = findViewById(R.id.register);
        existingUserTv = findViewById(R.id.existing);

        // For pfp
        pickPhotoBtn.setOnClickListener(v -> photoPicker.launch("image/*"));

        // Create Account button when clicked call Retrofit
        registerBtn.setOnClickListener(v -> {
            String firstName = firstNameEt.getText().toString().trim();
            String middleName= middleNameEt.getText().toString().trim();
            String lastName  = lastNameEt.getText().toString().trim();
            String username  = usernameEt.getText().toString().trim();
            String email     = emailEt.getText().toString().trim();
            String password  = passwordEt.getText().toString().trim();

            // Requires listed attributes in conditional to be filled in
            if (firstName.isEmpty() || lastName.isEmpty() ||
                    username.isEmpty()  || email.isEmpty()    ||
                    password.isEmpty()) {
                Toast.makeText(this,
                        "Please fill in all required fields",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // Sets DTO (empty strings for uncollected fields)
            UserDTO dto = new UserDTO(
                    /*
                     * pfp is null, and other attribute fields are empty strings for now
                     * For now, the user will have to later in their user profile update their info
                     */
                    firstName,
                    middleName,
                    lastName,
                    /* profilePicture */ null,
                    username,
                    email,
                    /* areaCode */ "",
                    /* phoneNumber */ "",
                    password,
                    /* currentDegree */ "",
                    /* seniority */ "",
                    /* preferredStudyStyle */ ""
            );

            // Retrofit network call
            BackendClient
                    .getInstance()
                    .getService()
                    .createUser(dto)
                    .enqueue(new Callback<StudentUserEntity>() {
                        @Override
                        public void onResponse(
                                Call<StudentUserEntity> call,
                                Response<StudentUserEntity> response
                        ) {
                            if (response.isSuccessful()) {
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Registered!",
                                        Toast.LENGTH_SHORT
                                ).show();
                                finish();
                            } else {
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Error: " + response.code(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                        @Override
                        public void onFailure(
                                Call<StudentUserEntity> call,
                                Throwable t
                        ) {
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Network error",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });

        // For "Existing User? Please Login” - returns to the login screen
        existingUserTv.setOnClickListener(v -> finish());
    }
}
