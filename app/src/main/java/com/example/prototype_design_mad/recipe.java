package com.example.prototype_design_mad;

public class recipe {
    private String Image;
    private String Ingredients;
    private String Procedure;
    private String Title;
    private String Description;
    private  String username;

    public recipe(String image, String ingredients, String procedure, String title, String description, String userName) {
        Image = image;
        Ingredients = ingredients;
        Procedure = procedure;
        Title = title;
        Description = description;
        this.username = userName;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public recipe() {
    }
}
