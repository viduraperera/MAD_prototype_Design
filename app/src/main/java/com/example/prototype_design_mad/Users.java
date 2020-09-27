package com.example.prototype_design_mad;

public class Users {

    String name;
    String image;
    String about;

    public Users() {
    }

    public Users(String name, String image, String about) {
        this.name = name;
        this.image = image;
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
