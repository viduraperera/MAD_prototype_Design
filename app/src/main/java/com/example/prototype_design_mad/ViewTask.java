package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewTask extends AppCompatActivity {

    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    private ImageView recipeImage;
    private TextView recipeView,dateView,timeView,ingredientsView;
     String recipeID = null;

     Button btnDelete;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

    recipeID = getIntent().getStringExtra("viewID");

    dbRef = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList");

    mAuth = FirebaseAuth.getInstance();

     recipeImage = (ImageView) findViewById(R.id.foodimage1);
     recipeView = (TextView) findViewById(R.id.viewRecipe);
     dateView = (TextView) findViewById(R.id.viewDate);
     timeView = (TextView) findViewById(R.id.viewTime);
     ingredientsView = (TextView) findViewById(R.id.viewIngredients);

     btnDelete = (Button)findViewById(R.id.btnDelete);
     btnUpdate=(Button)findViewById(R.id.EditButton);


        dbRef.child(recipeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String trecipeView = (String) snapshot.child("recipe").getValue();
                String tdateView = (String) snapshot.child("date").getValue();
                String ttimeView = (String) snapshot.child("time").getValue();
                String ttodolistimage = (String) snapshot.child("image").getValue();
                String tingredientsView = (String) snapshot.child("ingredients").getValue();
                String myuserid = (String) snapshot.child("uid").getValue();


                recipeView.setText(trecipeView);
                dateView.setText(tdateView);
                timeView.setText(ttimeView);
                ingredientsView.setText(tingredientsView);

                Picasso.get().load(ttodolistimage).into(recipeImage);

                if(mAuth.getCurrentUser().getUid().equals(myuserid)){
                    btnDelete.setVisibility(View.VISIBLE);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("-------------------"+recipeID);

//                DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("Recipe_toDoList").child(recipeID);

                dbRef.child(recipeID).removeValue();
                Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();

                Intent mintent = new Intent(ViewTask.this,ToDoList.class);
                startActivity(mintent);
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dbRef =FirebaseDatabase.getInstance().getReference();
                dbRef.child("Recipe_toDoList").child(recipeID).child("recipe").setValue(recipeView.getText().toString());
                dbRef.child("Recipe_toDoList").child(recipeID).child("ingredients").setValue(ingredientsView.getText().toString());
                dbRef.child("Recipe_toDoList").child(recipeID).child("date").setValue(dateView.getText().toString());
                dbRef.child("Recipe_toDoList").child(recipeID).child("time").setValue(timeView.getText().toString());

                Toast.makeText(getApplicationContext(),"Update Success",Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(ViewTask.this, ToDoList.class);
                startActivity(mIntent);
            }
        });

        dateView.setInputType(InputType.TYPE_NULL);
        timeView.setInputType(InputType.TYPE_NULL);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(dateView);
            }


        });
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(timeView);
            }
        });


    }
    private void showTimeDialog(final TextView timeView){
        final Calendar calendar=Calendar.getInstance() ;

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                android.icu.text.SimpleDateFormat simpleDateFormat=new android.icu.text.SimpleDateFormat("HH:mm");
                timeView.setText(simpleDateFormat.format(calendar.getTime()));


            }
        };
        new TimePickerDialog(ViewTask.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }
    private void showDateDialog(final TextView dateView) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                android.icu.text.SimpleDateFormat simpleDateFormat = new android.icu.text.SimpleDateFormat("yy-MM-dd");
                dateView.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new DatePickerDialog(ViewTask.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


}

