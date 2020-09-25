package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddingRecipes extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton;
    EditText title, ingredient, description, steps;
    Button btn_insert;
    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_recipes);

        title = findViewById(R.id.add_title);
        ingredient = findViewById(R.id.add_ingredients);
        description = findViewById(R.id.add_description);
        steps = findViewById(R.id.add_steps);
        imageButton = findViewById(R.id.add_image);
        btn_insert = findViewById(R.id.add_submit);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Recipes");
        mStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK){
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        }

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    add_recipe();
            }
        });
    }

    private void add_recipe(){
        final String on_title = title.getText().toString().trim();
        final String on_ingredient = ingredient.getText().toString().trim();
        final String on_description = description.getText().toString().trim();
        final String on_steps = steps.getText().toString().trim();

        if(!(on_title.isEmpty() && on_steps.isEmpty() && on_description.isEmpty() && on_ingredient.isEmpty() && imageUrl != null)){
            progressDialog.setTitle("uploading........");
            progressDialog.show();

            StorageReference filepath = mStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
            filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String t = task.getResult().toString();

                            DatabaseReference newPost = mRef.push();

                            newPost.child("Title").setValue(on_title);
                            newPost.child("Ingredient").setValue(on_ingredient);
                            newPost.child("Description").setValue(on_description);
                            newPost.child("Steps").setValue(on_steps);
                            newPost.child("Image").setValue(task.getResult().toString());
                            progressDialog.dismiss();

                            startActivity(new Intent(AddingRecipes.this, MainActivity.class));

                        }
                    });
                }
            });
        }
    }
}





















