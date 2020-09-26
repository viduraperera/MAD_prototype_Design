package com.example.prototype_design_mad;

import android.view.LayoutInflater;
import android.widget.ScrollView;

public class recipe {
    private String Image;
    private String Ingredients;
    private String Procedure;
    private String Title;
    private String Description;
    private  String UserName;

    public recipe(String image, String ingredients, String procedure, String title, String description, String userName) {
        Image = image;
        Ingredients = ingredients;
        Procedure = procedure;
        Title = title;
        Description = description;
        UserName = userName;

    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredient(String ingredients) {
        Ingredients = ingredients;
    }

    public String getProcedure() {
        return Procedure;
    }

    public void setProcedure(String procedure) {
        Procedure = procedure;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public recipe() {
    }
}
