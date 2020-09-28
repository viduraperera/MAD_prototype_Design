package com.example.prototype_design_mad.Model;

public class Recipe_ToDoList {

    private String recipe, date, time, ingredients, image,id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Recipe_ToDoList() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }




//    public String getPurl() {
//        return purl;
//    }
//
//    public void setPurl(String purl) {
//        this.purl = purl;
//    }
}