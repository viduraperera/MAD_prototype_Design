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

import com.example.prototype_design_mad.Model.Recipe_ToDoList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class UpdateTask extends AppCompatActivity {

    EditText recipe, date, time, ingredients;
    String rID, curDate, curTime ;
    Button btnUpdate;
    Button btnEdit;
    DatabaseReference dbRef;
    Recipe_ToDoList Model;
    private String recipeID = null;
    //    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;
    ImageButton imageButton;
    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    private void clearControls () {

        recipe.setText("");
        date.setText("");
        time.setText("");
        ingredients.setText("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);


        recipeID = getIntent().getStringExtra("rID");
        dbRef = FirebaseDatabase.getInstance().getReference("Recipe_toDoList");


        imageButton = findViewById(R.id.imageView);
        recipe = findViewById(R.id.UpdateRecipe);
        date = findViewById(R.id.UpdateDate);
        time = findViewById(R.id.UpdateTime);
        ingredients = findViewById(R.id.UpdateIngredients);
        btnUpdate = findViewById(R.id.Editbutton);
        btnEdit =findViewById(R.id.updateBtn);
        Model = new Recipe_ToDoList();


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
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Code && resultCode == RESULT_OK) {
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);

        }
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(recipeID)) {
                    Model.setRecipe(recipe.getText().toString().trim());
                    Model.setDate(date.getText().toString().trim());
                    Model.setTime(time.getText().toString().trim());
                    Model.setIngredients(ingredients.getText().toString().trim());

                    dbRef = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");
                    dbRef.setValue(Model);
                    clearControls();
                    Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "No Source to Update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(UpdateTask.this, ToDoList.class);
//                startActivity(intent);
//    };


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
        new TimePickerDialog(UpdateTask.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
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
        new DatePickerDialog(UpdateTask.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }




}



