package com.example.fyp.UserFolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp.R;

public class SelectionPage extends AppCompatActivity {

    private Button bsBtn;
    private Button bpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_page);
        bsBtn = findViewById(R.id.ksBtn);
        bpBtn = findViewById(R.id.kpBtn);

        bsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectionPage.this, RegistrationPage.class));
            }
        });

        bpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectionPage.this, ParentRegistrationPage.class));
            }
        });
    }
}