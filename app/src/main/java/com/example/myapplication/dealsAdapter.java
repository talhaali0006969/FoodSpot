package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class dealsAdapter extends RecyclerView.Adapter<dealsAdapter.dealsHolder> {

    private ArrayList<deals> dataset;
    private AdapterView.OnItemClickListener onItemClickListener;

    public dealsAdapter(ArrayList<deals> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public dealsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_deals , parent , false);

        return new dealsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull dealsHolder holder, int position) {

        deals currentItem = dataset.get(position);
        Picasso.get().load(currentItem.getProductImage()).into(holder.ivDeals);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null , holder.itemView , holder.getAdapterPosition() , 0 );
            }
        });

    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }

    public class dealsHolder extends RecyclerView.ViewHolder {

        ImageView ivDeals;


        public dealsHolder(@NonNull View itemView) {
            super(itemView);

            ivDeals = itemView.findViewById(R.id.ivDeals);


        }
    }
}












