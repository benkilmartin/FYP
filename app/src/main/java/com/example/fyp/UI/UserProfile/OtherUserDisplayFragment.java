package com.example.fyp.UI.UserProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.GoogleBooks.BookDisplayAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OtherUserDisplayFragment extends Fragment implements UserDisplayAdapter.ItemClickListener{
    FirebaseAuth firebaseAuth;
    private DatabaseReference bdatabaseRefernce;
    private ArrayList<User> buserArrayList;
    private RecyclerView mRecyclerView;
    private EditText mBookSearch;
    private Button sButton;
    String userID;

    public static OtherUserDisplayFragment newInstance() {
        OtherUserDisplayFragment fragment = new OtherUserDisplayFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_user_display, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        buserArrayList = new ArrayList<>();

        mBookSearch = view.findViewById(R.id.searchETa);
        sButton = view.findViewById(R.id.sbtn);

        mRecyclerView = view.findViewById(R.id.idRVBooks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        UserDisplayAdapter adapter = new UserDisplayAdapter(buserArrayList, this, getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setAdapter(adapter);
                buserArrayList.clear();
                if (mBookSearch.getText().toString().isEmpty()) {
                    mBookSearch.setError("Please Populate Field");
                } else {
                    String query = mBookSearch.getText().toString();
                    bdatabaseRefernce = FirebaseDatabase.getInstance().getReference("Users");
                    bdatabaseRefernce.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);

                                if (query.equalsIgnoreCase(user.getUsernname())) {
                                    buserArrayList.add(user);
                                    System.out.println(user.getId());
                                    System.out.println(user.getUsernname());
                                    System.out.println(user.getEmail());
                                    System.out.println(user.getpImage());
                                }
                            }
                            mRecyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(User user) {
        Fragment fragment = OtherUserProfileFragment.newInstance(user.getUsernname(), user.getEmail(), user.getpImage(), user.getId());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("otheruserdisplayfragment"));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    private void initRecyclerView(View view) {
//        mRecyclerView = view.findViewById(R.id.idRVBooks);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        UserDisplayAdapter adapter = new UserDisplayAdapter(buserArrayList, this, getContext());
//        mRecyclerView.setAdapter(adapter);
//    }
}