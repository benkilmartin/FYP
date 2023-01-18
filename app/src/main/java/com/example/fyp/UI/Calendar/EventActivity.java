package com.example.fyp.UI.Calendar;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView eventDateTV, eventTimeTV;
    private Button saveEvent;
    private Spinner subSpinner, durSpinner;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String userID;
    private String subString="";
    private int durString=0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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
        durString = parseInt(durSpinner.getSelectedItem().toString());

        Date time = new Date();
        LocalDate one = CalUtils.selectedDate;
        String one1 = one.toString();
        System.out.println(one1);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String strDate = one1;
        String strTime = timeFormat.format(time);

        eventDateTV.setText("Date: " + one1);
        eventTimeTV.setText("Time: " + strTime);
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                    Event newEvent = new Event(subString, durString, strDate, strTime, CalUtils.selectedDate);
                    Event.eventsList.add(newEvent);
                    Events newEvents = new Events(subString, durString, strDate, strTime);
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("Events").child(userID).child(subString).push().setValue(newEvents);
                    Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();
                    finish();
//                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case R.id.sSpinner:
                subString = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.dSpinner:
                durString = parseInt(adapterView.getItemAtPosition(i).toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}