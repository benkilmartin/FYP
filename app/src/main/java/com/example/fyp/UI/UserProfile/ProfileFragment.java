package com.example.fyp.UI.UserProfile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fyp.R;
import com.example.fyp.UI.Goals.GoalDisplayFragment;
import com.example.fyp.UserFolder.LoginPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;



public class ProfileFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;
    String userID, iKey;
    TextView uName, uEmail, editProfilePicture;
    Button lButton, sButton, fButton, flButton, glButton;
    ImageView pImage;
    public Uri iUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        uName= view.findViewById(R.id.UsernameTV);
        uEmail= view.findViewById(R.id.EmailTV);
        lButton = view.findViewById(R.id.sbookBtn);
        fButton = view.findViewById(R.id.flistBtn);
        flButton = view.findViewById(R.id.kfriendlistBtn);
        glButton = view.findViewById(R.id.kgoallistBtn);
        sButton = view.findViewById(R.id.saveBtn);
        pImage = view.findViewById(R.id.userProfilePicture);
        editProfilePicture = view.findViewById(R.id.editImageTV);
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    uEmail.setText(user.getEmail());
                    uName.setText(user.getUsernname());
                    if (getActivity() == null) {
                        return;
                    }
                    if(user.getpImage().equalsIgnoreCase(" ")){
                        Glide.with(getActivity()).load(R.drawable.ic_person).apply(new RequestOptions().override(200, 200)).into(pImage);
                    }else {
                        Glide.with(getActivity()).load(storageReference.child("images").child(user.getpImage())).apply(new RequestOptions().override(200, 200)).into(pImage);
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });

        lButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = new BookDisplayFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = OtherUserDisplayFragment.newInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "otheruserdisplayfragment");
                transaction.commit();
            }
        });

        flButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment  = FriendDisplayFragment.newInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "frienddisplayfragment");
                transaction.commit();

//                Fragment frag = new FriendDisplayFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_container, frag);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
            }
        });

        glButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment  = GoalDisplayFragment.newInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "goalsdisplayfragment");
                transaction.commit();

//                Fragment frag = new GoalDisplayFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_container, frag);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
            }
        });

        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String rKey = UUID.randomUUID().toString();
                iKey = rKey;
                StorageReference storagereference = storageReference.child("images/" + iKey);
                storagereference.putFile(iUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mDatabase.child("Users").child(userID).child("pImage").setValue(iKey);
                                Toast.makeText(getActivity(), "Profile Image Set", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed to set Profile Image", Toast.LENGTH_SHORT).show();
                            }
                        });
                sButton.setVisibility(View.GONE);
                editProfilePicture.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==-1 && data!=null && data.getData()!=null){
            iUri = data.getData();
            pImage.setImageURI(iUri);
            sButton.setVisibility(View.VISIBLE);
            editProfilePicture.setVisibility(View.GONE);
        }
    }
}