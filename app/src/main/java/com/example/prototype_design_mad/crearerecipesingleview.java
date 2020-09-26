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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class crearerecipesingleview extends AppCompatActivity {

    private String mpost_key = null;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ImageView msigImage;
    private TextView msigTitle;
    private TextView msigDescription;
    private TextView msigIngredents;
    private TextView msigProcedure;
    private Button msigDeleteButton;
    private Button msigUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearerecipesingleview);

        mpost_key = getIntent().getExtras().getString("viewID");

//        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipe");

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
                String mytitle = (String) dataSnapshot.child("Title").getValue();
                String myingredent =(String) dataSnapshot.child("Ingredients").getValue();
                String myprocedure =(String) dataSnapshot.child("Procedure").getValue();
                String myDescription = (String) dataSnapshot.child("Description").getValue();
                String myimage = (String) dataSnapshot.child("Image").getValue();
//                String myuserid = (String) dataSnapshot.child("uid").getValue();

                msigTitle.setText(mytitle);
                msigDescription.setText(myDescription);
                msigIngredents.setText(myingredent);
                msigProcedure.setText(myprocedure);

                Picasso.get().load(myimage).into(msigImage);

//                if(mAuth.getCurrentUser().getUid().equals(post_uid){
//                    msigDeleteButton.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        msigDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mDatabase.child(mpost_key).removeValue();

               Intent mainIntent = new Intent(crearerecipesingleview.this, mycreaterecipe.class);
               startActivity(mainIntent);
            }

        });

//        msigUpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference upRef=FirebaseDatabase.getInstance().getReference().child("Student");
//                upRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.hasChild("recipe")){
//                            try{
//                                recipe.msigTitle(msigIngredents.getText().toString().trim());
//                                recipe.setName(txt2.getText().toString().trim());
//                                recipe.setAddress(txt3.getText().toString().trim());
//
//                                mDatabase=FirebaseDatabase.getInstance().getReference().child("recipe");
//                                mDatabase.setValue(mDatabase);
//                                clearControls();
//                                Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
//
//                            }catch(NumberFormatException e){
//                                Toast.makeText(MainActivity.this, "Invalid Contact Number", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });


    }
}