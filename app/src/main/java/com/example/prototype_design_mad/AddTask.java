package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.prototype_design_mad.Model.Recipe_toDoList;
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

import java.util.Calendar;

public class AddTask extends AppCompatActivity {

    EditText recipe, date, time, ingredients;
    String curDate, curTime ;
    Button btnSave;
    DatabaseReference dbRef;
    Recipe_toDoList Model;
    //    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;

    ImageButton imageButton;
    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);



        mAuth = FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());



        dbRef = FirebaseDatabase.getInstance().getReference("Recipe_toDoList");


        imageButton = findViewById(R.id.imageView);
        recipe = findViewById(R.id.AddRecipe);
        date = findViewById(R.id.AddDate);
        time = findViewById(R.id.AddTime);
        ingredients = findViewById(R.id.AddIngredients);
        btnSave = findViewById(R.id.saveBtn);
        Model = new Recipe_toDoList();


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


        //date time picker
        date.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(date);
            }


        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(time);
            }
        });

    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Code && resultCode == RESULT_OK) {
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);

        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String reh = recipe.getText().toString().trim();
                final String dt = date.getText().toString().trim();
                final String tm = time.getText().toString().trim();
                final String in = ingredients.getText().toString().trim();


                if (TextUtils.isEmpty(reh)) {
                    Toast.makeText(getApplicationContext(), " Recipe", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dt)) {
                    Toast.makeText(getApplicationContext(), " Date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(tm)) {
                    Toast.makeText(getApplicationContext(), " Time", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(in)) {
                    Toast.makeText(getApplicationContext(), " Ingredients", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    StorageReference filepath = mStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull final Task<Uri> task) {
                                    final String t = task.getResult().toString();
                                    final DatabaseReference todo = dbRef.push();
                                    Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                    clearControls();

                                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
                                            todo.child("ingredients").setValue(in);
//
                                            todo.child("image").setValue(task.getResult().toString());
                                            todo.child("time").setValue(tm);
                                            todo.child("date").setValue(dt);
                                            todo.child("recipe").setValue(reh);
                                            todo.child("uid").setValue(mCurrentUser.getUid());
                                            todo.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        startActivity(new Intent(AddTask.this,ToDoList.class));
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
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

        recipe.setText("");
        date.setText("");
        time.setText("");
        ingredients.setText("");
    }


    //implement date and time picker
    private void showTimeDialog ( final EditText time){
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                android.icu.text.SimpleDateFormat simpleDateFormat = new android.icu.text.SimpleDateFormat("HH:mm");
                time.setText(simpleDateFormat.format(calendar.getTime()));

                //to id
                android.icu.text.SimpleDateFormat simpleDateFormat2 = new android.icu.text.SimpleDateFormat("HH:mm:ss a");
                curTime =  simpleDateFormat2.format(calendar.getTime());

            }
        };
        new TimePickerDialog(AddTask.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }
    private void showDateDialog ( final EditText date){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                android.icu.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                date.setText(simpleDateFormat.format(calendar.getTime()));

                //to id
                android.icu.text.SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                curDate = simpleDateFormat2.format(calendar.getTime());

            }
        };
        new DatePickerDialog(AddTask.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }




}


