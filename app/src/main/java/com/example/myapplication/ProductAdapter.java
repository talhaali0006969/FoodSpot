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

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    private ArrayList<Product> dataSet;
    private ArrayList<Product> backUpDataSet;
    private AdapterView.OnItemClickListener onItemClickListener;



    public ProductAdapter(ArrayList<Product> dataSet, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
        this.backUpDataSet = new ArrayList<>(dataSet);

    }


    public void filter(String query) {
        dataSet.clear();
        if(query.isEmpty()) {
            dataSet.addAll(backUpDataSet);
        } else {
            for (int i=0;i<backUpDataSet.size(); i++){
                Product obj = backUpDataSet.get(i);
                if (obj.getProductName().toLowerCase().startsWith(query)) {
                    dataSet.add(obj);
                }
                else if(obj.getProductName().toUpperCase().startsWith(query))
                {
                    dataSet.add(obj);
                }

            }
        }
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_product, parent, false);

        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        Product currentItem = dataSet.get(position);

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
        return dataSet.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductDescription;
        TextView tvProductPrice;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);

        }
    }
}
