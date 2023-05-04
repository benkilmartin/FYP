package com.example.fyp.UI.UserProfile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fyp.R;
import com.example.fyp.UI.Calendar.CalUtils;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

public class UserProfileExpanded extends AppCompatActivity {
    String userID, btitle, bsubtitle, bthumbnail, CURRENT_STATE;
    private String receiverUserId = "";
    private FirebaseAuth firebaseAuth;
    DatabaseReference bFriendRequestRef, bFriendshipRef, parent;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    TextView titleTV, subtitleTV;
    Button bSendRequest, bDeclineRequest, bViewChildData, bMakeGoal;
    private ImageView bookIV;

    private DatabaseReference bDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_expanded);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        titleTV = findViewById(R.id.idTVpublisher);
        subtitleTV = findViewById(R.id.idTVNoOfPages);
        bookIV = findViewById(R.id.idIVbook);
        bSendRequest = findViewById(R.id.kSendFriendRequest);
        bDeclineRequest = findViewById(R.id.kDeclineFriendRequest);
        bViewChildData = findViewById(R.id.kViewChildData);
        bMakeGoal = findViewById(R.id.kMakeGoal);
        firebaseAuth = FirebaseAuth.getInstance();
        bDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        bFriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        bFriendshipRef = FirebaseDatabase.getInstance().getReference().child("Friendship");

        receiverUserId = getIntent().getStringExtra("id");
        btitle = getIntent().getStringExtra("title");
        bsubtitle = getIntent().getStringExtra("subtitle");
        bthumbnail = getIntent().getStringExtra("thumbnail");
        CURRENT_STATE = "not_friends";


        titleTV.setText(btitle);
        subtitleTV.setText(bsubtitle);
        if (bthumbnail.equalsIgnoreCase(" ")) {
            Glide.with(getApplicationContext()).load(R.drawable.ic_person).apply(new RequestOptions().override(200, 200)).into(bookIV);
        } else {
            Glide.with(getApplicationContext()).load(storageReference.child("images").child(bthumbnail)).apply(new RequestOptions().override(200, 200)).into(bookIV);
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
                            ChildDataFragment fragment = ChildDataFragment.newInstance(btitle,  bsubtitle, bthumbnail, receiverUserId);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, fragment);
                            transaction.commit();
                        } else {
                            bViewChildData.setVisibility(View.INVISIBLE);
                            bViewChildData.setEnabled(false);
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
                            AddGoalsChildFragment fragment = AddGoalsChildFragment.newInstance(btitle,  bsubtitle, bthumbnail, receiverUserId);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, fragment);
                            transaction.commit();
                        } else {
                            bViewChildData.setVisibility(View.INVISIBLE);
                            bViewChildData.setEnabled(false);
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
                            System.out.println(bIsParent);
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
        System.out.println(bSaveDate);
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
}
