package com.example.studybuddy.retrofitNetwork;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton wrapper around Retrofit for your StudyBuddyService.
 */
public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8081/"; // ktor backend server runs on port 8081
    private static RetrofitClient instance;
    private final StudyBuddyService service;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(StudyBuddyService.class);
    }

    // Returns Retrofit client instance
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // Accesses API interface
    public StudyBuddyService getService() {
        return service;
    }
}
