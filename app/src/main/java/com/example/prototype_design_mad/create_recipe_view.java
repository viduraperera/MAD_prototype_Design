package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class create_recipe_view extends AppCompatActivity {

    EditText textingredients, textprocedure, texttitle, textdescription;
    Button btncreate;
    DatabaseReference dbRef;
    FirebaseDatabase mmDatabase;
    FirebaseStorage mmStorage;
    ImageButton imageButton;
    recipe recipe;
    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createrecipe1);

        imageButton = findViewById(R.id.createrecipeimage);
        textingredients = findViewById(R.id.textingredients);
        textprocedure = findViewById(R.id.textprocedure);
        texttitle = findViewById(R.id.texttitle);
        textdescription = findViewById(R.id.textdescription);
        btncreate = findViewById(R.id.btncreaterecipe);
        recipe = new recipe();

        mmDatabase = FirebaseDatabase.getInstance();
        dbRef = mmDatabase.getReference().child("recipe");

        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("recipe");
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    textingredients.setText(dataSnapshot.child("ingedients").getValue().toString());
                    textprocedure.setText(dataSnapshot.child("procedure").getValue().toString());
                    texttitle.setText(dataSnapshot.child("title").getValue().toString());
                    textdescription.setText(dataSnapshot.child("description").getValue().toString());
//                    imageButton.setImageURI(Uri );

                } else
                    Toast.makeText(getApplicationContext(), "no source to display", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}