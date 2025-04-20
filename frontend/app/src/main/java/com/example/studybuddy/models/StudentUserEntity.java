package com.example.studybuddy.models;

// Model for the JSON that the backend returns after creating or getting the info for a student user
public class StudentUserEntity {
    public long id;
    public String firstName;
    public String middleName;
    public String lastName;

    // Stored in backend as base64, currently nullable - need front end convert image to base64 util later
    public byte[] profilePicture;
    public String username;
    public String email;
    public String areaCode;
    public String phoneNumber;
    public String password;
    public String currentDegree;
    public String seniority;
    public String preferredStudyStyle;
    public String dateCreated;  // Use ISO format

    // Constructor for Gson
    public StudentUserEntity() {}
}
