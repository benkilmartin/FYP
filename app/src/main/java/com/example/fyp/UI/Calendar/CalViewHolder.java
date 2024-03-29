package com.example.fyp.UI.Calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final CalAdapter.OnItemListener onItemListener;
    public CalViewHolder(@NonNull View itemView, CalAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days){
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    @Override
    public void onClick(View view){
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}
