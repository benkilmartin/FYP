package com.example.fyp.UI.UserProfile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fyp.R;
import com.example.fyp.UI.Calendar.Events;
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
import java.util.ArrayList;

public class ChildDataFragment extends Fragment implements OnChartValueSelectedListener {
    private DatabaseReference bDatabase;
    private FirebaseAuth bfirebaseAuth;
    private String buserID;

    LinearLayout bmainLayout, bpieChart1, bpieChart2, bpieChart3;
    private PieChart bpieChart, bpiemonthChart, bpiedayChart;
    private TextView bhoursStudied, bsubjectTitle, bmonthTitle;
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2", ARG_PARAM3 = "param3", ARG_PARAM4 = "param4";
    private ArrayList<Integer> bpieChartColors;
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
            bChildID = getArguments().getString(ARG_PARAM4);
            System.out.println(bChildID);

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_data,container,false);


        bhoursStudied = view.findViewById(R.id.khourStudied);
        bsubjectTitle = view.findViewById(R.id.ksubjectTitle);
        bmonthTitle= view.findViewById(R.id.kmonthTitle);
        bpieChart = view.findViewById(R.id.kpieChart);
        bpiemonthChart = view.findViewById(R.id.kpiemonthChart);
        bpiedayChart = view.findViewById(R.id.kpiedayChart);
        bpieChart1 = view.findViewById(R.id.kpieChart1);
        bpieChart2 = view.findViewById(R.id.kpieChart2);
        bpieChart3 = view.findViewById(R.id.kpieChart3);
        bmainLayout = view.findViewById(R.id.kmainLayout);

        bfirebaseAuth = FirebaseAuth.getInstance();
        buserID = bfirebaseAuth.getUid();
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

        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(bChildID);
        bDatabase.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        getPieData();
                        getPieDataMonth();
                        getPieDataDay();
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



    private void getPieData(){
        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(bChildID);
        bDatabase.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PieEntry> dataValues = new ArrayList<>();
                int englishTime = 0,mathsTime= 0, geographyTime= 0, historyTime= 0, artTime= 0, biologyTime= 0, chemistryTime= 0, physicsTime= 0, irishTime= 0;
                DataSnapshot eShot=snapshot.child("English");
                for(DataSnapshot dataSnapshots : eShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    englishTime = englishTime + events.getDuration();
                }

                DataSnapshot mShot=snapshot.child("Maths");
                for(DataSnapshot dataSnapshots : mShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    mathsTime = mathsTime + events.getDuration();
                }

                DataSnapshot gShot=snapshot.child("Geography");
                for(DataSnapshot dataSnapshots : gShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    geographyTime = geographyTime + events.getDuration();
                }

                DataSnapshot hShot=snapshot.child("History");
                for(DataSnapshot dataSnapshots : hShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    historyTime = historyTime + events.getDuration();
                }

                DataSnapshot aShot=snapshot.child("Art");
                for(DataSnapshot dataSnapshots : aShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    artTime = artTime + events.getDuration();
                }

                DataSnapshot bShot=snapshot.child("Biology");
                for(DataSnapshot dataSnapshots : bShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    biologyTime = biologyTime + events.getDuration();
                }

                DataSnapshot cShot=snapshot.child("Chemistry");
                for(DataSnapshot dataSnapshots : cShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    chemistryTime = chemistryTime + events.getDuration();
                }

                DataSnapshot pShot=snapshot.child("Physics");
                for(DataSnapshot dataSnapshots : pShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    physicsTime = physicsTime + events.getDuration();
                }

                DataSnapshot iShot=snapshot.child("Irish");
                for(DataSnapshot dataSnapshots : iShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    irishTime = irishTime + events.getDuration();
                }
                bhoursStudied.setText("Total Hours Spent Studying "+String.valueOf(englishTime+mathsTime+geographyTime+historyTime+artTime+biologyTime+chemistryTime+physicsTime+irishTime));
                bhoursStudied.setVisibility(View.VISIBLE);
                dataValues.add(new PieEntry(englishTime, "English"));
                dataValues.add(new PieEntry(mathsTime, "Maths"));
                dataValues.add(new PieEntry(geographyTime, "Geography"));
                dataValues.add(new PieEntry(historyTime, "History"));
                dataValues.add(new PieEntry(artTime, "Art"));
                dataValues.add(new PieEntry(biologyTime, "Biology"));
                dataValues.add(new PieEntry(chemistryTime, "Chemistry"));
                dataValues.add(new PieEntry(physicsTime, "Physics"));
                dataValues.add(new PieEntry(irishTime, "Irish"));
                PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                pieDataSet.setColors(bpieChartColors);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.BLACK);
                bpieChart.setData(pieData);
                bpieChart.setDrawHoleEnabled(true);
                bpieChart.setDrawEntryLabels(true);
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

    private void getPieDataMonth(){
        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(bChildID);
        bDatabase.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PieEntry> dataValues = new ArrayList<>();
                int january = 0, febuary = 0, march = 0, april = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
                DataSnapshot eShot=snapshot.child("English");
                for(DataSnapshot dataSnapshots : eShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot mShot=snapshot.child("Maths");
                for(DataSnapshot dataSnapshots : mShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot gShot=snapshot.child("Geography");
                for(DataSnapshot dataSnapshots : gShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot hShot=snapshot.child("History");
                for(DataSnapshot dataSnapshots : hShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot aShot=snapshot.child("Art");
                for(DataSnapshot dataSnapshots : aShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot bShot=snapshot.child("Biology");
                for(DataSnapshot dataSnapshots : bShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot cShot=snapshot.child("Chemistry");
                for(DataSnapshot dataSnapshots : cShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot pShot=snapshot.child("Physics");
                for(DataSnapshot dataSnapshots : pShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }

                DataSnapshot iShot=snapshot.child("Irish");
                for(DataSnapshot dataSnapshots : iShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    String date = events.getDate().substring(5,7);
                    if(date.equals("01")){
                        january = january+events.getDuration();
                    }else if(date.equals("02")){
                        febuary = febuary+events.getDuration();
                    }else if(date.equals("03")){
                        march = march+events.getDuration();
                    }else if(date.equals("04")){
                        april = april+events.getDuration();
                    }else if(date.equals("05")){
                        april = april+events.getDuration();
                    }else if(date.equals("06")){
                        june = june+events.getDuration();
                    }else if(date.equals("07")){
                        july = july+events.getDuration();
                    }else if(date.equals("08")){
                        august = august+events.getDuration();
                    }else if(date.equals("09")){
                        september = september+events.getDuration();
                    }else if(date.equals("10")){
                        october = october+events.getDuration();
                    }else if(date.equals("11")){
                        november = november+events.getDuration();
                    }else if(date.equals("12")){
                        december = december+events.getDuration();
                    }
                }
                dataValues.add(new PieEntry(january, "January"));
                dataValues.add(new PieEntry(febuary, "Febuary"));
                dataValues.add(new PieEntry(march, "March"));
                dataValues.add(new PieEntry(april, "April"));
                dataValues.add(new PieEntry(may, "May"));
                dataValues.add(new PieEntry(june, "June"));
                dataValues.add(new PieEntry(july, "July"));
                dataValues.add(new PieEntry(august, "August"));
                dataValues.add(new PieEntry(september, "September"));
                dataValues.add(new PieEntry(october, "October"));
                dataValues.add(new PieEntry(november, "November"));
                dataValues.add(new PieEntry(december, "December"));

                PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                pieDataSet.setColors(bpieChartColors);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.BLACK);
                bpiemonthChart.setData(pieData);
                bpiemonthChart.setDrawEntryLabels(true);
                bpieChart2.setVisibility(View.VISIBLE);

                Legend l = bpiemonthChart.getLegend();
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

    private void getPieDataDay(){
        bDatabase = FirebaseDatabase.getInstance().getReference("Events").child(bChildID);
        bDatabase.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PieEntry> dataValues = new ArrayList<>();
                int mon = 0, tue = 0, wed = 0, thur = 0, fri = 0, sat = 0, sun = 0;
                                DataSnapshot eShot=snapshot.child("English");
                for(DataSnapshot dataSnapshots : eShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot mShot=snapshot.child("Maths");
                for(DataSnapshot dataSnapshots : mShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot gShot=snapshot.child("Geography");
                for(DataSnapshot dataSnapshots : gShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot hShot=snapshot.child("History");
                for(DataSnapshot dataSnapshots : hShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot aShot=snapshot.child("Art");
                for(DataSnapshot dataSnapshots : aShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot bShot=snapshot.child("Biology");
                for(DataSnapshot dataSnapshots : bShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot cShot=snapshot.child("Chemistry");
                for(DataSnapshot dataSnapshots : cShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot pShot=snapshot.child("Physics");
                for(DataSnapshot dataSnapshots : pShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }

                DataSnapshot iShot=snapshot.child("Irish");
                for(DataSnapshot dataSnapshots : iShot.getChildren()) {
                    Events events = dataSnapshots.getValue(Events.class);
                    LocalDate DATE  = LocalDate.parse(events.getDate());
                    DayOfWeek dayOfWeek = DATE.getDayOfWeek();
                    System.out.println(dayOfWeek.getValue());
                    int date = dayOfWeek.getValue();
                    if(date == 1){
                        mon = mon+events.getDuration();
                    }else if(date == 2){
                        tue = tue+events.getDuration();
                    }else if(date == 3){
                        wed = wed+events.getDuration();
                    }else if(date == 4){
                        thur = thur+events.getDuration();
                    }else if(date == 5){
                        fri = fri+events.getDuration();
                    }else if(date == 6){
                        sat = sat+events.getDuration();
                    }else if(date == 7){
                        sun = sun+events.getDuration();
                    }
                }
                dataValues.add(new PieEntry(mon, "Monday"));
                dataValues.add(new PieEntry(tue, "Tuesday"));
                dataValues.add(new PieEntry(wed, "Wednesday"));
                dataValues.add(new PieEntry(thur, "Thursday"));
                dataValues.add(new PieEntry(fri, "Friday"));
                dataValues.add(new PieEntry(sat, "Saturday"));
                dataValues.add(new PieEntry(sun, "Sunday"));

                PieDataSet pieDataSet = new PieDataSet(dataValues, "");
                pieDataSet.setColors(bpieChartColors);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.BLACK);
                bpiedayChart.setData(pieData);
                bpiedayChart.setDrawEntryLabels(true);
                bpieChart3.setVisibility(View.VISIBLE);

                Legend l = bpiedayChart.getLegend();
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
}