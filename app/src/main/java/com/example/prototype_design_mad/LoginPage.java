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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    EditText emailLogin, passWordLogin;
    Button buttonLogin, switchUser;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferenceUsers;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReferenceUsers.keepSynced(true);

        progressDialog = new ProgressDialog(this);

        emailLogin = (EditText)findViewById(R.id.login_email);
        passWordLogin = (EditText)findViewById(R.id.login_password);

        buttonLogin = (Button)findViewById(R.id.login_button);
        switchUser = (Button)findViewById(R.id.switch_sign_in);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLogin();
            }
        });

        switchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginPage.this, "New Account Page", Toast.LENGTH_SHORT).show();
                Intent RegisterIntent = new Intent(LoginPage.this, RegisterPage.class);
                //user cannot go back(IT19008110)
                RegisterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(RegisterIntent);
            }
        });

    }

    private void checkLogin() {

        String email = emailLogin.getText().toString().trim();
        String password = passWordLogin.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            progressDialog.setMessage("Checking Login........");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){


                        checkUserExist();
                    }
                    else{
                        Toast.makeText(LoginPage.this, "Error Login", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

                }
            });

        }
    }

    private void checkUserExist() {

        final String user_id = firebaseAuth.getCurrentUser().getUid();

        databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){

                    Intent MainIntent = new Intent(LoginPage.this, MainActivity.class);
                    //user cannot go back(IT19008110)
                    MainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(MainIntent);

                }
                else{
                    Intent setUpIntent = new Intent(LoginPage.this, SetUpActivity.class);
                    //user cannot go back(IT19008110)
                    setUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setUpIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}