package com.example.prototype_design_mad;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetUpActivity extends AppCompatActivity {

    ImageButton mSetUpImage;
    EditText mName, mAbout;
    Button mSubmitButton;

    Uri mImageUri = null;

     private  static  final int GALLERY_REQUEST = 1;

     DatabaseReference mDatabaseUsers;
     StorageReference mStorageImage;

     ProgressDialog progressDialog;

     FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        progressDialog = new ProgressDialog(this);

        mSetUpImage =(ImageButton) findViewById(R.id.SetUpProfile_image);
        mName = (EditText) findViewById(R.id.setUp_name);
        mAbout =(EditText) findViewById(R.id.setUp_About_id);
        mSubmitButton = (Button) findViewById(R.id.submit_setUp);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetUpAccount();
            }
        });

        mSetUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
    }

    private void startSetUpAccount() {

        final String name = mName.getText().toString().trim();
        final String about = mAbout.getText().toString().trim();

        final String user_id = mAuth.getCurrentUser().getUid();

        if(!TextUtils.isEmpty(name) && mImageUri != null){

            progressDialog.setMessage("Finishing Setup....");
            progressDialog.show();

                StorageReference filePath = mStorageImage.child(mImageUri.getLastPathSegment());

                filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                        mDatabaseUsers.child(user_id).child("name").setValue(name);
                        mDatabaseUsers.child(user_id).child("about").setValue(about);
                        mDatabaseUsers.child(user_id).child("image").setValue(downloadUrl.toString());

                        progressDialog.dismiss();

                        Intent MainIntent = new Intent(SetUpActivity.this, MainActivity.class);
                        //user cannot go back(IT19008110)
                        MainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(MainIntent);

                        // newPost.child("image").setValue(downloadUrl.toString());
                      //  Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    }
                });



        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                mImageUri = result.getUri();

                mSetUpImage.setImageURI(mImageUri);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }
}