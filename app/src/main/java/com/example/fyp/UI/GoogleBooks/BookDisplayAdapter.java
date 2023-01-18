package com.example.fyp.UI.GoogleBooks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.UserProfile.BookSavedExpanded;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookDisplayAdapter extends RecyclerView.Adapter<BookDisplayAdapter.BookViewHolder> {

    private ArrayList<Book> bookList;
    private Context bcontext;

    public BookDisplayAdapter(ArrayList<Book> bookList, Context bcontext) {
        this.bookList = bookList;
        this.bcontext = bcontext;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bnameTV.setText(book.getTitle());
        holder.bpublisherTV.setText(book.getPublisher());
        holder.bpageCountTV.setText("No of Pages : " + book.getPageCount());
        holder.bdateTV.setText(book.getPublishedDate());
        Picasso.get().load(book.getThumbnail()).into(holder.bbookIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bcontext, BookSavedExpanded.class);
                intent.putExtra("title", book.getTitle());
                intent.putExtra("subtitle", book.getSubtitle());
                intent.putExtra("authors", book.getAuthors());
                intent.putExtra("publisher", book.getPublisher());
                intent.putExtra("publishedDate", book.getPublishedDate());
                intent.putExtra("description", book.getDescription());
                intent.putExtra("pageCount", book.getPageCount());
                intent.putExtra("thumbnail", book.getThumbnail());
                intent.putExtra("previewLink", book.getPreviewLink());
                intent.putExtra("infoLink", book.getInfoLink());
                intent.putExtra("buyLink", book.getBuyLink());
                bcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bnameTV, bpublisherTV, bpageCountTV, bdateTV;
        ImageView bbookIV;

        public BookViewHolder(View itemView) {
            super(itemView);
            bnameTV = itemView.findViewById(R.id.knameTV);
            bpublisherTV = itemView.findViewById(R.id.kpublisherTV);
            bpageCountTV = itemView.findViewById(R.id.kpagedcountTV);
            bdateTV = itemView.findViewById(R.id.kdateTV);
            bbookIV = itemView.findViewById(R.id.kbookIV);
        }
    }
}
