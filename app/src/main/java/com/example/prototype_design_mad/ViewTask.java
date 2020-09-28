package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype_design_mad.Model.Recipe_ToDoList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewTask extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView recipeView,dateView,timeView,ingredientsView;
    private String recipeID = null;
    DatabaseReference dbRef;
    Button btnDelete;
    Button btnUpdate;

//    private void clearControls () {
//
//        recipeView.setText("");
//        dateView.setText("");
//        timeView.setText("");
//        ingredientsView.setText("");
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

    recipeID = getIntent().getStringExtra("rID");
    dbRef=FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");


     recipeImage = (ImageView) findViewById(R.id.foodimage1);
     recipeView = (TextView) findViewById(R.id.viewRecipe);
     dateView = (TextView) findViewById(R.id.viewDate);
     timeView = (TextView) findViewById(R.id.viewTime);
     ingredientsView = (TextView) findViewById(R.id.viewIngredients);
     btnDelete = (Button)findViewById(R.id.Deletebutton);
     btnUpdate=(Button)findViewById(R.id.Editbutton);

//     getRecipeDetails(recipeID);

//    }

//    private void getRecipeDetails(final String recipeID) {

//        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");

        dbRef.child(recipeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Recipe_ToDoList recipes = snapshot.getValue(Recipe_ToDoList.class);

                    recipeView.setText(recipes.getRecipe());
                    dateView.setText(recipes.getDate());
                    timeView.setText(recipes.getTime());
                    ingredientsView.setText(recipes.getIngredients());
                    Picasso.get().load(recipes.getImage()).into(recipeImage);
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbRef.child(recipeID).removeValue();

                Intent mintent = new Intent(ViewTask.this,ToDoList.class);
                startActivity(mintent);



            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ViewTask.this, UpdateTask.class);
                startActivity(intent);


            }
        });
    }

    }

