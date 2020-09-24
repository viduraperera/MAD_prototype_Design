package com.example.prototype_design_mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewSingleRecipe extends AppCompatActivity {

    TextView title, ingredient, description, steps;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_recipe);
        title = findViewById(R.id.display_title);
        ingredient = findViewById(R.id.display_ingredients);
        description = findViewById(R.id.display_description);
        steps = findViewById(R.id.display_steps);
        imageView = findViewById(R.id.display_image);

        title.setText(getIntent().getStringExtra("Title"));
        ingredient.setText(getIntent().getStringExtra("Ingredient"));
        description.setText(getIntent().getStringExtra("Description"));
        steps.setText(getIntent().getStringExtra("Steps"));


    }
}