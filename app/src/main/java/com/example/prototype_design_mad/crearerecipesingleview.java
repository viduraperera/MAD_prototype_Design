package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    FloatingActionButton addBtnCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearerecipesingleview);


        mpost_key = getIntent().getStringExtra("viewID");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipe");

        mAuth = FirebaseAuth.getInstance();



        msigTitle = findViewById(R.id.mcreatevtitle);
        msigDescription =  findViewById(R.id.mcreatvdescription);
        msigIngredents =  findViewById(R.id.mcreatvingredients);
        msigProcedure =  findViewById(R.id.mcratevprocedure);
        msigImage =  findViewById(R.id.mvimage);

        addBtnCreate = findViewById(R.id.addbtnrecipe);


        msigDeleteButton =  findViewById(R.id.myvdeletebutton);
        msigUpdateButton = findViewById(R.id.myeditbutton2);

        addBtnCreate = findViewById(R.id.addbtnrecipe);
        addBtnCreate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), createrecipe1.class));

            }
        });

        mDatabase.child(mpost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String mytitle = (String) dataSnapshot.child("Title").getValue();
                String myingredent = (String) dataSnapshot.child("Ingredients").getValue();
                String myprocedure = (String) dataSnapshot.child("Procedure").getValue();
                String myDescription = (String) dataSnapshot.child("Description").getValue();
                String myimage = (String) dataSnapshot.child("Image").getValue();
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

        msigDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase =FirebaseDatabase.getInstance().getReference().child("recipe").child(mpost_key);
                mDatabase.removeValue();

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.onel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.homebtnn) {
            startActivity(new Intent(crearerecipesingleview.this, MainActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }


}
