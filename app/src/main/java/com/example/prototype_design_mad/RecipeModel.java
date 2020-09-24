package com.example.prototype_design_mad;

public class RecipeModel {
    String Image;
    String Title;
    String Ingredient;
    String Description;
    String Steps;

    public RecipeModel() {
    }

    public RecipeModel(String image, String title, String ingredient, String description, String steps) {
        Image = image;
        Title = title;
        Ingredient = ingredient;
        Description = description;
        Steps = steps;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSteps() {
        return Steps;
    }

    public void setSteps(String steps) {
        Steps = steps;
    }
}
