package com.example.fyp.UI.Goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.GoogleBooks.BookDisplayAdapter;
import com.example.fyp.UI.UserProfile.BookDisplayFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GoalDisplayFragment extends Fragment implements GoalDisplayAdapter.ItemClickListener {
    FirebaseAuth firebaseAuth;
    private DatabaseReference bdatabaseRefernce;
    private ArrayList<Goals> goalsArrayList;
    private RecyclerView mRecyclerView;
    private String userID;
    private Button bgoalcompletedbutton;

    public static GoalDisplayFragment newInstance() {
        GoalDisplayFragment fragment = new GoalDisplayFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_display, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        goalsArrayList = new ArrayList<>();
        bgoalcompletedbutton = view.findViewById(R.id.kgoalcompletedbutton);


        initRecyclerView(view);
        bdatabaseRefernce = FirebaseDatabase.getInstance().getReference("Goals").child(userID);
        bdatabaseRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Goals goal = dataSnapshot.getValue(Goals.class);
                    if (goal.getProgress().equalsIgnoreCase("In Progress"))
                        goalsArrayList.add(goal);
                }
                initRecyclerView(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bgoalcompletedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new GoalDisplayCompletedFragment();
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
    public void onItemClick(Goals goals) {
        Fragment fragment = GoalsEditFragment.newInstance(goals.getTitle(), goals.getDescription(), goals.getDate(), goals.getStartDate(), goals.getProgress(), goals.getId());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // transaction.replace(R.id.frame_container, fragment, "detail_fragment");

        transaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("goalsdisplayfragment"));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.idRVBooks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        GoalDisplayAdapter adapter = new GoalDisplayAdapter(goalsArrayList, this);
        mRecyclerView.setAdapter(adapter);
    }
}