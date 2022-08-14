package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private TextView tvFavorite;
    private RecyclerView rvFavorite;
    private ArrayList<Product> fAVORITEwALLPAPER;
    private com.example.myapplication.ProductAdapter adapter;
    private com.example.myapplication.DbHelperFav dbHelperFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle("Favorites");

        rvFavorite=findViewById(R.id.rvFavorite);
        tvFavorite=findViewById(R.id.tvFavorite);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelperFav=new com.example.myapplication.DbHelperFav(FavoriteActivity.this);

        fAVORITEwALLPAPER=dbHelperFav.getAllCartItems();

        if(fAVORITEwALLPAPER.size()==0)
        {
            tvFavorite.setVisibility(View.VISIBLE);
        }

        else
        {
            tvFavorite.setVisibility(View.GONE);

            adapter = new com.example.myapplication.ProductAdapter(fAVORITEwALLPAPER, new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    com.example.myapplication.Product selectedproduct=fAVORITEwALLPAPER.get(position);
                    Intent intent=new Intent(FavoriteActivity.this, com.example.myapplication.ProductDetailActivity.class);
                    intent.putExtra("product",selectedproduct);
                    startActivity(intent);
                }
            });
            rvFavorite.setLayoutManager(new GridLayoutManager(FavoriteActivity.this,2));
            rvFavorite.setAdapter(adapter);


        }


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