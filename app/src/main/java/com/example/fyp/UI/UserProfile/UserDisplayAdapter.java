package com.example.fyp.UI.UserProfile;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fyp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserDisplayAdapter extends RecyclerView.Adapter<UserDisplayAdapter.BookViewHolder> {

    private ArrayList<User> buserList;
    private Context bcontext;
    private FirebaseStorage storage;
    private ItemClickListener clickListener;
    private StorageReference storageReference;

    public UserDisplayAdapter(ArrayList<User> buserList, ItemClickListener clickListener, Context bcontext) {
        this.buserList = buserList;
        this.clickListener = clickListener;
        this.bcontext = bcontext;
    }


    @NonNull
    @Override
    public UserDisplayAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new BookViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull UserDisplayAdapter.BookViewHolder holder, int position) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        holder.bnameTV.setText(buserList.get(position).getUsernname());
        holder.bpublisherTV.setText(buserList.get(position).getEmail());
        holder.bbookTV.setText(buserList.get(position).getpImage());
        holder.bIDTV.setText(buserList.get(position).getId());
        if (buserList.get(position).getpImage().equalsIgnoreCase(" ")) {
            Glide.with(bcontext).load(R.drawable.ic_person).apply(new RequestOptions().override(200, 200)).into(holder.bbookIV);
        } else {
            Glide.with(bcontext).load(storageReference.child("images").child(buserList.get(position).getpImage())).apply(new RequestOptions().override(200, 200)).into(holder.bbookIV);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(buserList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return buserList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bnameTV, bpublisherTV,bbookTV, bIDTV;
        ImageView bbookIV;

        public BookViewHolder(View itemView) {
            super(itemView);
            bnameTV = itemView.findViewById(R.id.knameTV);
            bpublisherTV = itemView.findViewById(R.id.kpublisherTV);
            bbookTV = itemView.findViewById(R.id.kbookTV);
            bIDTV = itemView.findViewById(R.id.kIDTV);
            bbookIV = itemView.findViewById(R.id.kbookIV);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(User user);
    }
}
