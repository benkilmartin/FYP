package com.example.fyp.UserFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.Splash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth bfirebase;


    private EditText bemail, bpassword;
    private Button bloginBtn;
    private TextView bforgotBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        bemail = findViewById(R.id.kEmail);
        bpassword = findViewById(R.id.kPassword);
        bloginBtn = findViewById(R.id.kBtn);
        bforgotBtn = findViewById(R.id.kpassword);
        bfirebase = FirebaseAuth.getInstance();

        bloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qemail = bemail.getText().toString().trim();
                String qpassword = bpassword.getText().toString().trim();

                if (qpassword.isEmpty() || qemail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Populate Fields", Toast.LENGTH_SHORT).show();
                } else {
                    bfirebase.signInWithEmailAndPassword(qemail,qpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

        bforgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, ForgotPassword.class));
            }
        });
    }

    private void checkVerification() {
        FirebaseUser firebaseUser = bfirebase.getCurrentUser();
        if(firebaseUser.isEmailVerified()){
            Intent intent=new Intent(LoginPage.this, Splash.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Please verify your email!", Toast.LENGTH_SHORT).show();
        }
    }
}