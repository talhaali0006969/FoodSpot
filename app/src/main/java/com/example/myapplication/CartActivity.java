package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private com.example.myapplication.CartAdapter cartAdapter;
    private ArrayList<com.example.myapplication.CartItem> cartItemList;
    private com.example.myapplication.DbHelper dbHelper ;
    private TextView tvTotal;
    private TextView tvCheckout;
    private static final int LOGIN_REQUEST_CODE=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotal);
        tvCheckout=findViewById(R.id.tvCheckOut);
        dbHelper=new DbHelper(CartActivity.this);
        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cartCount=dbHelper.getCartCount();

                if(com.example.myapplication.SessionHelper.getCurrentUser(CartActivity.this) == null)
                {
                    Intent mainInent=new Intent(CartActivity.this, com.example.myapplication.LoginActivity.class);
                    startActivityForResult(mainInent,LOGIN_REQUEST_CODE);
                }
                else if(cartCount==0){
                    Toast.makeText(CartActivity.this,"Add Product First in Cart",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(CartActivity.this, com.example.myapplication.CheckOutActivity.class));

                }

            }
        });

        setTitle("Cart Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new com.example.myapplication.DbHelper(CartActivity.this);

        cartItemList = dbHelper.getAllCartItems();
        cartAdapter = new com.example.myapplication.CartAdapter(cartItemList);

        rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        rvCart.setAdapter(cartAdapter);

        cartAdapter.setOnCartUpdateListener(new com.example.myapplication.CartAdapter.onCartUpdateListener() {
            @Override
            public void onCartUpdate() {
                calculateTotal();
            }
        });

        calculateTotal();

    }

    private void calculateTotal() {

        int total = 0;
        for (int i = 0 ; i<cartItemList.size(); i++ ){

            int amount = Integer.parseInt(String.valueOf(cartItemList.get(i).getProduct().getProductPrice())) * cartItemList.get(i).getQuantity();

            total= total+amount;
        }
        tvTotal.setText("Total : Rs." + NumberFormat.getNumberInstance().format(total));
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==LOGIN_REQUEST_CODE)
        {

            if(resultCode==RESULT_OK)
            {
                startActivity(new Intent(CartActivity.this, com.example.myapplication.CheckOutActivity.class));

            }

        }
    }
}