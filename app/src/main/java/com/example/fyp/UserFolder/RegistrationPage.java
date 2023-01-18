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
import com.example.fyp.UI.UserProfile.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationPage extends AppCompatActivity {

    private FirebaseAuth bfirebase;

    private Button bsignupButton;
    private TextView bloginpageButton;
    private EditText bsignupUsername, bsignupEmail, bsignupPassword, bcsignupPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        bfirebase= FirebaseAuth.getInstance();

        bsignupUsername = findViewById(R.id.kUsername);
        bsignupEmail = findViewById(R.id.kEmail);
        bsignupPassword = findViewById(R.id.kPassword);
        bcsignupPassword = findViewById(R.id.kcPassword);

        bsignupButton = findViewById(R.id.kBtn);
        bloginpageButton = findViewById(R.id.khaveAccount);

        bsignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String buserN = bsignupUsername.getText().toString().trim();
                String bmail = bsignupEmail.getText().toString().trim();
                String bpassword = bsignupPassword.getText().toString().trim();
                String bcpassword = bcsignupPassword.getText().toString().trim();

                if (bmail.isEmpty() || bpassword.isEmpty() || bcpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
                } else if (bpassword.length() < 5 & !bpassword.contains(" ")) {
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                } else {
                    bfirebase.createUserWithEmailAndPassword(bmail,bpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                                User user = new User(buserN,bmail,bpassword, " ");
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

        bloginpageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(RegistrationPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }
    private void sendVerificationEmail() {
        FirebaseUser firebaseUser=bfirebase.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                    bfirebase.signOut();
                    finish();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Verification Email Failed", Toast.LENGTH_SHORT).show();


        }
    }
}