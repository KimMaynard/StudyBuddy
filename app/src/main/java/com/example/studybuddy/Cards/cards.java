package com.example.studybuddy.Cards;

import java.io.Serializable;

public class cards implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String name;
    private String university;
    private String major;
    private String interests;
    private String pets;
    private String profileImageUrl;


    public cards(String userId, String name, String university, String major, String interests, String pets, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.university = university;
        this.major = major;
        this.interests = interests;
        this.pets = pets;
        this.profileImageUrl = profileImageUrl;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
