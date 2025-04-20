package com.example.studybuddy.retrofitNetwork;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Wrapper around Retrofit for StudyBuddyService
public class BackendClient {
    private static final String BASE_URL = "http://10.0.2.2:8081/"; // KTor backend server runs on port 8081
    private static BackendClient instance;
    private final StudyBuddyService service;

    private BackendClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(StudyBuddyService.class);
    }

    // Returns Retrofit client instance
    public static synchronized BackendClient getInstance() {
        if (instance == null) {
            instance = new BackendClient();
        }
        return instance;
    }

    // Accesses API interface
    public StudyBuddyService getService() {
        return service;
    }
}
