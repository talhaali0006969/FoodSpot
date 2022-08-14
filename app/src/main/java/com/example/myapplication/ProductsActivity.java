package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rvProducts;
    private ArrayList<Product> productsList;
    private com.example.myapplication.ProductAdapter productAdapter ;
    private com.example.myapplication.Category selectedCategory;
    private Button btnVoiceSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        progressBar = findViewById(R.id.progressBar);
        rvProducts = findViewById(R.id.rvProducts);





        SearchView searchView = findViewById(R.id.btnSearch);
        searchView.onActionViewExpanded(); //new Added line
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Specific Products");

        if(!searchView.isFocused()) {
            searchView.clearFocus();
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.filter(newText);
                return false;
            }
        });










        rvProducts.setLayoutManager( new LinearLayoutManager(ProductsActivity.this));
        selectedCategory = (com.example.myapplication.Category) getIntent().getSerializableExtra("category");

        setTitle(selectedCategory.getCatName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);













































        FetchProductFromServer();

    }

    private void FetchProductFromServer() {

        progressBar.setVisibility(View.VISIBLE);
        String url = ApiConfig.PRODUCT_URL +"?category_id="+ selectedCategory.getCatId();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");
                    if (status==0){
                        String message = jsonResponse.getString("message");
                        Toast.makeText(ProductsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else {

                        JSONArray productArray = jsonResponse.getJSONArray("products");
                        productsList = new Gson().fromJson(productArray.toString(), new TypeToken<ArrayList<Product>>(){}.getType());

                        productAdapter  = new com.example.myapplication.ProductAdapter(productsList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                Intent intent = new Intent(ProductsActivity.this , ProductDetailActivity.class);
                                intent.putExtra("product", productsList.get(position) );

                                startActivity(intent);
                            }
                        });
                        rvProducts.setAdapter(productAdapter);

                    }
                } catch (JSONException e) {
                    Toast.makeText(ProductsActivity.this, "Unable to process server response ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}