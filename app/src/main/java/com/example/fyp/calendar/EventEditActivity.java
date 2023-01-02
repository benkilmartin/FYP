package com.example.fyp.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView eventDateTV, eventTimeTV;
    private Button saveEvent;
    private Spinner subSpinner, durSpinner;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String userID;
    private String subString="", durString="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        subSpinner = findViewById(R.id.sSpinner);
        durSpinner = findViewById(R.id.dSpinner);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        saveEvent = findViewById(R.id.eBtn);

        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this, R.array.subjectspinneroptions, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        subSpinner.setAdapter(subjectAdapter);
        subSpinner.setOnItemSelectedListener(this);
        subString = subSpinner.getSelectedItem().toString();

        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(this, R.array.durationspinneroptions, android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        durSpinner.setAdapter(durationAdapter);
        durSpinner.setOnItemSelectedListener(this);
        durString = durSpinner.getSelectedItem().toString();

        Date date = new Date();
        Date time = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String strDate = dateFormat.format(date);
        String strTime = timeFormat.format(time);

        eventDateTV.setText("Date: " + strDate);
        eventTimeTV.setText("Time: " + strTime);
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

//                if(subString.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
//                }else{
                    Event newEvent = new Event(subString, durString, strDate, strTime, CalendarUtils.selectedDate);
                    Event.eventsList.add(newEvent);
                    Events newEvents = new Events(subString, durString, strDate, strTime);
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("Events").child(userID).push().setValue(newEvents);
                    Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();
                    finish();
//                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String durString = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}