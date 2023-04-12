package com.example.whatsappclone.Models;

public class Stories {

    String username,profile,stories_image,date;

    public Stories() {
    }

    public Stories(String username, String profile, String stories_image, String date) {
        this.username = username;
        this.profile = profile;
        this.stories_image = stories_image;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getStories_image() {
        return stories_image;
    }

    public void setStories_image(String stories_image) {
        this.stories_image = stories_image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
