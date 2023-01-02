package com.example.fyp.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private Button saveEvent;

    private LocalTime time;
    private LocalDate date;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        saveEvent = findViewById(R.id.saveEventAction);

        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            time = LocalTime.now();
            date = LocalDate.now();
            eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
            eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
            String eventName = eventNameET.getText().toString().trim();

                if(eventName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
                }else{
                    Event newEvent = new Event(eventName);
                    Event.eventsList.add(newEvent);

                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("Events").child(userID).setValue(newEvent);
                    finish();
                }
            }
        });
    }
}