package com.example.prototype_design_mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class AllRecipeStart extends AppCompatActivity {

    ListView listView;
    FirebaseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipe_start);

        listView  = findViewById(R.id.recipe_list_view);

        Query query = FirebaseDatabase.getInstance().getReference().child("Recipes");
        FirebaseListOptions<RecipeModel> options = new FirebaseListOptions.Builder<RecipeModel>()
                .setLayout(R.layout.recipe)
                .setQuery(query, RecipeModel.class)
                .build();

        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView title = v.findViewById(R.id.recipe_title);
                TextView description = v.findViewById(R.id.recipe_description);
                ImageView imageView = v.findViewById(R.id.recipe_image);

                RecipeModel recipeModel = (RecipeModel) model;
                title.setText( "Title:  " + recipeModel.getTitle().toString());
                description.setText("Description:  "+ recipeModel.getDescription().toString());
                Picasso.get().load(recipeModel.getImage().toString()).into(imageView);

            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}