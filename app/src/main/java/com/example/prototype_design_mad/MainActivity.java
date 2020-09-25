package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewList;
    FirebaseRecyclerAdapter<RecipeModel, RecipeViewHolder> firebaseRecyclerAdapter;
    DatabaseReference mDatabase;
    boolean mProcessLike = false;
    DatabaseReference mDatabaseLike;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Recipes");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        mDatabaseLike.keepSynced(true);

        recyclerViewList = (RecyclerView) findViewById(R.id.list_recipe);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<RecipeModel, RecipeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RecipeModel, RecipeViewHolder>(
                RecipeModel.class,
                R.layout.single_design_recipe,
                RecipeViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(RecipeViewHolder recipeViewHolder, RecipeModel recipeModel, int i) {

                final String post_key = getRef(i).getKey();

                recipeViewHolder.setTitle(recipeModel.getTitle());
                recipeViewHolder.setDec(recipeModel.getDescription());
                recipeViewHolder.setImage(getApplicationContext(), recipeModel.getImage());

                recipeViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       // Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SingleViewActivity.class);
                        intent.putExtra("Recipe_id", post_key);
                        startActivity(intent);
                    }


                });

                recipeViewHolder.mLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            mProcessLike = true;

                            if(mProcessLike){
                                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                                        }
                                        else {
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                    }
                });

            }
        };
        recyclerViewList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageButton mLike;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            mLike = (ImageButton) mView.findViewById(R.id.recipe_like_btn);


        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.single_title);
            post_title.setText(title);
        }
        public void setDec(String description){
            TextView post_description = (TextView) mView.findViewById(R.id.single_description);
            post_description.setText(description);
        }
        public void setImage(Context ctc, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.single__image);
            Picasso.get().load(image).into(post_image);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.add_item){
            startActivity(new Intent(MainActivity.this, AddingRecipes.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
