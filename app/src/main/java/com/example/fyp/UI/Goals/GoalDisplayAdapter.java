package com.example.fyp.UI.Goals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;
import com.example.fyp.UI.GoogleBooks.Book;
import com.example.fyp.UI.UserProfile.BookSavedExpanded;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GoalDisplayAdapter extends RecyclerView.Adapter<GoalDisplayAdapter.BookViewHolder> {

    private ArrayList<Goals> goalsList;
    private ItemClickListener clickListener;

    public GoalDisplayAdapter(ArrayList<Goals> goalsList, ItemClickListener clickListener) {
        this.goalsList = goalsList;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public GoalDisplayAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalDisplayAdapter.BookViewHolder holder, int position) {
        holder.bnameTV.setText(goalsList.get(position).getTitle());
        holder.bpublisherTV.setText(goalsList.get(position).getDescription());
        holder.bpageCountTV.setText(goalsList.get(position).getProgress());
        holder.bdateTV.setText(goalsList.get(position).getDate());
        holder.bdateTVs.setText(goalsList.get(position).getStartDate());
        goalsList.get(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(goalsList.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return goalsList.size();
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bnameTV, bpublisherTV, bpageCountTV, bdateTV, bdateTVs;

        public BookViewHolder(View itemView) {
            super(itemView);
            bnameTV = itemView.findViewById(R.id.knameTV);
            bpublisherTV = itemView.findViewById(R.id.kpublisherTV);
            bpageCountTV = itemView.findViewById(R.id.kpagedcountTV);
            bdateTV = itemView.findViewById(R.id.kstartdate);
            bdateTVs = itemView.findViewById(R.id.kenddate);;
        }
    }

    public interface ItemClickListener {
        public void onItemClick(Goals goals);
    }
}

