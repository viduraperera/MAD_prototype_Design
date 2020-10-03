package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewList;
    FirebaseRecyclerAdapter<recipe, RecipeViewHolder> firebaseRecyclerAdapter;

    DatabaseReference mDatabase;
    DatabaseReference mDatabaseLike;
    DatabaseReference mDatabaseUsers;
    FloatingActionButton addCreate;

    boolean mProcessLike = false;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCreate = findViewById(R.id.addrecipe);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser() == null){

                        Intent loginIntent = new Intent(MainActivity.this, LoginPage.class);
                        //user cannot go back(IT19008110)
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);

                    }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipe");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        mDatabaseLike.keepSynced(true);

        recyclerViewList = (RecyclerView) findViewById(R.id.list_recipe);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        checkUserExist();

        addCreate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), createrecipe1.class));

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<recipe, RecipeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<recipe, RecipeViewHolder>(
                recipe.class,
                R.layout.single_design_recipe,
                RecipeViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(RecipeViewHolder recipeViewHolder, recipe recipeModel, int i) {

                final String post_key = getRef(i).getKey();

                recipeViewHolder.setTitle(recipeModel.getTitle());
                recipeViewHolder.setDec(recipeModel.getDescription());
                recipeViewHolder.setImage(getApplicationContext(), recipeModel.getImage());
                recipeViewHolder.setUserName(recipeModel.getUsername());

                recipeViewHolder.setLikeBtn(post_key);


                recipeViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SingleViewActivity.class);
                        intent.putExtra("Recipe_id", post_key);
                        startActivity(intent);
                    }


                });

                recipeViewHolder.mReviewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent reviewIntent = new Intent(MainActivity.this, AddingReview.class);
                        reviewIntent.putExtra("Recipe_id", post_key);
                        startActivity(reviewIntent);

                    }
                });

                recipeViewHolder.mLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            mProcessLike = true;

                                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (mProcessLike) {


                                            if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                            } else {
                                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");

                                            }
                                            mProcessLike = false;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                    }
                });
            }
        };
        recyclerViewList.setAdapter(firebaseRecyclerAdapter);



    }
    private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(user_id)){

                    Intent setUpIntent = new Intent(MainActivity.this, SetUpActivity.class);
                    //user cannot go back(IT19008110)
                    setUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setUpIntent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageButton mLike, mReviewBtn;


        DatabaseReference mDataBaseLike;
        DatabaseReference mDatBaseUser;
        FirebaseAuth mAuth;
        int countLike;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            mLike = (ImageButton) mView.findViewById(R.id.recipe_like_btn);
            mReviewBtn = (ImageButton) mView.findViewById(R.id.reviewBtn);

            mDataBaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();

            mDataBaseLike.keepSynced(true);

        }
        public void setLikeBtn(final String post_key) {

            mDataBaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                        mLike.setImageResource(R.mipmap.red_button);
                    }
                    else{
                       mLike.setImageResource(R.mipmap.baseline_thumb_up_grey_36dp);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.single_title);
            post_title.setText(title);
        }
        public void setDec(String description){
            TextView post_description = (TextView) mView.findViewById(R.id.single_description);
            post_description.setText(description);
        }

        public void setUserName(String userName){
            TextView post_username = (TextView) mView.findViewById(R.id.user_name_view);
            post_username.setText(userName);
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
            startActivity(new Intent(MainActivity.this, createrecipe1.class));
        }

        if(item.getItemId() == R.id.log_out){

            logout();

        }
        if(item.getItemId() == R.id.user_account){

            startActivity(new Intent(MainActivity.this, userAccountPage.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();
    }

}
