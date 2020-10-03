
package com.example.prototype_design_mad;
//
////
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.prototype_design_mad.Model.Recipe_ToDoList;
//import com.example.prototype_design_mad.ViewHolder.RecipeViewHolder;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.squareup.picasso.Picasso;
//
//public class ToDoList extends AppCompatActivity {
//
//    private DatabaseReference dbref;
//    private RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//
//    FloatingActionButton floatingActionButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_to_do_list);
//
//        dbref = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");
//        recyclerView = findViewById(R.id.recview);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//
//        floatingActionButton = findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), AddTask.class));
//
//            }
//        });
//
//}
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerAdapter<Recipe_ToDoList, MainActivity.RecipeViewHolder> adapter = new FirebaseRecyclerAdapter<Recipe_ToDoList, MainActivity.RecipeViewHolder>() {
//            @Override
//            protected void populateViewHolder(MainActivity.RecipeViewHolder recipeViewHolder, Recipe_ToDoList recipe_toDoList, int i) {
//
//                recipeViewHolder
//
//            }
//        }
//
//        FirebaseRecyclerOptions<Recipe_ToDoList> options =
//                new FirebaseRecyclerOptions.Builder<Recipe_ToDoList>()
//                        .setQuery(dbref, Recipe_ToDoList.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Recipe_ToDoList, RecipeViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Recipe_ToDoList, RecipeViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull final Recipe_ToDoList model) {
//
//                        holder.recipe.setText("Recipe: " + model.getRecipe());
//                        holder.date.setText("Date: " + model.getDate());
//                        holder.time.setText("Time: " + model.getTime());
//                        holder.ingredients.setText("Ingredients: " + model.getIngredients());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(ToDoList.this,ViewTask.class);
//                                intent.putExtra("rID", model.getId());
//                                startActivity(intent);
//                            }
//                        });
//
//                    }
//
//
//                    @NonNull
//                    @Override
//                    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_data, parent, false);
//                        RecipeViewHolder holder = new RecipeViewHolder(view);
//                        return holder;
//
//                    }
//
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//
//    }
//
//
//
//
//}
//
//
//
//
//
//public class ToDoList extends AppCompatActivity {
//package com.example.prototype_design_mad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype_design_mad.Model.Recipe_toDoList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ToDoList extends AppCompatActivity {

    private RecyclerView todolist;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseCurrentUser;

    private Query mQueryCurrentUser;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");

        String currentUserId = mAuth.getCurrentUser().getUid();

//        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");
        mQueryCurrentUser = mDatabaseCurrentUser.orderByChild("uid").equalTo(currentUserId);
        todolist = (RecyclerView) findViewById(R.id.recview);
        todolist.setHasFixedSize(true);
        todolist.setLayoutManager(new LinearLayoutManager(this));

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

        FirebaseRecyclerAdapter<Recipe_toDoList, CreateToDoListHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Recipe_toDoList, CreateToDoListHolder>(
                Recipe_toDoList.class,
                R.layout.retrieved_data,
                CreateToDoListHolder.class,
//                mDatabase
                mQueryCurrentUser

        ){
            @Override
            protected void populateViewHolder(CreateToDoListHolder createToDoListHolder, Recipe_toDoList Model, int i) {

                final String post_key = getRef(i).getKey();

                createToDoListHolder.setRecipe(Model.getRecipe());
                createToDoListHolder.setDate(Model.getDate());
                createToDoListHolder.setTime(Model.getTime());
                createToDoListHolder.setIngredients(Model.getIngredients());
                createToDoListHolder.setImage(getApplicationContext(),Model.getImage());

                createToDoListHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//
//                        Text(mycreaterecipe.this,post_key, Toast.LENGTH_SHORT).show();
                        Intent MyViewIntent = new Intent(ToDoList.this, ViewTask.class);
                        MyViewIntent.putExtra("viewID",post_key);
                        startActivity(MyViewIntent);
                    }
                });

            }

        };
        todolist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class CreateToDoListHolder extends RecyclerView.ViewHolder{
        View mView;
        public CreateToDoListHolder(@NonNull View itemView) {
            super(itemView);
            mView =itemView;

        }
        public void setRecipe(String recipe){
            TextView myTitle = (TextView) mView.findViewById(R.id.tvRecipe);
            myTitle.setText(recipe);

        }
        public void setDate(String date){
            TextView myDescription = (TextView) mView.findViewById(R.id.tvDate);
            myDescription.setText(date);

        }
        public void setTime(String time){
            TextView myIngredients = (TextView) mView.findViewById(R.id.tvTime);
            myIngredients.setText(time);

        }
        public void setIngredients(String ingredients){
            TextView myProcedure = (TextView) mView.findViewById(R.id.tvIngredients);
            myProcedure.setText(ingredients);

        }

        public void setImage(Context ctx, String image){
            ImageView myImage = (ImageView) mView.findViewById(R.id.foodimage);
            Picasso.get().load(image).into(myImage);
        }

    }
}