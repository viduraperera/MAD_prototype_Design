package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class TipDetailedView extends AppCompatActivity
{
    private String tip_ID = null;

    private DatabaseReference tips_DB;

    private FirebaseAuth mAuth;

    private ImageView postImage;
    private TextView postCaption;
    private TextView postCategory;
    private TextView postDescription;
    private TextView postUser;

    private Button postUpdateBtn;
    private Button postRemoveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detailed_view);

        tip_ID = getIntent().getStringExtra("viewID");

        tips_DB = FirebaseDatabase.getInstance().getReference().child("Tip");

        mAuth = FirebaseAuth.getInstance();

        /*create string to get tip ID*/
        final String tip_ID = getIntent().getExtras().getString("tip_id");

        postImage = (ImageView) findViewById(R.id.post_Tip_Image);
        postCaption = (TextView) findViewById(R.id.post_Tip_Caption);
        postCategory = (TextView) findViewById(R.id.post_Tip_Category);
        postDescription = (TextView) findViewById(R.id.post_Tip_Description);
        postUser = (TextView) findViewById(R.id.post_Tip_User);

        postRemoveBtn = (Button) findViewById(R.id.remove_btn);
        postUpdateBtn = (Button) findViewById(R.id.update_btn);

        //Toast.makeText(TipDetailedView.this, tip_ID, Toast.LENGTH_LONG).show();

        tips_DB.child(tip_ID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String post_Caption = (String) dataSnapshot.child("Caption").getValue();
                String post_Category = (String) dataSnapshot.child("Category").getValue();
                String post_Description = (String) dataSnapshot.child("Description").getValue();
                String post_Image = (String) dataSnapshot.child("Tip_Image").getValue();
                String post_User = (String) dataSnapshot.child("username").getValue();

                postCaption.setText(post_Caption);
                postCategory.setText(post_Category);
                postDescription.setText(post_Description);
                postUser.setText(post_User);

                Picasso.get().load(post_Image).into(postImage);

                if (mAuth.getCurrentUser().getUid().equals(post_User))
                {
                    postRemoveBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        postRemoveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tips_DB.child(tip_ID).removeValue();

                Intent mainIntent = new Intent(TipDetailedView.this, my_tips_page.class);
                startActivity(mainIntent);
            }
        });

        postUpdateBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                tips_DB =FirebaseDatabase.getInstance().getReference();
                tips_DB.child("Tip").child(tip_ID).child("Caption").setValue(postCaption.getText().toString());
                tips_DB.child("Tip").child(tip_ID).child("Category").setValue(postCategory.getText().toString());
                tips_DB.child("Tip").child(tip_ID).child("username").setValue(postUser.getText().toString());
                tips_DB.child("Tip").child(tip_ID).child("Description").setValue(postDescription.getText().toString());

                Toast.makeText(getApplicationContext(),"Successfully Updated!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TipDetailedView.this, my_tips_page.class);
                startActivity(intent);
            }
        });

    }
}