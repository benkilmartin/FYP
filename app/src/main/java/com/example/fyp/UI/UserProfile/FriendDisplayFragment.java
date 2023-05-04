package com.example.fyp.UI.UserProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.Goals.GoalDisplayAdapter;
import com.example.fyp.UI.Goals.GoalDisplayFragment;
import com.example.fyp.UI.Goals.GoalsEditFragment;
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.GoogleBooks.BookDisplayAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FriendDisplayFragment extends Fragment implements FriendDisplayAdapter.ItemClickListener {
    FirebaseAuth firebaseAuth;
    private DatabaseReference bdatabaseRefernce;
    private ArrayList<User> buserArrayList;
    private ArrayList<String> friendNamess = new ArrayList<>();
    private RecyclerView mRecyclerView;
    DatabaseReference bFriendshipRef, UserRef;
    private String userID;

    public static FriendDisplayFragment newInstance() {
        FriendDisplayFragment fragment = new FriendDisplayFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_display, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        buserArrayList = new ArrayList<>();


        initRecyclerView(view);
        bFriendshipRef = FirebaseDatabase.getInstance().getReference().child("Friendship").child(userID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");


        bdatabaseRefernce = FirebaseDatabase.getInstance().getReference("Friendship").child(userID);
        bdatabaseRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshots : snapshot.getChildren()) {
                    bdatabaseRefernce = FirebaseDatabase.getInstance().getReference("Users");
                    bdatabaseRefernce.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String key = dataSnapshot.getKey();
                                friendNamess.add(key);
                                System.out.println(friendNamess);
                                if (dataSnapshots.getKey().equals(dataSnapshot.getKey())) {
                                    User user = dataSnapshot.getValue(User.class);
                                    buserArrayList.add(user);
                                }
                            }
                            initRecyclerView(view);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    @Override
    public void onItemClick(User user) {
        Fragment fragment = OtherUserProfileFragment.newInstance(user.getUsernname(), user.getEmail(), user.getpImage(), user.getId());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("frienddisplayfragment"));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.idRVBooks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        FriendDisplayAdapter adapter = new FriendDisplayAdapter(buserArrayList, this, getContext());
        mRecyclerView.setAdapter(adapter);


    }
}