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

public class NewArrivalAdapter  extends  RecyclerView.Adapter<NewArrivalAdapter.NewArrivalHolder>{

    private ArrayList<com.example.myapplication.NewArrival> dataset;
    private AdapterView.OnItemClickListener onItemClickListener;

    public NewArrivalAdapter(ArrayList<com.example.myapplication.NewArrival> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NewArrivalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_newarrival , parent , false);

        return new NewArrivalHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewArrivalHolder holder, int position) {

        com.example.myapplication.NewArrival currentItem = dataset.get(position);

        Picasso.get().load(currentItem.getProductImage()).into(holder.ivProductImage);
        holder.tvProductName.setText(currentItem.getProductName());
        holder.tvProductDescription.setText(currentItem.getProductDescription());
        holder.tvProductPrice.setText("Rs:" +currentItem.getProductPrice());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null , holder.itemView , holder.getAdapterPosition(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class NewArrivalHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductDescription;
        TextView tvProductPrice;

        public NewArrivalHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);

        }
    }

}
