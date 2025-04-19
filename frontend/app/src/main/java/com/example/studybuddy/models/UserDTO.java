package com.example.studybuddy.models;

// Front-end Retrofit DTO for creating a student user
public class UserDTO {
    public String firstName;
    public String middleName;
    public String lastName;
    public byte[] profilePicture;
    public String username;
    public String email;
    public String areaCode;
    public String phoneNumber;
    public String password;
    public String currentDegree;
    public String seniority;
    public String preferredStudyStyle;

    public UserDTO(
            String firstName,
            String middleName,
            String lastName,
            byte[] profilePicture,
            String username,
            String email,
            String areaCode,
            String phoneNumber,
            String password,
            String currentDegree,
            String seniority,
            String preferredStudyStyle
    ) {
        this.firstName             = firstName;
        this.middleName            = middleName;
        this.lastName              = lastName;
        this.profilePicture        = profilePicture;
        this.username              = username;
        this.email                 = email;
        this.areaCode              = areaCode;
        this.phoneNumber           = phoneNumber;
        this.password              = password;
        this.currentDegree         = currentDegree;
        this.seniority             = seniority;
        this.preferredStudyStyle   = preferredStudyStyle;
    }
}
