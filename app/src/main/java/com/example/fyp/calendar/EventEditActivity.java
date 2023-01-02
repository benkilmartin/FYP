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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventSubjectET, eventTopicET;
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

        eventSubjectET = findViewById(R.id.eSubject);
        eventTopicET = findViewById(R.id.eTopic);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        saveEvent = findViewById(R.id.eBtn);

        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            time = LocalTime.now();
            date = LocalDate.now();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String strDate = dateFormat.format(date);
            String eventSubject = eventSubjectET.getText().toString().trim();
            String eventTopic = eventSubjectET.getText().toString().trim();

            eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
            eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));

                if(eventSubject.isEmpty()||eventTopic.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
                }else{
                    Event newEvent = new Event(eventSubject, eventTopic, strDate);
                    Event.eventsList.add(newEvent);

                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("Events").child(userID).push().setValue(newEvent);
                    finish();
                }
            }
        });
    }
}