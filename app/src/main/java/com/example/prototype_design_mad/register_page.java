package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register_page extends AppCompatActivity
{
    EditText editTextEmail, editTextUsername, editTextPassword;
    Button signupid;
    TextView tologin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        editTextEmail    = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        signupid = findViewById(R.id.signupid);
        tologin  = findViewById(R.id.tologin);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), login_page.class));
            finish();
        }

        signupid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = editTextEmail.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    editTextEmail.setError("Email Required!!");
                    return;
                }

                if (TextUtils.isEmpty(username))
                {
                    editTextEmail.setError("User Name Required!!");
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    editTextEmail.setError("Password Required!!");
                    return;
                }

                if (password.length() < 6)
                {
                    editTextPassword.setError("Password Must have at-least 6 characters!");
                    return;
                }

                //Register the user in FireBase
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(register_page.this, "Profile Created!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), login_page.class));
                        }
                        else
                        {
                            Toast.makeText(register_page.this, "ERROR!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }

}