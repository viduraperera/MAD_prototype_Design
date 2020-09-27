package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleViewActivity extends AppCompatActivity {

    String mPost_key = null;
    DatabaseReference mDatabase;
    ImageView imageView;
    TextView title, description, ingredient, step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipe");
         mPost_key = getIntent().getExtras().getString("Recipe_id");

         title = (TextView) findViewById(R.id.one_Title);
         description = (TextView) findViewById(R.id.one_Description);
         ingredient = (TextView) findViewById(R.id.one_Ingredient);
         step = (TextView)findViewById(R.id.one_Steps);
         imageView = (ImageView) findViewById(R.id.one_recipeImage);

       // Toast.makeText(SingleViewActivity.this, mPost_key, Toast.LENGTH_SHORT).show();

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("Title").getValue();
                String post_description = (String) dataSnapshot.child("Description").getValue();
                String post_ingredient = (String) dataSnapshot.child("Ingredients").getValue();
                String post_step = (String) dataSnapshot.child("Procedure").getValue();
                String post_image = (String) dataSnapshot.child("Image").getValue();

                title.setText(post_title);
                description.setText(post_description);
                ingredient.setText(post_ingredient);
                step.setText(post_step);

                Picasso.get().load(post_image).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}