package com.example.prototype_design_mad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class UpdateTask extends AppCompatActivity {

    EditText date;
    EditText time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        date=findViewById(R.id.AddDate);
        time=findViewById(R.id.AddTime);


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


    private void showTimeDialog(final EditText time){
        final Calendar calendar=Calendar.getInstance() ;

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                android.icu.text.SimpleDateFormat simpleDateFormat=new android.icu.text.SimpleDateFormat("HH:mm");
                time.setText(simpleDateFormat.format(calendar.getTime()));


            }
        };
        new TimePickerDialog(UpdateTask.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }
    private void showDateDialog(final EditText date) {
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

            }
        };
        new DatePickerDialog(UpdateTask.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }



}
