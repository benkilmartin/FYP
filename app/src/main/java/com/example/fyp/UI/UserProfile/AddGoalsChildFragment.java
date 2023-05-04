package com.example.fyp.UI.UserProfile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fyp.R;
import com.example.fyp.UI.Goals.Goals;
import com.example.fyp.UI.Home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddGoalsChildFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private EditText bGoalTitle, bGoalDescription, bGoalDate;
    private Button bAddGoalButton;
    private String bGoalFinishDate, bStartDate, bUserID;
    private String bGoalProgressString="In Progress";
    DatabaseReference bGoalID;
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2", ARG_PARAM3 = "param3", ARG_PARAM4 = "param4";
    private String bChildTitle, bChilddescription, bChildThumbnail, bChildID;

    public static AddGoalsChildFragment newInstance(String btitle, String bsubtitle, String bthumbnail, String receiverUserId) {
        AddGoalsChildFragment fragment = new AddGoalsChildFragment();
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        bUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bGoalID = FirebaseDatabase.getInstance().getReference();
        bGoalTitle = view.findViewById(R.id.kgoalTitle);
        bGoalDescription = view.findViewById(R.id.kgoalDescription);
        bGoalDate = view.findViewById(R.id.kgoalDate);
        bAddGoalButton = view.findViewById(R.id.kaddgoalBtn);

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
                String ID = bGoalID.push().getKey();
                String titles = bGoalTitle.getText().toString();
                String descriptions = bGoalDescription.getText().toString();
                bGoalFinishDate = bGoalDate.getText().toString();
                Goals goals = new Goals(titles, descriptions, bGoalFinishDate, bStartDate, bGoalProgressString, ID);
                FirebaseDatabase.getInstance().getReference("Goals").child(bChildID).child(ID).setValue(goals);
                Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();
                Fragment frag = new ProfileFragment();
                FragmentManager fragmentManager  = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, frag);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case R.id.kgoalProgress:
                bGoalProgressString = adapterView.getItemAtPosition(i).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}