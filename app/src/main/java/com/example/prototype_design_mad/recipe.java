package com.example.prototype_design_mad;

import android.view.LayoutInflater;
import android.widget.ScrollView;

public class recipe {
    private String imageButton;
    private String Ingredient;
    private String Procedure;
    private String Title;
    private String Description;

    public String getImageButton() {
        return imageButton;
    }

    public void setImageButton(String imageButton) {
        this.imageButton = imageButton;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
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

    public recipe() {
    }
}
