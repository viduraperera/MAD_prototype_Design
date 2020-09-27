package com.example.prototype_design_mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MyRecipeUpdate extends AppCompatActivity {
    private String mpost_key = null;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private ImageView umvimage;
    private TextView umcreatevtitle;
    private TextView umcreatvdescription;
    private TextView umcreatvingredients;
    private TextView umcratevprocedure;

    private Button umyeditbutton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_update);
    }
}