package com.example.fyp.UI.Calendar;

import static com.example.fyp.UI.Calendar.CalUtils.daysInWeekArray;
import static com.example.fyp.UI.Calendar.CalUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekActivity extends AppCompatActivity implements CalAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Button bpbtn, bnbtn, bebtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        bpbtn = findViewById(R.id.kpbtn);
        bnbtn = findViewById(R.id.knbtn);
        bebtn = findViewById(R.id.kebtn);
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
                Intent intent=new Intent(WeekActivity.this, EventActivity.class);
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

    private void setWeekView(){
        monthYearText.setText(monthYearFromDate(CalUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalUtils.selectedDate);

        CalAdapter calendarAdapter = new CalAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }

    @Override
    public void onItemClick(int position, LocalDate date){
        CalUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater(){
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }
}