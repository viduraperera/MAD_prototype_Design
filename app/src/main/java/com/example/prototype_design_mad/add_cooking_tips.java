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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class add_cooking_tips extends AppCompatActivity
{

    private ImageButton add_image;

    private EditText caption_id;
    private EditText category_id;
    private EditText description_id;

    private Button submit_btn;

    private Uri imageUri;

    private static final int GALLERY_REQUEST =1;

    private StorageReference storage;
    private DatabaseReference db;

    private ProgressDialog progress_bar;

    /*firebase auth*/
    private FirebaseAuth mAuth;

    private FirebaseUser current_User;

    private DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cooking_tips);

        mAuth = FirebaseAuth.getInstance();

        current_User = mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference().child("Tip");

        dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(current_User.getUid());

        add_image = (ImageButton) findViewById(R.id.addimg);

        caption_id = (EditText) findViewById(R.id.captionid);
        category_id = (EditText) findViewById(R.id.categoryid);
        description_id = (EditText) findViewById(R.id.descriptionid);

        submit_btn = (Button) findViewById(R.id.submitid);

        progress_bar = new ProgressDialog(this);

        add_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startPosting();
            }
        });
    }

    private void startPosting()
    {
        final String caption_input = caption_id.getText().toString().trim();
        final String category_input = category_id.getText().toString().trim();
        final String description_input = description_id.getText().toString().trim();

        if (!TextUtils.isEmpty(caption_input) && !TextUtils.isEmpty(category_input) && !TextUtils.isEmpty(description_input))
        {
            progress_bar.setMessage("Posting Tip");
            progress_bar.show();

            StorageReference filepath = storage.child("Tip_Images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @Override
                        public void onComplete(@NonNull final Task<Uri> task)
                        {
                            String a = task.getResult().toString();

                            final DatabaseReference newTip = db.push();

                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

                            dbUser.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    newTip.child("Caption").setValue(caption_input);
                                    newTip.child("Category").setValue(category_input);
                                    newTip.child("Description").setValue(description_input);
                                    newTip.child("Tip_Image").setValue(task.getResult().toString());
                                    newTip.child("uid").setValue(current_User.getUid());
                                    newTip.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                startActivity(new Intent(add_cooking_tips.this, my_tips_page.class));
                                            }

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError)
                                {

                                }
                            });

                            progress_bar.dismiss();
                        }

                    });

                    startActivity(new Intent(add_cooking_tips.this, my_tips_page.class));

                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            imageUri = data.getData();

            add_image.setImageURI(imageUri);
        }
    }
}