package com.example.fyp.UI.Calendar;

import static com.example.fyp.UI.Calendar.CalUtils.daysInWeekArray;
import static com.example.fyp.UI.Calendar.CalUtils.monthYearFromDate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.GoogleBooks.Book;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WeekActivity extends AppCompatActivity implements CalAdapter.OnItemListener {
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
        setContentView(R.layout.activity_week);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        bpbtn = findViewById(R.id.kpbtn);
        bnbtn = findViewById(R.id.knbtn);
        bebtn = findViewById(R.id.kebtn);
        Event.eventsList.clear();
        if (Event.eventsList.isEmpty()) {
            checkForEvents();
        }
        setWeekView();


        bpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalUtils.selectedDate = CalUtils.selectedDate.minusMonths(1);
                setWeekView();
            }
        });

        bebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeekActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });

        bnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalUtils.selectedDate = CalUtils.selectedDate.plusMonths(1);
                setWeekView();
            }
        });
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalUtils.selectedDate);

        CalAdapter calendarAdapter = new CalAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(dailyEvents, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        eventListView.setLayoutManager(linearLayoutManager);
        eventListView.setAdapter(eventAdapter);
    }

    public void checkForEvents() {
        bfirebaseAuth = FirebaseAuth.getInstance();
        buserID = bfirebaseAuth.getUid();
        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
        bDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot eShot = snapshot.child("English");
                for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);
                }


                DataSnapshot mShot = snapshot.child("Maths");
                for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);

                }


                DataSnapshot gShot = snapshot.child("Geography");
                for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);

                }

                DataSnapshot hShot = snapshot.child("History");
                for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);

                }

                DataSnapshot aShot = snapshot.child("Art");
                for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);
                }

                DataSnapshot bShot = snapshot.child("Biology");
                for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);

                }

                DataSnapshot cShot = snapshot.child("Chemistry");
                for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);

                }

                DataSnapshot pShot = snapshot.child("Physics");
                for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);

                }

                DataSnapshot iShot = snapshot.child("Irish");
                for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    Event newEvent = new Event(events.getSubject(), events.getTags(), events.getDuration(), events.getDate(), events.getTime(), events.getCompleted(), date = LocalDate.parse(events.getDate()), events.getId());
                    Event.eventsList.add(newEvent);

                }
                setWeekView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}