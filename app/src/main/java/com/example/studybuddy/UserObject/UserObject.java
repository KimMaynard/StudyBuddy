package com.example.studybuddy.UserObject;

import java.io.Serializable;

public class UserObject implements Serializable {
    private String name;
    private String university;
    private String major;
    private String interests;
    private String pets;

    private Boolean selected = false;

    public UserObject(String name) {
        this.name = name;
    }

    public UserObject(String university, String major, String interests, String pets) {
        this.university = university;
        this.major = major;
        this.interests = interests;
        this.pets = pets;
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
}
