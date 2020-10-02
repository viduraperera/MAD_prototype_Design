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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class userAccountPage extends AppCompatActivity {

    TextView name, about;
    ImageView proPic;
    Button pro_edit, pro_delete, see_pro_recipes;
    DatabaseReference mDatabase;
    FirebaseStorage mStorage;
    String userID = null;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_page);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth =FirebaseAuth.getInstance();

        name  = (TextView) findViewById(R.id.pro_name);
        about = (TextView)findViewById(R.id.pro_about);
        proPic = (ImageView)findViewById(R.id.pro_pic);
        pro_edit =(Button) findViewById(R.id.pro_edit);
        pro_delete = (Button)findViewById(R.id.pro_delete);
        see_pro_recipes = (Button)findViewById(R.id.pro_see_recipes);



        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        userID = user.getUid();

       mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String post_name = (String) dataSnapshot.child("name").getValue();
               String post_about = (String) dataSnapshot.child("about").getValue();
               String post_image = (String) dataSnapshot.child("image").getValue();

               name.setText(post_name);
               about.setText(post_about);

               Picasso.get().load(post_image).into(proPic);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       pro_edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mDatabase = FirebaseDatabase.getInstance().getReference();
               mDatabase.child("Users").child(userID).child("name").setValue(name.getText().toString());
               mDatabase.child("Users").child(userID).child("about").setValue(about.getText().toString());
               Toast.makeText(getApplicationContext(),"update success",Toast.LENGTH_SHORT).show();
               Intent updateIntent = new Intent(userAccountPage.this, MainActivity.class);
               startActivity(updateIntent);
           }
       });


    }
}