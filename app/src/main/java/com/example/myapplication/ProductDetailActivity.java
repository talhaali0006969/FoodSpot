package com.example.myapplication;

import android.annotation.SuppressLint;
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

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvProductDescription;
    private ImageButton ibDecrement;
    private ImageButton ibIncrement;
    private TextView tvQuantity;
    private Button btnAddToCart;
    private com.example.myapplication.Product selectedProduct;
    private com.example.myapplication.DbHelper dbHelper;


    private ImageButton ibFav;
    private com.example.myapplication.DbHelperFav dbHelperFav;
    private com.example.myapplication.ProductAdapter adapter;


    @SuppressLint("NotifyDataSetChanged")
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
        dbHelperFav=new com.example.myapplication.DbHelperFav(ProductDetailActivity.this);
        selectedProduct = (com.example.myapplication.Product) getIntent().getSerializableExtra("product");
        if(dbHelperFav.isWallpaperFavorite(selectedProduct))
        {

            ibFav.setImageResource(R.drawable.ic_fill_favorite_24);
        }else
        {

            ibFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }


            ibFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (dbHelperFav.isWallpaperFavorite(selectedProduct)) {
                        int deletedCount = dbHelperFav.deleteFromCart(selectedProduct);
                        if (deletedCount > 0) {
                            ibFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Unable to remove from Favourites", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        long insertedId = dbHelperFav.addTOFav(selectedProduct);
                        if (insertedId > -1) {

                            ibFav.setImageResource(R.drawable.ic_fill_favorite_24);
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Unable to add in Favourites", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });




        dbHelper = new com.example.myapplication.DbHelper(ProductDetailActivity.this);
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

                if (dbHelper.isProductInCart(selectedProduct)) {
                    // update product in cart

                    dbHelper.updateProductInCart(selectedProduct, quantity);

                    Toast.makeText(ProductDetailActivity.this, "Product Quantity Updated ", Toast.LENGTH_SHORT).show();

                } else {

                    long cartId = dbHelper.addTOCart(selectedProduct, quantity);

                    if (cartId == -1) {

                        Toast.makeText(ProductDetailActivity.this, "Unable to add in cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
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