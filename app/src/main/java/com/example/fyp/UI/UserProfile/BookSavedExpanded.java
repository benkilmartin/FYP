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

import com.example.fyp.R;
import com.example.fyp.UI.GoogleBooks.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookSavedExpanded extends AppCompatActivity {
    String userID, btitle, bsubtitle, bpublisher, bpublishedDate, bdescription, bthumbnail, bpreviewLink, binfoLink, bbuyLink;
    int bpageCount;
    private ArrayList<String> bauthors;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    Button previewBtn, buyBtn;
    private ImageView bookIV;

    private DatabaseReference bDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_saved_expanded);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        titleTV = findViewById(R.id.idTVTitle);
        subtitleTV = findViewById(R.id.idTVSubTitle);
        publisherTV = findViewById(R.id.idTVpublisher);
        descTV = findViewById(R.id.idTVDescription);
        pageTV = findViewById(R.id.idTVNoOfPages);
        publishDateTV = findViewById(R.id.idTVPublishDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        buyBtn = findViewById(R.id.idBtnBuy);
        bookIV = findViewById(R.id.idIVbook);
        firebaseAuth = FirebaseAuth.getInstance();
        bDatabase = FirebaseDatabase.getInstance().getReference();

        btitle = getIntent().getStringExtra("title");
        bsubtitle = getIntent().getStringExtra("subtitle");
        bpublisher = getIntent().getStringExtra("publisher");
        bpublishedDate = getIntent().getStringExtra("publishedDate");
        bdescription = getIntent().getStringExtra("description");
        bpageCount = getIntent().getIntExtra("pageCount", 0);
        bthumbnail = getIntent().getStringExtra("thumbnail");
        bpreviewLink = getIntent().getStringExtra("previewLink");
        binfoLink = getIntent().getStringExtra("infoLink");
        bbuyLink = getIntent().getStringExtra("buyLink");

        titleTV.setText(btitle);
        subtitleTV.setText(bsubtitle);
        publisherTV.setText(bpublisher);
        publishDateTV.setText("Published On : " + bpublishedDate);
        descTV.setText(bdescription);
        pageTV.setText("No Of Pages : " + bpageCount);
        Picasso.get().load(bthumbnail).into(bookIV);

        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bpreviewLink.isEmpty()){
                    Toast.makeText(BookSavedExpanded.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri uri = Uri.parse(bpreviewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Books").child(userID);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Book book = dataSnapshot.getValue(Book.class);
                            if(book.getDescription().equalsIgnoreCase(bdescription)){
                                dataSnapshot.getRef().removeValue();
                                Toast.makeText(BookSavedExpanded.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(BookSavedExpanded.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}