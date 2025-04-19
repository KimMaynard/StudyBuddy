package com.example.studybuddy.retrofitNetwork;

import com.example.studybuddy.models.UserDTO;
import com.example.studybuddy.models.StudentUserEntity; // your response model

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StudyBuddyService {
    // For POST /users - creates a new student user
    @POST("users")
    Call<StudentUserEntity> createUser(@Body UserDTO user);
}
