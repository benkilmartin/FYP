package com.example.fyp.UI.Goals;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fyp.R;
import com.example.fyp.UI.UserProfile.FriendDisplayFragment;
import com.example.fyp.UI.UserProfile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class GoalsEditFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private DatabaseReference bdatabaseRefernce;
    private EditText bGoalTitle, bGoalDescription, bGoalDate;
    private Button bAddGoalButton;
    private Spinner bGoalSpinner;
    private String bGoalFinishDate, bStartDate, bUserID, bGoalProgress;
    private String bGoalProgressString = "";
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2", ARG_PARAM3 = "param3", ARG_PARAM4 = "param4", ARG_PARAM5 = "param5", ARG_PARAM6 = "param6";
    private String bEditGoalTitle, bEditGoalDescription, bEditGoalFinishDate, bEditGoalID;

    public static GoalsEditFragment newInstance(String title, String description, String date, String startdate, String progress, String id) {
        GoalsEditFragment fragment = new GoalsEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, description);
        args.putString(ARG_PARAM3, date);
        args.putString(ARG_PARAM4, id);
        args.putString(ARG_PARAM5, progress);
        args.putString(ARG_PARAM6, startdate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bEditGoalTitle = getArguments().getString(ARG_PARAM1);
            bEditGoalDescription = getArguments().getString(ARG_PARAM2);
            bEditGoalFinishDate = getArguments().getString(ARG_PARAM3);
            bEditGoalID = getArguments().getString(ARG_PARAM4);
            bGoalProgressString = getArguments().getString(ARG_PARAM5);
            bStartDate = getArguments().getString(ARG_PARAM6);

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals_edit, container, false);

        bUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bdatabaseRefernce = FirebaseDatabase.getInstance().getReference("Goals").child(bUserID).child(bEditGoalID);
        bGoalTitle = view.findViewById(R.id.kgoalTitle);
        bGoalDescription = view.findViewById(R.id.kgoalDescription);
        bGoalDate = view.findViewById(R.id.kgoalDate);
        bGoalSpinner = view.findViewById(R.id.kgoalProgress);
        bAddGoalButton = view.findViewById(R.id.kaddgoalBtn);


        bGoalTitle.setText(bEditGoalTitle);
        bGoalDescription.setText(bEditGoalDescription);
        bGoalDate.setText(bEditGoalFinishDate);

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.progress, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bGoalSpinner.setAdapter(goalAdapter);
        bGoalSpinner.setOnItemSelectedListener(this);
        bGoalProgressString = bGoalSpinner.getSelectedItem().toString();


        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMMM yyyy");
        bStartDate = currentDate.format(calendarDate.getTime());


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        bGoalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        bGoalDate.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                };
                new DatePickerDialog(getContext(), dateSetListener, year, monthOfYear, dayOfMonth).show();
            }
        });

        bAddGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titles = bGoalTitle.getText().toString();
                String descriptions = bGoalDescription.getText().toString();
                bGoalFinishDate = bGoalDate.getText().toString();
                bdatabaseRefernce.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("date", bGoalFinishDate);
                        updates.put("title", titles);
                        updates.put("description", descriptions);
                        updates.put("progress", bGoalProgressString);
                        updates.put("startDate", bStartDate);
                        updates.put("id", bEditGoalID);
                        bdatabaseRefernce.updateChildren(updates);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(getContext(), "Event Edited", Toast.LENGTH_SHORT).show();
                Fragment frag = new ProfileFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }

        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.kgoalProgress:
                bGoalProgressString = adapterView.getItemAtPosition(i).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}