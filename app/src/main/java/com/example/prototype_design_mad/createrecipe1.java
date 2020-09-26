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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class createrecipe1 extends AppCompatActivity {
    EditText textingredients, textprocedure, texttitle, textdescription;
    Button btncreate;
    DatabaseReference dbRef;
    recipe recipe;
    FirebaseDatabase mmDatabase;
    FirebaseStorage mmStorage;
    ImageButton imageButton;
    ProgressDialog progressDialog;

    private static final int Gallery_Code = 1;
    Uri imageUrl = null;

//    private FirebaseAuth mAuth;
//    private FirebaseUser mCurrentUser;
//    private DatabaseReference mDatabaseUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createrecipe1);
//        mAuth = FirebaseAuth.getInstance();
//        mCurrentUser=mAuth.getCurrentUser();
//        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        dbRef = FirebaseDatabase.getInstance().getReference("recipe");

        textingredients = findViewById(R.id.textingredients);
        textprocedure = findViewById(R.id.textprocedure);
        texttitle = findViewById(R.id.texttitle);
        textdescription = findViewById(R.id.textdescription);
        btncreate = findViewById(R.id.btncreaterecipe);
        imageButton = findViewById(R.id.createvimage);
        recipe = new recipe();

        mmDatabase = FirebaseDatabase.getInstance();
        dbRef = mmDatabase.getReference().child("recipe");
        mmStorage = FirebaseStorage.getInstance();
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

        if (requestCode == Gallery_Code && resultCode == RESULT_OK) {
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        }

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String in = textingredients.getText().toString().trim();
                final String pro = textprocedure.getText().toString().trim();
                final String tit = texttitle.getText().toString().trim();
                final String des = textdescription.getText().toString().trim();

                if (TextUtils.isEmpty(in)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Ingredients", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pro)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Procedure", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(tit)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Title", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(des)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    StorageReference filepath = mmStorage.getReference().child("imagerecipe").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull final Task<Uri> task) {
                                     String t = task.getResult().toString();

                                    DatabaseReference newPost = dbRef.push();
                                    Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                    clearControls();
                                    newPost.child("Ingredients").setValue(in);
                                    newPost.child("Procedure").setValue(pro);
                                    newPost.child("Title").setValue(tit);
                                    newPost.child("Description").setValue(des);
                                    newPost.child("Image").setValue(task.getResult().toString());

//                                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                            newPost.child("uid").setValue(mCurrentUser.getUid());
//                                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful()){
//                                                        startActivity(new Intent(createrecipe1.this,mycreaterecipe.class));
//                                                    }
//                                                }
//                                            });
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });

                                    progressDialog.dismiss();
                                }
                            });
                        }

                    });
                }
            }
        });
    }
    private void clearControls () {
        textingredients.setText("");
        textprocedure.setText("");
        texttitle.setText("");
        textdescription.setText("");

    }
}
//
//
//
//    @Override
//    public void onComplete(@NonNull final Task<Uri> task) {
//        final String t = task.getResult().toString();
//
//        final DatabaseReference newPost = dbRef.push();
//        Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
//        clearControls();
//
//        mDatabaseUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                newPost.child("Ingredients").setValue(in);
//                newPost.child("Procedure").setValue(pro);
//                newPost.child("Title").setValue(tit);
//                newPost.child("Description").setValue(des);
//                newPost.child("Image").setValue(task.getResult().toString());
//                newPost.child("uid").setValue(mCurrentUser.getUid());
//                newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            startActivity(new Intent(createrecipe1.this,mycreaterecipe.class));
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


