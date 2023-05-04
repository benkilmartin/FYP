package com.example.fyp.UI.Calendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.GoogleBooks.BookAdapter;
import com.example.fyp.UI.GoogleBooks.BookExpanded;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.BookViewHolder>{

    private List<Event> eventList;
    private Context bcontext;


//    public EventAdapter(@NonNull Context context, List<Event> events)
//    {
//        super(context, 0, events);
//    }

    public EventAdapter(List<Event> eventList, Context bcontext) {
        this.eventList = eventList;
        this.bcontext = bcontext;
    }


//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)    {
//
//        Event event = getItem(position);
//
//        if (convertView == null)
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
//
//        TextView eventCellTV = convertView.findViewById(R.id.eventitem);
//
//        String eventTitle = event.getSubject();
//        eventCellTV.setText(eventTitle);
//        return convertView;
//    }

    @NonNull
    @Override
    public EventAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventAdapter.BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.BookViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventCellTV.setText(event.getSubject());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bcontext, EventExpanded.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Subject", event.getSubject());
                intent.putExtra("Duration", event.getDuration());
                intent.putExtra("Tags", event.getTags());
                intent.putExtra("ID", event.getId());
                intent.putExtra("Time", event.getTime());
                intent.putExtra("Date", event.getDate());
                bcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView eventCellTV;

        public BookViewHolder(View itemView) {
            super(itemView);
            eventCellTV = itemView.findViewById(R.id.eventitem);
        }
    }
}
