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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



public class crearerecipesingleview extends AppCompatActivity {

     String mpost_key = null;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private ImageView msigImage;
    private TextView msigTitle;
    private TextView msigDescription;
    private TextView msigIngredents;
    private TextView msigProcedure;
    private Button msigDeleteButton;
    private Button msigUpdateButton;
//    recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearerecipesingleview);


        mpost_key = getIntent().getStringExtra("viewID");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipe");
        mAuth = FirebaseAuth.getInstance();



        msigTitle = (TextView) findViewById(R.id.mcreatevtitle);
        msigDescription = (TextView) findViewById(R.id.mcreatvdescription);
        msigIngredents = (TextView) findViewById(R.id.mcreatvingredients);
        msigProcedure = (TextView) findViewById(R.id.mcratevprocedure);
        msigImage = (ImageView) findViewById(R.id.mvimage);

        msigDeleteButton = (Button) findViewById(R.id.myvdeletebutton);
        msigUpdateButton = (Button) findViewById(R.id.myeditbutton2);


        mDatabase.child(mpost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                recipe recipe = dataSnapshot.getValue(recipe.class);
//
//                String mytitle = recipe.getTitle();
//                String myingredent = recipe.getDescription();
//                String myprocedure = recipe.getProcedure();
//                String myDescription = recipe.getDescription();
//                String myimage = recipe.getImage();
//                String myuserid = recipe.getUid();


                String mytitle = (String) dataSnapshot.child("Title").getValue().toString();
                String myingredent = (String) dataSnapshot.child("Ingredients").getValue().toString();
                String myprocedure = (String) dataSnapshot.child("Procedure").getValue().toString();
                String myDescription = (String) dataSnapshot.child("Description").getValue().toString();
                String myimage = (String) dataSnapshot.child("Image").getValue().toString();
                String myuserid = (String) dataSnapshot.child("uid").getValue();

                msigTitle.setText(mytitle);
                msigDescription.setText(myDescription);
                msigIngredents.setText(myingredent);
                msigProcedure.setText(myprocedure);

                Picasso.get().load(myimage).into(msigImage);

                if (mAuth.getCurrentUser().getUid().equals(myuserid)) {
                    msigDeleteButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
        msigDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mpost_key).removeValue();

                Intent mainIntent = new Intent(crearerecipesingleview.this, mycreaterecipe.class);
                startActivity(mainIntent);
            }

        });
        msigUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase =FirebaseDatabase.getInstance().getReference();
                mDatabase.child("recipe").child(mpost_key).child("Title").setValue(msigTitle.getText().toString());
                mDatabase.child("recipe").child(mpost_key).child("Description").setValue(msigDescription.getText().toString());
                mDatabase.child("recipe").child(mpost_key).child("Ingredients").setValue(msigIngredents.getText().toString());
                mDatabase.child("recipe").child(mpost_key).child("Procedure").setValue(msigProcedure.getText().toString());
//                mDatabase.child("recipe").child("uid").child("Ingredients").setValue(msigTitle.getText().toString().trim())
           Toast.makeText(getApplicationContext(),"update success",Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(crearerecipesingleview.this, mycreaterecipe.class);
                startActivity(mIntent);
            }
        });


    }


}
