package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.calendar.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth firebase;
    private FirebaseUser firebaseU;

    private EditText email, password;
    private Button loginBtn;
    private TextView forgotBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebase = FirebaseAuth.getInstance();
        firebaseU =firebase.getCurrentUser();

//        if(firebaseU!=null){
//            startActivity(new Intent(LoginPage.this, MainActivity.class));
//        }
        email = findViewById(R.id.lEmail);
        password = findViewById(R.id.lPassword);
        loginBtn = findViewById(R.id.lBtn);
        forgotBtn = findViewById(R.id.fpassword);
        firebase = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qemail = email.getText().toString().trim();
                String qpassword = password.getText().toString().trim();

                if (qpassword.isEmpty() || qemail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Populate Fields", Toast.LENGTH_SHORT).show();
                } else {
                    firebase.signInWithEmailAndPassword(qemail,qpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                checkVerification();
                            }else{
                                Toast.makeText(getApplicationContext(), "Username or password are incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

//        forgotBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //call signup activity
//                startActivity(new Intent(LoginPage.this, forgotPassword.class));
//            }
//        });
    }

    private void checkVerification() {
        FirebaseUser firebaseUser = firebase.getCurrentUser();
        if(firebaseUser.isEmailVerified()){
            startActivity(new Intent(LoginPage.this, MainActivity.class));
        }else{
            Toast.makeText(getApplicationContext(), "Please verify your email!", Toast.LENGTH_SHORT).show();
        }
    }
}