
package com.example.prototype_design_mad;

//
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype_design_mad.Model.Recipe_ToDoList;
import com.example.prototype_design_mad.ViewHolder.RecipeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ToDoList extends AppCompatActivity {

    private DatabaseReference dbref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        dbref = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");
        recyclerView = findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddTask.class));

            }
        });

}

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Recipe_ToDoList> options =
                new FirebaseRecyclerOptions.Builder<Recipe_ToDoList>()
                        .setQuery(dbref, Recipe_ToDoList.class)
                        .build();

        FirebaseRecyclerAdapter<Recipe_ToDoList, RecipeViewHolder> adapter =
                new FirebaseRecyclerAdapter<Recipe_ToDoList, RecipeViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull final Recipe_ToDoList model) {

                        holder.recipe.setText("Recipe: " + model.getRecipe());
                        holder.date.setText("Date: " + model.getDate());
                        holder.time.setText("Time: " + model.getTime());
                        holder.ingredients.setText("Ingredients: " + model.getIngredients());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ToDoList.this,ViewTask.class);
                                intent.putExtra("rID", model.getId());
                                startActivity(intent);
                            }
                        });

                    }


                    @NonNull
                    @Override
                    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_data, parent, false);
                        RecipeViewHolder holder = new RecipeViewHolder(view);
                        return holder;

                    }

                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }




}





