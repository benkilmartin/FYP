package com.example.fyp.UI.Calendar;

import static com.example.fyp.UI.Calendar.CalUtils.daysInWeekArray;
import static com.example.fyp.UI.Calendar.CalUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class UncompleteEvents extends AppCompatActivity implements CalAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView, eventListView;

    private Button bpbtn, bnbtn, bebtn;

    private DatabaseReference bDatabase;
    private FirebaseAuth bfirebaseAuth;
    private String buserID;
    private LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncompleted_events);


        eventListView = findViewById(R.id.eventListView);

        ArrayList<Event> dailyEvents = new ArrayList<>();
        bfirebaseAuth = FirebaseAuth.getInstance();
        buserID = bfirebaseAuth.getUid();
        EventAdapter eventAdapter = new EventAdapter(Event.eventsList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        eventListView.setLayoutManager(linearLayoutManager);
        eventListView.setAdapter(eventAdapter);
        Event.eventsList.clear();
        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
        bDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot eShot = snapshot.child("English");
                for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }


                DataSnapshot mShot = snapshot.child("Maths");
                for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }


                DataSnapshot gShot = snapshot.child("Geography");
                for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }

                DataSnapshot hShot = snapshot.child("History");
                for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }

                DataSnapshot aShot = snapshot.child("Art");
                for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }

                DataSnapshot bShot = snapshot.child("Biology");
                for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }

                DataSnapshot cShot = snapshot.child("Chemistry");
                for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }

                DataSnapshot pShot = snapshot.child("Physics");
                for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }

                DataSnapshot iShot = snapshot.child("Irish");
                for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                        Event.eventsList.add(newEvent);
                    }
                }
                eventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        checkForUncompletedEvents();

    }

    public void checkForUncompletedEvents() {

    }

    @Override
    public void onItemClick(int position, LocalDate date) {

    }
}