package com.example.fyp.UI.UserProfile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.Calendar.Events;
import com.example.fyp.UI.Goals.Goals;
import com.example.fyp.UI.Goals.GoalsEditFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class ChildDataFragment extends Fragment implements OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {
    private ArrayList<User> buserArrayList;
    private ArrayList<String> friendNamess = new ArrayList<>();
    private DatabaseReference bDatabase;
    private FirebaseAuth bfirebaseAuth;
    private String buserID, subString = "", yearString = "", subString2 = "", yearString2 = "";

    LinearLayout bmainLayout, bpieChart1, bpieChart2, bpieChart3, bpieChart4, bpieChart5, bLinkedAccount;
    private PieChart bpieChart, bpiemonthChart, bpiedayChart, bFailedvCompletedChart, bpieChartMonthPerSubject;
    private TextView bhoursStudied, bGoalsCompleted, bsubjectTitle, bmonthTitle, bFailedvCompletedTitle;
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2", ARG_PARAM3 = "param3", ARG_PARAM4 = "param4";
    private ArrayList<Integer> bpieChartColors;
    private Spinner bDisplayMonthSpinner, bDisplayYearSpinner, bDisplayMonthSpinner2, bDisplayYearSpinner2;
    private Button bUncompletedButton;
    private String bChildTitle, bChilddescription, bChildThumbnail, bChildID;

    public static ChildDataFragment newInstance(String btitle, String bsubtitle, String bthumbnail, String receiverUserId) {
        ChildDataFragment fragment = new ChildDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, btitle);
        args.putString(ARG_PARAM2, bsubtitle);
        args.putString(ARG_PARAM3, bthumbnail);
        args.putString(ARG_PARAM4, receiverUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bChildTitle = getArguments().getString(ARG_PARAM1);
            bChilddescription = getArguments().getString(ARG_PARAM2);
            bChildThumbnail = getArguments().getString(ARG_PARAM3);
            buserID = getArguments().getString(ARG_PARAM4);
            System.out.println(bChildID);

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_data,container,false);



        bUncompletedButton = view.findViewById(R.id.kUncompletedButton);
        bhoursStudied = view.findViewById(R.id.khourStudied);
        bGoalsCompleted = view.findViewById(R.id.kGoalsCompleted);
        bsubjectTitle = view.findViewById(R.id.ksubjectTitle);
        bmonthTitle = view.findViewById(R.id.kmonthTitle);
        bFailedvCompletedTitle = view.findViewById(R.id.kFailedvCompletedTitle);
        bpieChart = view.findViewById(R.id.kpieChart);
        bpiemonthChart = view.findViewById(R.id.kpiemonthChart);
        bpiedayChart = view.findViewById(R.id.kpiedayChart);
        bFailedvCompletedChart = view.findViewById(R.id.kFailedvCompletedChart);
        bpieChartMonthPerSubject = view.findViewById(R.id.kpieChartMonthPerSubject);
        bDisplayMonthSpinner = view.findViewById(R.id.kDisplayMonthSpinner);
        bDisplayYearSpinner = view.findViewById(R.id.kDisplayYearSpinner);
        bDisplayYearSpinner2 = view.findViewById(R.id.kDisplayYearSpinner2);
        bpieChart1 = view.findViewById(R.id.kpieChart1);
        bpieChart2 = view.findViewById(R.id.kpieChart2);
        bpieChart3 = view.findViewById(R.id.kpieChart3);
        bpieChart4 = view.findViewById(R.id.kpieChart4);
        bpieChart5 = view.findViewById(R.id.kpieChart5);
        bmainLayout = view.findViewById(R.id.kmainLayout);

        bfirebaseAuth = FirebaseAuth.getInstance();
        bDatabase = FirebaseDatabase.getInstance().getReference();
        System.out.println(bChildID);

        bpieChartColors = new ArrayList<>();
        bpieChartColors.add(Color.parseColor("#abde02"));
        bpieChartColors.add(Color.parseColor("#322e50"));
        bpieChartColors.add(Color.parseColor("#4bcea6"));
        bpieChartColors.add(Color.parseColor("#2bc8dc"));
        bpieChartColors.add(Color.parseColor("#908ef1"));
        bpieChartColors.add(Color.parseColor("#f6fa19"));
        bpieChartColors.add(Color.parseColor("#da870c"));
        bpieChartColors.add(Color.parseColor("#fa1919"));
        bpieChartColors.add(Color.parseColor("#e411a6"));
        bpieChartColors.add(Color.parseColor("#9116dc"));
        bpieChartColors.add(Color.parseColor("#4622c1"));
        bpieChartColors.add(Color.parseColor("#19faaf"));

        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
        bDatabase.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        bpieChart5.setVisibility(View.VISIBLE);
                        bpieChart5.setEnabled(true);
                        getPieDataFailedVPassed();
                        getTotalGoals();
                        getHoursStudiedPerSubjectPerMonth();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

//    private ArrayList<PieEntry> dataValue(){
//        ArrayList<PieEntry> dataValues = new ArrayList<>();
//
//        dataValues.add(new PieEntry(15, "sun"));
//        dataValues.add(new PieEntry(15, "mon"));
//        dataValues.add(new PieEntry(15, "tue"));
//        dataValues.add(new PieEntry(15, "wed"));
//        dataValues.add(new PieEntry(15, "thur"));
//        dataValues.add(new PieEntry(15, "fri"));
//        dataValues.add(new PieEntry(15, "sat"));
//        return dataValues;
//    }




    private void getPieDataFailedVPassed() {
        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
        bDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PieEntry> dataValues = new ArrayList<>();
                int failed = 0, completed = 0;
                DataSnapshot eShot = snapshot.child("English");
                for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot mShot = snapshot.child("Maths");
                for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot gShot = snapshot.child("Geography");
                for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot hShot = snapshot.child("History");
                for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot aShot = snapshot.child("Art");
                for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot bShot = snapshot.child("Biology");
                for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot cShot = snapshot.child("Chemistry");
                for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot pShot = snapshot.child("Physics");
                for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }

                DataSnapshot iShot = snapshot.child("Irish");
                for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate EVENTDATE = LocalDate.parse(events.getDate());
                    LocalDate today = LocalDate.now(ZoneId.systemDefault());
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(false)) {
                        failed = failed + 1;
                    }
                    if (EVENTDATE.isBefore(today) && events.getCompleted().equals(true)) {
                        completed = completed + 1;
                    }
                }
                if (failed > 0)
                    dataValues.add(new PieEntry(failed, "Uncompleted"));
                if (completed > 0)
                    dataValues.add(new PieEntry(completed, "Completed"));

                PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                pieDataSet.setColors(bpieChartColors);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.WHITE);
                bFailedvCompletedChart.setData(pieData);
                bFailedvCompletedChart.setDrawEntryLabels(true);
                bpieChart4.setVisibility(View.VISIBLE);

                Legend l = bFailedvCompletedChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setDrawInside(false);
                l.setTextSize(10);
                l.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getTotalGoals() {
        bDatabase = FirebaseDatabase.getInstance().getReference("Goals").child(buserID);
        bDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int goalsNum = 0;
                    Goals goal = dataSnapshot.getValue(Goals.class);
                    if (goal.getProgress().equalsIgnoreCase("Completed")) {
                        goalsNum = goalsNum + 1;
                    }
                    bGoalsCompleted.setText("Total Goals Achieved " + String.valueOf(goalsNum));
                    bGoalsCompleted.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getHoursStudiedPerSubjectPerMonth() {
        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.subjectspinneroptions, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bDisplayMonthSpinner.setAdapter(subjectAdapter);
        bDisplayMonthSpinner.setOnItemSelectedListener(this);
        subString = bDisplayMonthSpinner.getSelectedItem().toString();

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.year, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bDisplayYearSpinner.setAdapter(yearAdapter);
        bDisplayYearSpinner.setOnItemSelectedListener(this);
        yearString = bDisplayYearSpinner.getSelectedItem().toString();

        ArrayAdapter<CharSequence> yearAdapter2 = ArrayAdapter.createFromResource(this.getContext(), R.array.year, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bDisplayYearSpinner2.setAdapter(yearAdapter2);
        bDisplayYearSpinner2.setOnItemSelectedListener(this);
        yearString2 = bDisplayYearSpinner2.getSelectedItem().toString();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        subString = adapterView.getItemAtPosition(i).toString();
        yearString = adapterView.getItemAtPosition(i).toString();
        yearString2 = adapterView.getItemAtPosition(i).toString();

        if(bDisplayYearSpinner2.getSelectedItem().equals("Total")){
            bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
            bDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PieEntry> dataValues = new ArrayList<>();
                    int englishTime = 0, mathsTime = 0, geographyTime = 0, historyTime = 0, artTime = 0, biologyTime = 0, chemistryTime = 0, physicsTime = 0, irishTime = 0;
                    DataSnapshot eShot = snapshot.child("English");
                    for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            englishTime = englishTime + events.getDuration();
                        }
                    }

                    DataSnapshot mShot = snapshot.child("Maths");
                    for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            mathsTime = mathsTime + events.getDuration();
                        }
                    }

                    DataSnapshot gShot = snapshot.child("Geography");
                    for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            geographyTime = geographyTime + events.getDuration();
                        }
                    }

                    DataSnapshot hShot = snapshot.child("History");
                    for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            historyTime = historyTime + events.getDuration();
                        }
                    }

                    DataSnapshot aShot = snapshot.child("Art");
                    for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            artTime = artTime + events.getDuration();
                        }
                    }

                    DataSnapshot bShot = snapshot.child("Biology");
                    for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            biologyTime = biologyTime + events.getDuration();
                        }
                    }

                    DataSnapshot cShot = snapshot.child("Chemistry");
                    for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            chemistryTime = chemistryTime + events.getDuration();
                        }
                    }

                    DataSnapshot pShot = snapshot.child("Physics");
                    for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            physicsTime = physicsTime + events.getDuration();
                        }
                    }

                    DataSnapshot iShot = snapshot.child("Irish");
                    for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        if (events.getCompleted().equals(true)) {
                            irishTime = irishTime + events.getDuration();
                            System.out.println(irishTime);
                        }
                    }
                    bhoursStudied.setText("Total Hours Spent Studying " + String.valueOf(englishTime + mathsTime + geographyTime + historyTime + artTime + biologyTime + chemistryTime + physicsTime + irishTime));
                    bhoursStudied.setVisibility(View.VISIBLE);
                    if (englishTime > 0)
                        dataValues.add(new PieEntry(englishTime, "English"));
                    if (mathsTime > 0)
                        dataValues.add(new PieEntry(mathsTime, "Maths"));
                    if (geographyTime > 0)
                        dataValues.add(new PieEntry(geographyTime, "Geography"));
                    if (historyTime > 0)
                        dataValues.add(new PieEntry(historyTime, "History"));
                    if (artTime > 0)
                        dataValues.add(new PieEntry(artTime, "Art"));
                    if (biologyTime > 0)
                        dataValues.add(new PieEntry(biologyTime, "Biology"));
                    if (chemistryTime > 0)
                        dataValues.add(new PieEntry(chemistryTime, "Chemistry"));
                    if (physicsTime > 0)
                        dataValues.add(new PieEntry(physicsTime, "Physics"));
                    if (irishTime > 0)
                        dataValues.add(new PieEntry(irishTime, "Irish"));

                    PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                    pieDataSet.setColors(bpieChartColors);
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueTextSize(10f);
                    pieData.setValueTextColor(Color.BLACK);
                    bpieChart.setData(pieData);
                    bpieChart.setDrawHoleEnabled(true);
                    bpieChart.setDrawEntryLabels(true);
                    bpieChart.notifyDataSetChanged();
                    bpieChart.invalidate();
                    bpieChart1.setVisibility(View.VISIBLE);

                    Legend l = bpieChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setDrawInside(false);
                    l.setTextSize(10);
                    l.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else if(bDisplayYearSpinner2.getSelectedItem().equals("2024")){
            bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
            bDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PieEntry> dataValues = new ArrayList<>();
                    int englishTime = 0, mathsTime = 0, geographyTime = 0, historyTime = 0, artTime = 0, biologyTime = 0, chemistryTime = 0, physicsTime = 0, irishTime = 0;
                    DataSnapshot eShot = snapshot.child("English");
                    for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                englishTime = englishTime + events.getDuration();

                        }
                    }

                    DataSnapshot mShot = snapshot.child("Maths");
                    for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                mathsTime = mathsTime + events.getDuration();
                        }
                    }

                    DataSnapshot gShot = snapshot.child("Geography");
                    for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                geographyTime = geographyTime + events.getDuration();
                        }
                    }

                    DataSnapshot hShot = snapshot.child("History");
                    for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                historyTime = historyTime + events.getDuration();
                        }
                    }

                    DataSnapshot aShot = snapshot.child("Art");
                    for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                artTime = artTime + events.getDuration();
                        }
                    }

                    DataSnapshot bShot = snapshot.child("Biology");
                    for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                biologyTime = biologyTime + events.getDuration();
                        }
                    }

                    DataSnapshot cShot = snapshot.child("Chemistry");
                    for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                chemistryTime = chemistryTime + events.getDuration();
                        }
                    }

                    DataSnapshot pShot = snapshot.child("Physics");
                    for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                physicsTime = physicsTime + events.getDuration();
                        }
                    }

                    DataSnapshot iShot = snapshot.child("Irish");
                    for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("24")) {
                            if (events.getCompleted().equals(true))
                                irishTime = irishTime + events.getDuration();
                        }
                    }
                    if (englishTime > 0)
                        dataValues.add(new PieEntry(englishTime, "English"));
                    if (mathsTime > 0)
                        dataValues.add(new PieEntry(mathsTime, "Maths"));
                    if (geographyTime > 0)
                        dataValues.add(new PieEntry(geographyTime, "Geography"));
                    if (historyTime > 0)
                        dataValues.add(new PieEntry(historyTime, "History"));
                    if (artTime > 0)
                        dataValues.add(new PieEntry(artTime, "Art"));
                    if (biologyTime > 0)
                        dataValues.add(new PieEntry(biologyTime, "Biology"));
                    if (chemistryTime > 0)
                        dataValues.add(new PieEntry(chemistryTime, "Chemistry"));
                    if (physicsTime > 0)
                        dataValues.add(new PieEntry(physicsTime, "Physics"));
                    if (irishTime > 0)
                        dataValues.add(new PieEntry(irishTime, "Irish"));

                    PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                    pieDataSet.setColors(bpieChartColors);
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueTextSize(10f);
                    pieData.setValueTextColor(Color.BLACK);
                    bpieChart.setData(pieData);
                    bpieChart.setDrawHoleEnabled(true);
                    bpieChart.setDrawEntryLabels(true);
                    bpieChart.notifyDataSetChanged();
                    bpieChart.invalidate();
                    bpieChart1.setVisibility(View.VISIBLE);

                    Legend l = bpieChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setDrawInside(false);
                    l.setTextSize(10);
                    l.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }else if(bDisplayYearSpinner2.getSelectedItem().equals("2023")){
            bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
            bDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PieEntry> dataValues = new ArrayList<>();
                    int englishTime = 0, mathsTime = 0, geographyTime = 0, historyTime = 0, artTime = 0, biologyTime = 0, chemistryTime = 0, physicsTime = 0, irishTime = 0;
                    DataSnapshot eShot = snapshot.child("English");
                    for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                englishTime = englishTime + events.getDuration();

                        }
                    }

                    DataSnapshot mShot = snapshot.child("Maths");
                    for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                mathsTime = mathsTime + events.getDuration();
                        }
                    }

                    DataSnapshot gShot = snapshot.child("Geography");
                    for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                geographyTime = geographyTime + events.getDuration();
                        }
                    }

                    DataSnapshot hShot = snapshot.child("History");
                    for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                historyTime = historyTime + events.getDuration();
                        }
                    }

                    DataSnapshot aShot = snapshot.child("Art");
                    for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                artTime = artTime + events.getDuration();
                        }
                    }

                    DataSnapshot bShot = snapshot.child("Biology");
                    for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                biologyTime = biologyTime + events.getDuration();
                        }
                    }

                    DataSnapshot cShot = snapshot.child("Chemistry");
                    for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                chemistryTime = chemistryTime + events.getDuration();
                        }
                    }

                    DataSnapshot pShot = snapshot.child("Physics");
                    for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                physicsTime = physicsTime + events.getDuration();
                        }
                    }

                    DataSnapshot iShot = snapshot.child("Irish");
                    for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("23")) {
                            if (events.getCompleted().equals(true))
                                irishTime = irishTime + events.getDuration();
                            System.out.println(irishTime);
                        }
                    }
                    if (englishTime > 0)
                        dataValues.add(new PieEntry(englishTime, "English"));
                    if (mathsTime > 0)
                        dataValues.add(new PieEntry(mathsTime, "Maths"));
                    if (geographyTime > 0)
                        dataValues.add(new PieEntry(geographyTime, "Geography"));
                    if (historyTime > 0)
                        dataValues.add(new PieEntry(historyTime, "History"));
                    if (artTime > 0)
                        dataValues.add(new PieEntry(artTime, "Art"));
                    if (biologyTime > 0)
                        dataValues.add(new PieEntry(biologyTime, "Biology"));
                    if (chemistryTime > 0)
                        dataValues.add(new PieEntry(chemistryTime, "Chemistry"));
                    if (physicsTime > 0)
                        dataValues.add(new PieEntry(physicsTime, "Physics"));
                    if (irishTime > 0)
                        dataValues.add(new PieEntry(irishTime, "Irish"));

                    PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                    pieDataSet.setColors(bpieChartColors);
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueTextSize(10f);
                    pieData.setValueTextColor(Color.BLACK);
                    bpieChart.setData(pieData);
                    bpieChart.setDrawHoleEnabled(true);
                    bpieChart.setDrawEntryLabels(true);
                    bpieChart.notifyDataSetChanged();
                    bpieChart.invalidate();
                    bpieChart1.setVisibility(View.VISIBLE);

                    Legend l = bpieChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setDrawInside(false);
                    l.setTextSize(10);
                    l.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }else if(bDisplayYearSpinner2.getSelectedItem().equals("2022")){
            bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
            bDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PieEntry> dataValues = new ArrayList<>();
                    int englishTime = 0, mathsTime = 0, geographyTime = 0, historyTime = 0, artTime = 0, biologyTime = 0, chemistryTime = 0, physicsTime = 0, irishTime = 0;
                    DataSnapshot eShot = snapshot.child("English");
                    for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                englishTime = englishTime + events.getDuration();

                        }
                    }

                    DataSnapshot mShot = snapshot.child("Maths");
                    for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                mathsTime = mathsTime + events.getDuration();
                        }
                    }

                    DataSnapshot gShot = snapshot.child("Geography");
                    for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                geographyTime = geographyTime + events.getDuration();
                        }
                    }

                    DataSnapshot hShot = snapshot.child("History");
                    for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                historyTime = historyTime + events.getDuration();
                        }
                    }

                    DataSnapshot aShot = snapshot.child("Art");
                    for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                artTime = artTime + events.getDuration();
                        }
                    }

                    DataSnapshot bShot = snapshot.child("Biology");
                    for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                biologyTime = biologyTime + events.getDuration();
                        }
                    }

                    DataSnapshot cShot = snapshot.child("Chemistry");
                    for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                chemistryTime = chemistryTime + events.getDuration();
                        }
                    }

                    DataSnapshot pShot = snapshot.child("Physics");
                    for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                physicsTime = physicsTime + events.getDuration();
                        }
                    }

                    DataSnapshot iShot = snapshot.child("Irish");
                    for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date1 = events.getDate().substring(2, 4);
                        if (date1.equals("22")) {
                            if (events.getCompleted().equals(true))
                                irishTime = irishTime + events.getDuration();
                        }
                    }
                    if (englishTime > 0)
                        dataValues.add(new PieEntry(englishTime, "English"));
                    if (mathsTime > 0)
                        dataValues.add(new PieEntry(mathsTime, "Maths"));
                    if (geographyTime > 0)
                        dataValues.add(new PieEntry(geographyTime, "Geography"));
                    if (historyTime > 0)
                        dataValues.add(new PieEntry(historyTime, "History"));
                    if (artTime > 0)
                        dataValues.add(new PieEntry(artTime, "Art"));
                    if (biologyTime > 0)
                        dataValues.add(new PieEntry(biologyTime, "Biology"));
                    if (chemistryTime > 0)
                        dataValues.add(new PieEntry(chemistryTime, "Chemistry"));
                    if (physicsTime > 0)
                        dataValues.add(new PieEntry(physicsTime, "Physics"));
                    if (irishTime > 0)
                        dataValues.add(new PieEntry(irishTime, "Irish"));

                    PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                    pieDataSet.setColors(bpieChartColors);
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueTextSize(10f);
                    pieData.setValueTextColor(Color.BLACK);
                    bpieChart.setData(pieData);
                    bpieChart.setDrawHoleEnabled(true);
                    bpieChart.setDrawEntryLabels(true);
                    bpieChart.notifyDataSetChanged();
                    bpieChart.invalidate();
                    bpieChart1.setVisibility(View.VISIBLE);

                    Legend l = bpieChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setDrawInside(false);
                    l.setTextSize(10);
                    l.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }



        if (bDisplayYearSpinner.getSelectedItem().equals("2024")) {
            bDisplayMonthSpinner.setVisibility(View.VISIBLE);
            bDisplayMonthSpinner.setEnabled(true);
            switch (subString) {
                case "English":
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("English");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    System.out.println(date1);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }


                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChartMonthPerSubject.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    break;

                case "Maths":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Maths");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    break;
                case "Geography":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Geography");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "History":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("History");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Art":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Art");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Biology":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Biology");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Chemistry":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Chemistry");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Physics":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Physics");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Irish":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Irish");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("24")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
            }

        } else if (bDisplayYearSpinner.getSelectedItem().equals("2023")) {
            bDisplayMonthSpinner.setVisibility(View.VISIBLE);
            bDisplayMonthSpinner.setEnabled(true);
            switch (subString) {
                case "English":
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("English");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    System.out.println(date1);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }


                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChartMonthPerSubject.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    break;

                case "Maths":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Maths");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    break;
                case "Geography":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Geography");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "History":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("History");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Art":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Art");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Biology":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Biology");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Chemistry":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Chemistry");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Physics":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Physics");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Irish":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Irish");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("23")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
            }

        } else if (bDisplayYearSpinner.getSelectedItem().equals("2022")) {
            bDisplayMonthSpinner.setVisibility(View.VISIBLE);
            bDisplayMonthSpinner.setEnabled(true);
            switch (subString) {
                case "English":
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("English");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    System.out.println(date1);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }


                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChartMonthPerSubject.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    break;

                case "Maths":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Maths");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    break;
                case "Geography":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Geography");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "History":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("History");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Art":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Art");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Biology":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Biology");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Chemistry":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Chemistry");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Physics":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Physics");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;
                case "Irish":
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                    bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
                    bDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<PieEntry> dataValues = new ArrayList<>();
                            int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                            DataSnapshot eShot = snapshot.child("Irish");
                            for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                                Events events = dataSnapshots.getValue(Events.class);
                                String date1 = events.getDate().substring(2, 4);
                                if (date1.equals("22")) {
                                    String date = events.getDate().substring(5, 7);
                                    if (date.equals("01")) {
                                        if (events.getCompleted().equals(true))
                                            january = january + events.getDuration();
                                    } else if (date.equals("02")) {
                                        if (events.getCompleted().equals(true))
                                            febuary = febuary + events.getDuration();
                                    } else if (date.equals("03")) {
                                        if (events.getCompleted().equals(true))
                                            march = march + events.getDuration();
                                    } else if (date.equals("04")) {
                                        if (events.getCompleted().equals(true))
                                            april = april + events.getDuration();
                                    } else if (date.equals("05")) {
                                        if (events.getCompleted().equals(true))
                                            may = may + events.getDuration();
                                    } else if (date.equals("06")) {
                                        if (events.getCompleted().equals(true))
                                            june = june + events.getDuration();
                                    } else if (date.equals("07")) {
                                        if (events.getCompleted().equals(true))
                                            july = july + events.getDuration();
                                    } else if (date.equals("08")) {
                                        if (events.getCompleted().equals(true))
                                            august = august + events.getDuration();
                                    } else if (date.equals("09")) {
                                        if (events.getCompleted().equals(true))
                                            september = september + events.getDuration();
                                    } else if (date.equals("10")) {
                                        if (events.getCompleted().equals(true))
                                            october = october + events.getDuration();
                                    } else if (date.equals("11")) {
                                        if (events.getCompleted().equals(true))
                                            november = november + events.getDuration();
                                    } else if (date.equals("12")) {
                                        if (events.getCompleted().equals(true))
                                            december = december + events.getDuration();
                                    }
                                }
                            }
                            if (january > 0)
                                dataValues.add(new PieEntry(january, "January"));
                            if (febuary > 0)
                                dataValues.add(new PieEntry(febuary, "Febuary"));
                            if (march > 0)
                                dataValues.add(new PieEntry(march, "March"));
                            if (april > 0)
                                dataValues.add(new PieEntry(april, "April"));
                            if (may > 0)
                                dataValues.add(new PieEntry(may, "May"));
                            if (june > 0)
                                dataValues.add(new PieEntry(june, "June"));
                            if (july > 0)
                                dataValues.add(new PieEntry(july, "July"));
                            if (august > 0)
                                dataValues.add(new PieEntry(august, "August"));
                            if (september > 0)
                                dataValues.add(new PieEntry(september, "September"));
                            if (october > 0)
                                dataValues.add(new PieEntry(october, "October"));
                            if (november > 0)
                                dataValues.add(new PieEntry(november, "November"));
                            if (december > 0)
                                dataValues.add(new PieEntry(december, "December"));

                            PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                            pieDataSet.setColors(bpieChartColors);
                            PieData pieData = new PieData(pieDataSet);
                            pieData.setValueTextSize(10f);
                            pieData.setValueTextColor(Color.BLACK);
                            bpieChartMonthPerSubject.setData(pieData);
                            bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                            bpieChartMonthPerSubject.setDrawEntryLabels(true);
                            bpieChart5.setVisibility(View.VISIBLE);

                            Legend l = bpieChartMonthPerSubject.getLegend();
                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                            l.setOrientation(Legend.LegendOrientation.VERTICAL);
                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            l.setDrawInside(false);
                            l.setTextSize(10);
                            l.setEnabled(true);
                            bpieChartMonthPerSubject.notifyDataSetChanged();
                            bpieChartMonthPerSubject.invalidate();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    break;

            }
        } else if (bDisplayYearSpinner.getSelectedItem().equals("Total")) {
            bDisplayMonthSpinner.setVisibility(View.GONE);
            bDisplayMonthSpinner.setEnabled(false);
            bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(buserID);
            bDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PieEntry> dataValues = new ArrayList<>();
                    int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                    DataSnapshot eShot = snapshot.child("English");
                    for (DataSnapshot dataSnapshots : eShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot mShot = snapshot.child("Maths");
                    for (DataSnapshot dataSnapshots : mShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot gShot = snapshot.child("Geography");
                    for (DataSnapshot dataSnapshots : gShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot hShot = snapshot.child("History");
                    for (DataSnapshot dataSnapshots : hShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot aShot = snapshot.child("Art");
                    for (DataSnapshot dataSnapshots : aShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot bShot = snapshot.child("Biology");
                    for (DataSnapshot dataSnapshots : bShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot cShot = snapshot.child("Chemistry");
                    for (DataSnapshot dataSnapshots : cShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot pShot = snapshot.child("Physics");
                    for (DataSnapshot dataSnapshots : pShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }

                    DataSnapshot iShot = snapshot.child("Irish");
                    for (DataSnapshot dataSnapshots : iShot.getChildren()) {
                        Events events = dataSnapshots.getValue(Events.class);
                        String date = events.getDate().substring(5, 7);
                        if (date.equals("01")) {
                            if (events.getCompleted().equals(true))
                                january = january + events.getDuration();
                        } else if (date.equals("02")) {
                            if (events.getCompleted().equals(true))
                                febuary = febuary + events.getDuration();
                        } else if (date.equals("03")) {
                            if (events.getCompleted().equals(true))
                                march = march + events.getDuration();
                        } else if (date.equals("04")) {
                            if (events.getCompleted().equals(true))
                                april = april + events.getDuration();
                        } else if (date.equals("05")) {
                            if (events.getCompleted().equals(true))
                                may = may + events.getDuration();
                        } else if (date.equals("06")) {
                            if (events.getCompleted().equals(true))
                                june = june + events.getDuration();
                        } else if (date.equals("07")) {
                            if (events.getCompleted().equals(true))
                                july = july + events.getDuration();
                        } else if (date.equals("08")) {
                            if (events.getCompleted().equals(true))
                                august = august + events.getDuration();
                        } else if (date.equals("09")) {
                            if (events.getCompleted().equals(true))
                                september = september + events.getDuration();
                        } else if (date.equals("10")) {
                            if (events.getCompleted().equals(true))
                                october = october + events.getDuration();
                        } else if (date.equals("11")) {
                            if (events.getCompleted().equals(true))
                                november = november + events.getDuration();
                        } else if (date.equals("12")) {
                            if (events.getCompleted().equals(true))
                                december = december + events.getDuration();
                        }
                    }
                    if (january > 0)
                        dataValues.add(new PieEntry(january, "January"));
                    if (febuary > 0)
                        dataValues.add(new PieEntry(febuary, "Febuary"));
                    if (march > 0)
                        dataValues.add(new PieEntry(march, "March"));
                    if (april > 0)
                        dataValues.add(new PieEntry(april, "April"));
                    if (may > 0)
                        dataValues.add(new PieEntry(may, "May"));
                    if (june > 0)
                        dataValues.add(new PieEntry(june, "June"));
                    if (july > 0)
                        dataValues.add(new PieEntry(july, "July"));
                    if (august > 0)
                        dataValues.add(new PieEntry(august, "August"));
                    if (september > 0)
                        dataValues.add(new PieEntry(september, "September"));
                    if (october > 0)
                        dataValues.add(new PieEntry(october, "October"));
                    if (november > 0)
                        dataValues.add(new PieEntry(november, "November"));
                    if (december > 0)
                        dataValues.add(new PieEntry(december, "December"));

                    PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                    pieDataSet.setColors(bpieChartColors);
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueTextSize(10f);
                    pieData.setValueTextColor(Color.BLACK);
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.setData(pieData);
                    bpieChartMonthPerSubject.setDrawHoleEnabled(true);
                    bpieChartMonthPerSubject.setDrawEntryLabels(true);
                    bpieChartMonthPerSubject.setVisibility(View.VISIBLE);

                    Legend l = bpieChartMonthPerSubject.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setDrawInside(false);
                    l.setTextSize(10);
                    l.setEnabled(true);
                    bpieChartMonthPerSubject.notifyDataSetChanged();
                    bpieChartMonthPerSubject.invalidate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}