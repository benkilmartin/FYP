//package com.example.fyp.SubjectsFolder;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//
//import com.example.fyp.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import javax.security.auth.Subject;
//
//public class SubjectsActivity extends AppCompatActivity {
//
//    private String userID;
//    private String subjectList="";
//    private Spinner subjectSpinner;
//    private FirebaseAuth firebaseAuth;
//    private DatabaseReference mDatabase;
//    private Button addButton;
//
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        firebaseAuth = FirebaseAuth.getInstance();
//        userID = firebaseAuth.getUid();
//        setValues();
//        return inflater.inflate(R.layout.fragment_subject,container,false);
//
//
//    }
//
//    private void setValues() {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child(userID).child("food").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Subject subject = new Subject(Subjects.class);
//                    //set spinner values to users current saved inputs
//                    int spinnerPosition;
//                    ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(getContext(), R.array.subjectspinneroptions, android.R.layout.simple_spinner_item);
//                    spinnerPosition = subjectAdapter.getPosition(subject.getSubjectList());
//                    subjectSpinner.setSelection(spinnerPosition);
//                }
//            }
//            @Override
//            public void onCancelled (@NonNull DatabaseError error){
//
//            }
//        });
//    }
//
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //link firebase
//        firebaseAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
////        subjectTextView = view.findViewById(R.id.editText);
//
//
//        subjectSpinner = view.findViewById(R.id.subject_spinner);
//        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.subjectspinneroptions, android.R.layout.simple_spinner_item);
//        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        subjectSpinner.setAdapter(subjectAdapter);
//        subjectSpinner.setOnItemSelectedListener(this);
//
//        addButton = view.findViewById(R.id.addBtn);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                updateDisplay();
////                mDatabase.child(firebaseAuth.getUid()).child("footprint").child("food").setValue(getFoodCO2());
//                // to do only update this data when user ants to save input ie allow them to check the value based on their input into the spinners before they save it to firebase
// //               Subject subject = new Subject(subjectList);
//                mDatabase.child(firebaseAuth.getUid()).child("food").setValue(subjectList);
//            }
//        });
//    }
//}