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
import com.example.fyp.ui.UserProfile.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationPage extends AppCompatActivity {

    private FirebaseAuth firebase;
    private DatabaseReference mDatabase;

    private Button signupButton;
    private TextView loginpageButton;
    private EditText signupUsername, signupEmail, signupPassword, csignupPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        firebase= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signupUsername = findViewById(R.id.rUsername);
        signupEmail = findViewById(R.id.rEmail);
        signupPassword = findViewById(R.id.rPassword);
        csignupPassword = findViewById(R.id.cPassword);

        signupButton = findViewById(R.id.rBtn);
        loginpageButton = findViewById(R.id.haveAccount);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userN = signupUsername.getText().toString().trim();
                String mail = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String cpassword = csignupPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty() || cpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 5 & !password.contains(" ")) {
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                } else {
                    firebase.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                                User user = new User(userN,mail,password, " ");
                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                FirebaseDatabase.getInstance().getReference("Users").child(userID).setValue(user);
                                sendVerificationEmail();

                                startActivity(new Intent(RegistrationPage.this, LoginPage.class));

                            }else{
                                Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        loginpageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(RegistrationPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }
    private void sendVerificationEmail() {
        FirebaseUser firebaseUser=firebase.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                    firebase.signOut();
                    finish();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Verification Email Failed", Toast.LENGTH_SHORT).show();


        }
    }
}