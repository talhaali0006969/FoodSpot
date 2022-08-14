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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private ArrayList<com.example.myapplication.Category> dataset;
    private AdapterView.OnItemClickListener onItemClickListener;

    public CategoryAdapter(ArrayList<com.example.myapplication.Category> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_category , parent , false);

        return new CategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

        com.example.myapplication.Category currentItem = dataset.get(position);
        Picasso.get().load(currentItem.getCatImage()).into(holder.ivCatImage);
        holder.tvCatName.setText(currentItem.getCatName());

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

    public class CategoryHolder extends RecyclerView.ViewHolder {

        ImageView ivCatImage;
        TextView  tvCatName;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            ivCatImage = itemView.findViewById(R.id.ivCatImage);
            tvCatName = itemView.findViewById(R.id.tvCatName);

        }
    }
}
