package com.example.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    public interface onCartUpdateListener{

        void onCartUpdate();
    }

    private ArrayList<CartItem> dataset;
    private DbHelper dbHelper;

    private onCartUpdateListener onCartUpdateListener;
    public CartAdapter(ArrayList<CartItem> dataset) {
        this.dataset = dataset;
    }

    public void setOnCartUpdateListener(CartAdapter.onCartUpdateListener onCartUpdateListener) {
        this.onCartUpdateListener = onCartUpdateListener;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (dbHelper== null){
            dbHelper = new DbHelper(parent.getContext());
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_cart , parent, false);
        return new CartHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {

        CartItem currentItem = dataset.get(position);

        Picasso.get().load(currentItem.getProduct().getProductImage())
                .into(holder.ivProductImage);

        holder.tvProductName.setText(currentItem.getProduct().getProductName());

        int qty = currentItem.getQuantity();
        int amount = Integer.parseInt(String.valueOf(currentItem.getProduct().getProductPrice())) * qty;

        holder.tvQuantity.setText(String.valueOf(qty));
        holder.tvAmount.setText("Rs."+amount);

        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemsRemoved = dbHelper.deleteFromCart(currentItem.getCartId());
                if (itemsRemoved>0){
                    dataset.remove(holder.getAdapterPosition());
                    // notifyDataSetChanged();
                    notifyItemRemoved(holder.getAdapterPosition());

                    if (onCartUpdateListener != null){
                        onCartUpdateListener.onCartUpdate();
                    }

                }else {
                    Toast.makeText(holder.ibDecrement.getContext(), "Unable to delete" , Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.ibIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int oldQty =  currentItem.getQuantity();
                if (oldQty <10){
                    int newQty = oldQty+1;

                    int itemsUpdated = dbHelper.setProductQuantity(currentItem.getCartId() , newQty);
                    if (itemsUpdated>0){

                        currentItem.setQuantity(newQty);
                        notifyItemChanged(holder.getAdapterPosition());
                        if (onCartUpdateListener != null){
                            onCartUpdateListener.onCartUpdate();
                        }
                    }else {
                        Toast.makeText(holder.itemView.getContext(), "Unable to update cart", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        holder.ibDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oldQty =  currentItem.getQuantity();
                if (oldQty > 1){
                    int newQty = oldQty - 1;
                    int itemsUpdated = dbHelper.setProductQuantity(currentItem.getCartId() , newQty);
                    if (itemsUpdated>0){

                        currentItem.setQuantity(newQty);
                        notifyItemChanged(holder.getAdapterPosition());
                        if (onCartUpdateListener != null){
                            onCartUpdateListener.onCartUpdate();
                        }
                    }else {
                        Toast.makeText(holder.itemView.getContext(), "Unable to update cart", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder{

        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvRemove ;
        TextView tvAmount ;
        ImageButton ibIncrement ;
        TextView tvQuantity;
        ImageButton ibDecrement ;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvRemove = itemView.findViewById(R.id.tvRemove);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ibIncrement = itemView.findViewById(R.id.ibIncrement);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ibDecrement = itemView.findViewById(R.id.ibDecrement);


        }
    }
}
