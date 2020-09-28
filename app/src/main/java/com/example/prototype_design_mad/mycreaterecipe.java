package com.example.prototype_design_mad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class mycreaterecipe extends AppCompatActivity {

    private RecyclerView mycreatelist;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycreaterecipe);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipe");

        mycreatelist = (RecyclerView) findViewById(R.id.createlist);
        mycreatelist.setHasFixedSize(true);
        mycreatelist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<recipe, MyCreateRecipeHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<recipe, MyCreateRecipeHolder>(
            recipe.class,
            R.layout.mycardlist,
            MyCreateRecipeHolder.class,
                mDatabase

                ){
            @Override
            protected void populateViewHolder(MyCreateRecipeHolder myCreateRecipeHolder, recipe recipe, int i) {

                final String post_key = getRef(i).getKey();

                myCreateRecipeHolder.setTitle(recipe.getTitle());
                myCreateRecipeHolder.setDescription(recipe.getDescription());
                myCreateRecipeHolder.setIngredients(recipe.getIngredients());
                myCreateRecipeHolder.setProcedure(recipe.getProcedure());
                myCreateRecipeHolder.setImage(getApplicationContext(),recipe.getImage());

                myCreateRecipeHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//
//                        Text(mycreaterecipe.this,post_key, Toast.LENGTH_SHORT).show();
                        Intent MyViewIntent = new Intent(mycreaterecipe.this, crearerecipesingleview.class);
                        MyViewIntent.putExtra("viewID",post_key);
                        startActivity(MyViewIntent);
                    }
                });
            }
        };
        mycreatelist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MyCreateRecipeHolder extends RecyclerView.ViewHolder{
            View mView;
        public MyCreateRecipeHolder(@NonNull View itemView) {
            super(itemView);
            mView =itemView;

        }
        public void setTitle(String title){
            TextView myTitle = (TextView) mView.findViewById(R.id.mylisttitle);
            myTitle.setText(title);

        }
        public void setDescription(String description){
            TextView myDescription = (TextView) mView.findViewById(R.id.mylistdes);
            myDescription.setText(description);

        }
        public void setIngredients(String ingredients){
            TextView myIngredients = (TextView) mView.findViewById(R.id.mylistingredients);
            myIngredients.setText(ingredients);

        }
        public void setProcedure(String procedure){
            TextView myProcedure = (TextView) mView.findViewById(R.id.mylistprocedure);
            myProcedure.setText(procedure);

        }

        public void setImage(Context ctx, String image){
            ImageView myImage = (ImageView) mView.findViewById(R.id.mylistImage);
            Picasso.get().load(image).into(myImage);
        }

    }
}