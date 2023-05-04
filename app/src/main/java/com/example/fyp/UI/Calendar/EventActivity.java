package com.example.fyp.UI.Calendar;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference bEventID ;
    private TextView eventDateTV, eventTimeTV;
    private EditText bEventDescription;
    private Button saveEvent;
    private Spinner subSpinner, durSpinner;
    private String subString="";
    private int durString=0;




    @Override
    protected void onCreate(Bundle savedInstanceState)    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        subSpinner = findViewById(R.id.sSpinner);
        durSpinner = findViewById(R.id.dSpinner);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        bEventDescription = findViewById(R.id.kEventDescription);
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
        if(subString.equalsIgnoreCase("Enlgish")){

        }

        bEventID = FirebaseDatabase.getInstance().getReference();

        Date time = new Date();
        LocalDate one = CalUtils.selectedDate;
        System.out.println(CalUtils.selectedDate);
        String one1 = one.toString();
        System.out.println(one1);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String strDate = one.toString();
        String strTime = timeFormat.format(time);
        eventDateTV.setText("Date: " + one1);
        eventTimeTV.setText("Time: " + strTime);
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String ID = bEventID.push().getKey();
                String descs = bEventDescription.getText().toString();
                    Event newEvent = new Event(subString, descs, durString, strDate, strTime, false, CalUtils.selectedDate, ID);
                    Event.eventsList.add(newEvent);
                    Events newEvents = new Events(subString, descs, durString, strDate,false, strTime, ID);
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("Events").child(userID).child(subString).child(ID).setValue(newEvents);
                    Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();
                    finish();
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