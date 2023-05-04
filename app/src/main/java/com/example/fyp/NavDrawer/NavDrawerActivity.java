package com.example.fyp.NavDrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fyp.R;
import com.example.fyp.UI.Calendar.CalActivity;
import com.example.fyp.UI.Goals.GoalsFragment;
import com.example.fyp.UI.TaskTimer.TimerFragment;
import com.example.fyp.UI.UserProfile.ProfileFragment;
import com.example.fyp.UI.UserProfile.User;
import com.example.fyp.UI.GoogleBooks.BookFragment;
import com.example.fyp.UI.Home.HomeFragment;
import com.example.fyp.UserFolder.LoginPage;
import com.example.fyp.UserFolder.SelectionPage;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dLayout;
    private TextView uName, uEmail;
    private ImageView pImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String userID;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);
        dLayout = findViewById(R.id.drawLayout);
        Toolbar toolbar = findViewById(R.id.tbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,dLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        dLayout.addDrawerListener(toggle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        uEmail = header.findViewById(R.id.navemail);
        uName = header.findViewById(R.id.navusername);
        pImage = header.findViewById(R.id.userProfilePictureNav);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    uEmail.setText(user.getEmail());
                    uName.setText(user.getUsernname());
                    storage = FirebaseStorage.getInstance();

                    if(user.getpImage().equalsIgnoreCase(" ")){
                        Glide.with(NavDrawerActivity.this).load(R.drawable.ic_person).apply(new RequestOptions().override(200, 200)).into(pImage);
                    }else {
                        storageReference = FirebaseStorage.getInstance().getReference().child("images").child(user.getpImage());
                        Glide.with(NavDrawerActivity.this).load(storageReference).apply(new RequestOptions().override(200, 200)).into(pImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menu_relation){
                Intent intent = new Intent(NavDrawerActivity.this, CalActivity.class);
                startActivity(intent);
            return true;
        }
        return false;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_calendar:
                intent = new Intent(NavDrawerActivity.this, CalActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_timer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TimerFragment()).commit();
                break;
            case R.id.nav_book:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BookFragment()).commit();
                break;
            case R.id.nav_goal:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GoalsFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_logout:
                firebaseAuth.signOut();
                intent = new Intent(NavDrawerActivity.this, SelectionPage.class);
                startActivity(intent);
                break;
        }
        dLayout.closeDrawer(GravityCompat.START);


        return true;
    }

}