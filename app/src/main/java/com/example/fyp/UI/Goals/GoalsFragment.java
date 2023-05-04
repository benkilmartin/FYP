package com.example.fyp.UI.Goals;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp.R;
import com.example.fyp.UI.Calendar.CalUtils;
import com.example.fyp.UI.Calendar.Event;
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.GoogleBooks.BookAdapter;
import com.example.fyp.UI.Home.HomeFragment;
import com.example.fyp.UI.UserProfile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class GoalsFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private EditText bGoalTitle, bGoalDescription, bGoalDate;
    private Button bAddGoalButton;
    private String bGoalFinishDate, bStartDate, bUserID;
    private String bGoalProgressString="In Progress";
    DatabaseReference bGoalID;
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
                FirebaseDatabase.getInstance().getReference("Goals").child(bUserID).child(ID).setValue(goals);
                Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();
                Fragment frag = new HomeFragment();
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