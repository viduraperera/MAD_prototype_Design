package com.example.prototype_design_mad;

public class Review {

    String date, review, time, username;

    public Review() {
    }

    public Review(String date, String review, String time, String username) {
        this.date = date;
        this.review = review;
        this.time = time;
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
