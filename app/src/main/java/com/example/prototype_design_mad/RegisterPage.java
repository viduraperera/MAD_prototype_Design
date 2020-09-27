package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    EditText name, email, password;
    Button submit_signIn;

    FirebaseAuth mAuth;
    DatabaseReference mDataBase;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("Users");

        progressDialog = new ProgressDialog(this);

        name = (EditText) findViewById(R.id.name_reg);
        email = (EditText) findViewById(R.id.email_reg);
        password = (EditText) findViewById(R.id.password_reg);
        submit_signIn = (Button) findViewById(R.id.submit_reg);

        submit_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
    }

    private void startRegister() {

        final String nameReg = name.getText().toString().trim();
        String emailReg = email.getText().toString().trim();
        String passwordReg = password.getText().toString().trim();

        if(!TextUtils.isEmpty(nameReg) && !TextUtils.isEmpty(emailReg) && !TextUtils.isEmpty(passwordReg)){

            progressDialog.setMessage("Signing Up...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailReg, passwordReg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();

                      DatabaseReference current_user_db = mDataBase.child(user_id);

                      current_user_db.child("name").setValue(nameReg);
                      current_user_db.child("image").setValue("Default");

                      progressDialog.dismiss();

                        Intent mainIntent = new Intent(RegisterPage.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                }
            });
        }

    }
}