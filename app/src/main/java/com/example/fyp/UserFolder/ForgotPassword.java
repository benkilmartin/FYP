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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth firebase;

    private TextView returntoLogin;
    private EditText forgetPassowrd;
    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        firebase= FirebaseAuth.getInstance();

        returntoLogin=findViewById(R.id.haveAccount);
        forgetPassowrd=findViewById(R.id.rEmail);
        changePassword=findViewById(R.id.rBtn);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=forgetPassowrd.getText().toString().trim();
                if(mail.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter your email first",Toast.LENGTH_SHORT).show();
                }
                else {
                    firebase.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Email Sent, Please check your email to recover your pasword",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, LoginPage.class));
                            }
                            else                            {
                                Toast.makeText(getApplicationContext(),"The email you entered doe snot exist in our system",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        returntoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotPassword.this, LoginPage.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();
    }
}