
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
                    protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe_ToDoList model) {

                        holder.recipe.setText("Recipe: " + model.getRecipe());
                        holder.date.setText("Date: " + model.getDate());
                        holder.time.setText("Time: " + model.getTime());
                        holder.ingredients.setText("Ingredients: " + model.getIngredients());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

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






//package com.example.prototype_design_mad;
//
////
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseListAdapter;
//import com.firebase.ui.database.FirebaseListOptions;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.squareup.picasso.Picasso;
//
//public class ToDoList extends AppCompatActivity {
//
//
//    ListView lv;
//    FirebaseListAdapter adapter;
////    FloatingActionButton floatingActionButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_to_do_list);
//
//        lv = (ListView) findViewById(R.id.listView);
//        Query query = FirebaseDatabase.getInstance().getReference().child("Model");
//
//        FirebaseListOptions<Model> options = new FirebaseListOptions.Builder<Model>()
//                .setLayout(R.layout.retrieved_data)
//                .setQuery(query,Model.class)
//                .build();
//
//        adapter = new FirebaseListAdapter(options) {
//            @Override
//            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
//
//                TextView recipe = v.findViewById(R.id.recipe);
//                TextView date = v.findViewById(R.id.date);
//                TextView time = v.findViewById(R.id.time);
//                TextView ingredients = v.findViewById(R.id.ingredients);
//                ImageView imageView = v.findViewById(R.id.imageView);
//
//                Model md = (Model) model;
//                recipe.setText("Recipe: " +md.getRecipe().toString());
//                date.setText("Date: " +md.getDate().toString());
//                time.setText("Time: " +md.getTime().toString());
//                ingredients.setText("Ingredients: " +md.getIngredients().toString());
//                Picasso.with(start.this).load(md.getImageButton().toString())
//            }
//        };
//        lv.setAdapter(adapter);
//
////        floatingActionButton = findViewById(R.id.fab);
////        floatingActionButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                startActivity(new Intent(getApplicationContext(), AddTask.class));
////            }
////        });
//    }
//
//
//    @Override
//    protected void onStart () {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop () {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//
//}
