package com.example.myapplication;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ExtraDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvProductDescription;
    private ImageButton ibDecrement;
    private ImageButton ibIncrement;
    private TextView tvQuantity;
    private Button btnAddToCart;
    private com.example.myapplication.deals selectedProduct;
    private com.example.myapplication.DbHelperExtra dbHelper;
    private com.example.myapplication.DbHelperFa2 dbHelperFa2;
    private ImageButton ibFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        ibDecrement = findViewById(R.id.ibDecrement);
        ibIncrement = findViewById(R.id.ibIncrement);
        tvQuantity = findViewById(R.id.tvQuantity);

        btnAddToCart = findViewById(R.id.btnAddToCart);



        ibFav=findViewById(R.id.ibFav);
        dbHelperFa2=new com.example.myapplication.DbHelperFa2(ExtraDetailActivity.this);
        selectedProduct = (com.example.myapplication.deals) getIntent().getSerializableExtra("product");
        if(dbHelperFa2.isWallpaperFavorite(selectedProduct))
        {

            ibFav.setImageResource(R.drawable.ic_fill_favorite_24);
        }else
        {

            ibFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }


        ibFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (dbHelperFa2.isWallpaperFavorite(selectedProduct)) {
                    int deletedCount = dbHelperFa2.deleteFromCart(selectedProduct);
                    if (deletedCount > 0) {

                        ibFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    } else {
                        Toast.makeText(ExtraDetailActivity.this, "Unable to remove from Favourites", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    long insertedId = dbHelperFa2.addTOFav(selectedProduct);
                    if (insertedId > -1) {


                        ibFav.setImageResource(R.drawable.ic_fill_favorite_24);
                    } else {
                        Toast.makeText(ExtraDetailActivity.this, "Unable to add in Favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





        dbHelper = new com.example.myapplication.DbHelperExtra(ExtraDetailActivity.this);



        if (!selectedProduct.getProductImage().isEmpty()) {

            Picasso.get().load(selectedProduct.getProductImage()).into(ivProductImage);

        } else {
            ivProductImage.setImageResource(R.drawable.ic_baseline_remove_24);
        }

        tvProductName.setText(selectedProduct.getProductName());
        tvProductPrice.setText("Rs." + selectedProduct.getProductPrice());
        tvProductDescription.setText(selectedProduct.getProductDescription());

        ibIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int oldQty = Integer.parseInt(tvQuantity.getText().toString());

                if (oldQty < 10) {
                    int newQty = oldQty + 1;
                    tvQuantity.setText(String.valueOf(newQty));
                }
            }
        });

        ibDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int oldQty = Integer.parseInt(tvQuantity.getText().toString());

                if (oldQty > 1) {
                    int newQty = oldQty - 1;
                    tvQuantity.setText(String.valueOf(newQty));
                }

            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(tvQuantity.getText().toString());

                if (dbHelper.isProductIn(selectedProduct)) {
                    // update product in cart

                    dbHelper.updateProductIn(selectedProduct, quantity);

                    Toast.makeText(ExtraDetailActivity.this, "Product Quantity Updated ", Toast.LENGTH_SHORT).show();

                } else {

                    long cartId = dbHelper.addTOCar(selectedProduct, quantity);

                    if (cartId == -1) {

                        Toast.makeText(ExtraDetailActivity.this, "Unable to add in cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExtraDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}