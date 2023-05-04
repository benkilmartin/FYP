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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp.R;
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.GoogleBooks.BookAdapter;
import com.example.fyp.UI.GoogleBooks.BookDisplayAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BookDisplayFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    private DatabaseReference bdatabaseRefernce;
    private ArrayList<Book> bookArrayList;
    private RecyclerView mRecyclerView;
    private String userID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_display,container,false);
        mRecyclerView = view.findViewById(R.id.idRVBooks);
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        bookArrayList = new ArrayList<>();
        BookDisplayAdapter adapter = new BookDisplayAdapter(bookArrayList, getActivity());
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        bdatabaseRefernce = FirebaseDatabase.getInstance().getReference("Books").child(userID);
        bdatabaseRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    bookArrayList.add(book);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}