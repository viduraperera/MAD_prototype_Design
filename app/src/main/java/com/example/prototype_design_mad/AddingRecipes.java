package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

    ImageButton imageButton;
    EditText title, ingredients, description, steps;
    Button submitBtn;
    private  static final int GALLERY_REQUEST = 1;
    Uri imageUrl = null;
    StorageReference mStorage;
    DatabaseReference mDatabaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_recipes);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Recipes");

        imageButton = (ImageButton) findViewById(R.id.add_image);
        title = (EditText) findViewById(R.id.add_title);
        ingredients = (EditText) findViewById(R.id.add_ingredients);
        description = (EditText) findViewById(R.id.add_description);
        steps = (EditText) findViewById(R.id.add_steps);
        submitBtn = (Button) findViewById(R.id.add_submit);

        progressDialog = new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallery_intent = new Intent(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                startActivityForResult(gallery_intent, GALLERY_REQUEST);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecipe();
            }
        });

    }

    private void addRecipe(){

        progressDialog.setMessage("adding Recipe.....");
        progressDialog.show();

        final String title_val = title.getText().toString();
        final String description_val = description.getText().toString();
        final String ingredient_val = ingredients.getText().toString();
        final String steps_val = steps.getText().toString();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val) && !TextUtils.isEmpty(ingredient_val) && !TextUtils.isEmpty(steps_val) && imageUrl != null){
            StorageReference filePath = mStorage.child("Recipe").child(imageUrl.getLastPathSegment());

            filePath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                    DatabaseReference newPost = mDatabaseReference.push();

                    newPost.child("Title").setValue(title_val);
                    newPost.child("Description").setValue(description_val);
                    newPost.child("Ingredient").setValue(ingredient_val);
                    newPost.child("Steps").setValue(steps_val);
                    newPost.child("image").setValue(downloadUrl.toString());

                    progressDialog.dismiss();

                    Intent intent = new Intent(AddingRecipes.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUrl = data.getData();

            imageButton.setImageURI(imageUrl);
        }
    }
}





















