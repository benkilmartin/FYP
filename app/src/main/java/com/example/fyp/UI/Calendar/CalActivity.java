package com.example.fyp.UI.Calendar;

import static com.example.fyp.UI.Calendar.CalUtils.daysInMonthArray;
import static com.example.fyp.UI.Calendar.CalUtils.monthYearFromDate;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalActivity extends AppCompatActivity implements CalAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button wView, pWeek, nWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        wView = findViewById(R.id.wbtn);
        pWeek = findViewById(R.id.pbtn);
        nWeek = findViewById(R.id.nbtn);
        CalUtils.selectedDate = LocalDate.now();
        setMonthView();

        pWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalUtils.selectedDate = CalUtils.selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        wView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CalActivity.this, WeekActivity.class);
                startActivity(intent);
            }
        });

        nWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalUtils.selectedDate = CalUtils.selectedDate.plusMonths(1);
                setMonthView();
            }
        });
    }

    private void setMonthView(){
        monthYearText.setText(monthYearFromDate(CalUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalUtils.selectedDate);

        CalAdapter calendarAdapter = new CalAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }



    public void nextMonthAction(View view){
        CalUtils.selectedDate = CalUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date){
        if(date != null){
            CalUtils.selectedDate = date;
            setMonthView();
        }
    }
}








