package com.example.fyp.UI.UserProfile;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fyp.R;
import com.example.fyp.UI.Goals.Goals;
import com.example.fyp.UI.Goals.GoalsEditFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class OtherUserProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String userID, btitle, bsubtitle, bthumbnail, CURRENT_STATE;
    private String receiverUserId = "";
    private FirebaseAuth firebaseAuth;
    DatabaseReference bFriendRequestRef, bFriendshipRef, parent;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    TextView titleTV, subtitleTV, bParentOptionsTV;
    Button bSendRequest, bDeclineRequest, bViewChildData, bMakeGoal;
    private ImageView bookIV;

    private DatabaseReference bDatabase;

    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2", ARG_PARAM3 = "param3", ARG_PARAM4 = "param4";
    private String bEditGoalTitle, bEditGoalDescription, bEditGoalFinishDate, bEditGoalID;

    public static OtherUserProfileFragment newInstance(String title, String description, String date, String id) {
        OtherUserProfileFragment fragment = new OtherUserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, description);
        args.putString(ARG_PARAM3, date);
        args.putString(ARG_PARAM4, id);
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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_user_profile, container, false);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        titleTV = view.findViewById(R.id.idTVpublisher);
        subtitleTV = view.findViewById(R.id.idTVNoOfPages);
        bParentOptionsTV = view.findViewById(R.id.kParentOptionsTV);
        bookIV = view.findViewById(R.id.idIVbook);
        bSendRequest = view.findViewById(R.id.kSendFriendRequest);
        bDeclineRequest = view.findViewById(R.id.kDeclineFriendRequest);
        bViewChildData = view.findViewById(R.id.kViewChildData);
        bMakeGoal = view.findViewById(R.id.kMakeGoal);
        firebaseAuth = FirebaseAuth.getInstance();
        bDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        receiverUserId = bEditGoalID;
        bFriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        bFriendshipRef = FirebaseDatabase.getInstance().getReference().child("Friendship");

        CURRENT_STATE = "not_friends";

        parent = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        parent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                boolean bIsParent = user.isParent();
                if (!bIsParent) {
                    bViewChildData.setVisibility(View.INVISIBLE);
                    bViewChildData.setEnabled(false);

                    bMakeGoal.setVisibility(View.INVISIBLE);
                    bMakeGoal.setEnabled(false);

                    bParentOptionsTV.setVisibility(View.INVISIBLE);
                    bParentOptionsTV.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        titleTV.setText(bEditGoalTitle);
        subtitleTV.setText(bEditGoalDescription);
        if (bEditGoalFinishDate.equalsIgnoreCase(" ")) {
            Glide.with(getContext()).load(R.drawable.ic_person).apply(new RequestOptions().override(200, 200)).into(bookIV);
        } else {
            Glide.with(getContext()).load(storageReference.child("images").child(bEditGoalFinishDate)).apply(new RequestOptions().override(200, 200)).into(bookIV);
        }

        bDeclineRequest.setVisibility(View.INVISIBLE);
        bDeclineRequest.setEnabled(false);

        bViewChildData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bViewChildData.setEnabled(false);
                parent = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                parent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        boolean bIsParent = user.isParent();
                        if (bIsParent) {
                            ChildDataFragment fragment = ChildDataFragment.newInstance(btitle, bsubtitle, bthumbnail, bEditGoalID);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.commit();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        bMakeGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bViewChildData.setEnabled(false);
                parent = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                parent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        boolean bIsParent = user.isParent();
                        if (bIsParent) {
                            AddGoalsChildFragment fragment = AddGoalsChildFragment.newInstance(btitle, bsubtitle, bthumbnail, bEditGoalID);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.commit();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        bSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bSendRequest.setEnabled(false);
                if (CURRENT_STATE.equals("not_friends")) {
                    parent = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                    parent.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            boolean bIsParent = user.isParent();
                            if (bIsParent) {
                                sendFriendRequest();
                            } else {
                                bSendRequest.setVisibility(View.INVISIBLE);
                                bSendRequest.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                if (CURRENT_STATE.equals("request_sent")) {
                    cancelFriendRequest();
                }
                if (CURRENT_STATE.equals("request_received")) {
                    acceptFriendRequest();
                }
                if (CURRENT_STATE.equals("friendship")) {
                    unfriend();
                }
            }
        });

        friendshipButtons();
        return view;
    }

    private void unfriend() {
        bFriendshipRef.child(userID).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            bFriendshipRef.child(receiverUserId).child(userID).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                bSendRequest.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                bSendRequest.setText("Send Friend Request");

                                                bDeclineRequest.setVisibility(View.INVISIBLE);
                                                bDeclineRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void acceptFriendRequest() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMMM yyyy");
        String bSaveDate = currentDate.format(calendar.getTime());
        bFriendshipRef.child(userID).child(receiverUserId).child("date").setValue(bSaveDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            bFriendshipRef.child(receiverUserId).child(userID).child("date").setValue(bSaveDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                bFriendRequestRef.child(userID).child(receiverUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    bFriendRequestRef.child(receiverUserId).child(userID).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        bSendRequest.setEnabled(true);
                                                                                        CURRENT_STATE = "friendship";
                                                                                        bSendRequest.setText("UnFriend");

                                                                                        bDeclineRequest.setVisibility(View.INVISIBLE);
                                                                                        bDeclineRequest.setEnabled(false);

                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    private void cancelFriendRequest() {

        bFriendRequestRef.child(userID).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            bFriendRequestRef.child(receiverUserId).child(userID).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                bSendRequest.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                bSendRequest.setText("Send Friend Request");
                                                bDeclineRequest.setVisibility(View.INVISIBLE);
                                                bDeclineRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendFriendRequest() {
        bFriendRequestRef.child(userID).child(receiverUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            bFriendRequestRef.child(receiverUserId).child(userID).child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                bSendRequest.setEnabled(true);
                                                CURRENT_STATE = "request_sent";
                                                bSendRequest.setText("Cancel Friend Request");

                                                bDeclineRequest.setVisibility(View.INVISIBLE);
                                                bDeclineRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void friendshipButtons() {
        bFriendRequestRef.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(receiverUserId)) {
                            String request_type = dataSnapshot.child(receiverUserId).child("request_type").getValue().toString();

                            if (request_type.equals("sent")) {
                                CURRENT_STATE = "request_sent";
                                bSendRequest.setText("Cancel Friend Request");

                                bDeclineRequest.setVisibility(View.INVISIBLE);
                                bDeclineRequest.setEnabled(false);
                            } else if (request_type.equals("received")) {
                                CURRENT_STATE = "request_received";
                                bSendRequest.setText("Accept Friend Request");

                                bDeclineRequest.setVisibility(View.VISIBLE);
                                bDeclineRequest.setEnabled(true);
                                bDeclineRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cancelFriendRequest();
                                    }
                                });
                            }
                        } else {
                            bFriendshipRef.child(userID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(receiverUserId)) {
                                                CURRENT_STATE = "friendship";
                                                bSendRequest.setText("Cancel Friendship");

                                                bDeclineRequest.setVisibility(View.INVISIBLE);
                                                bDeclineRequest.setEnabled(false);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}