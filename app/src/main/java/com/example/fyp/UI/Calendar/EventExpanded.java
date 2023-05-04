package com.example.fyp.UI.Calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.GoogleBooks.BookAdapter;
import com.example.fyp.UI.Home.HomeFragment;
import com.example.fyp.UI.UserProfile.AddGoalsChildFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventExpanded extends AppCompatActivity {
    private String bUserID, bEventTitle, bEventTags, bEventID, bEventDate, bEventTime;
    private DatabaseReference bdatabaseRefernce;
    private int bEventDuration;
    private TextView bEventSubjectTV, bEventDurationTV, bEventDescriptionTV;
    private RecyclerView bEventBookDisplay;
    private RequestQueue mRequestQueue;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause, mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private ArrayList<Book> bookArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_expanded);
        bEventSubjectTV = findViewById(R.id.kEventSubject);
        bEventDescriptionTV = findViewById(R.id.kEventDescription);
        bEventDurationTV = findViewById(R.id.kEventDuration);
        bEventBookDisplay = findViewById(R.id.kEventBookDisplay);

        bEventTitle = getIntent().getStringExtra("Subject");
        bEventTags = getIntent().getStringExtra("Tags");
        bEventDuration = getIntent().getIntExtra("Duration", 0);
        bEventID = getIntent().getStringExtra("ID");
        bEventDate = getIntent().getStringExtra("Date");
        bEventTime = getIntent().getStringExtra("Time");

        bEventSubjectTV.setText("Title: " + bEventTitle);
        bEventDescriptionTV.setText("Description: " + bEventTags);
        bEventDurationTV.setText("Duration: " + bEventDuration + " Hour");

        bUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bdatabaseRefernce = FirebaseDatabase.getInstance().getReference("Events").child(bUserID).child(bEventTitle).child(bEventID);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);


        setTime();

        bookArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.getCache().clear();
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + bEventTags;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String thumbnail = "NoLink";
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        String subtitle = volumeObj.optString("subtitle");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        JSONObject thumbnailUrlObject = volumeObj.optJSONObject("imageLinks");
                        if (thumbnailUrlObject != null && thumbnailUrlObject.has("thumbnail")) {
                            thumbnail = thumbnailUrlObject.getString("thumbnail");
                        }
//                        thumbnailUrlObject.optString("thumbnail");
                        thumbnail = thumbnail.substring(0, 4) + 's' + thumbnail.substring(4);
                        System.out.println(thumbnail);
                        String previewLink = volumeObj.optString("previewLink");
                        String infoLink = volumeObj.optString("infoLink");
                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                        String buyLink = saleInfoObj.optString("buyLink");
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        }
                        Book book = new Book(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                        bookArrayList.add(book);
                        BookAdapter adapter = new BookAdapter(bookArrayList, getApplicationContext());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        bEventBookDisplay.setLayoutManager(linearLayoutManager);
                        bEventBookDisplay.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(booksObjrequest);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void setTime() {
        long milliseconds;
        resetTimer();
        if (bEventDuration == 1) {
            milliseconds = 6000;
            mStartTimeInMillis = milliseconds;
            resetTimer();
        } else if (bEventDuration == 2) {
            milliseconds = 7200000;
            mStartTimeInMillis = milliseconds;
            resetTimer();
        } else if (bEventDuration == 3) {
            milliseconds = 10800000;
            mStartTimeInMillis = milliseconds;
            resetTimer();
        }
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
                bdatabaseRefernce.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Object> updates = new HashMap<>();
                        Boolean completed = true;
                        updates.put("completed", completed);
                        updates.put("date", bEventDate);
                        updates.put("duration", bEventDuration);
                        updates.put("id", bEventID);
                        updates.put("subject", bEventTitle);
                        updates.put("tags", bEventTags);
                        updates.put("time", bEventTime);
                        bdatabaseRefernce.updateChildren(updates);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Fragment fragment = new HomeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    public void updateWatchInterface() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

}