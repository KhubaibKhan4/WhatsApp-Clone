package com.example.whatsappclone.Models;

public class Users {

    String username, fullname, phone, gender, date, profile,userid;

    public Users(String username, String fullname, String phone, String gender, String date) {
        this.username = username;
        this.fullname = fullname;
        this.phone = phone;
        this.gender = gender;
        this.date = date;
    }

    public Users(String username, String fullname, String phone, String gender, String date, String profile,String userid) {
        this.username = username;
        this.fullname = fullname;
        this.phone = phone;
        this.gender = gender;
        this.date = date;
        this.profile = profile;
        this.userid = userid;
    }

    public Users(String phone) {
        this.phone = phone;
    }

    public Users() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
