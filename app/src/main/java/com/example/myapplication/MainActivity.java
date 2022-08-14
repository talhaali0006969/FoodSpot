package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rvCategories;
    private ArrayList<com.example.myapplication.Category> categoryList;
    private com.example.myapplication.CategoryAdapter categoryAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private Button btnRetry;
    private LinearLayout errorLayout;
    private MenuItem item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Categories");


        progressBar = findViewById(R.id.progressBar);
        rvCategories = findViewById(R.id.rvCategories);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        btnRetry = findViewById(R.id.btnRetry);

        errorLayout = findViewById(R.id.errorLayout);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





















        categoryList = new ArrayList<>();

        rvCategories.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        fetchCategoriesFromServer();


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCategoriesFromServer();
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCategoriesFromServer();
            }
        });

    }





    private void fetchCategoriesFromServer() {

        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        if (swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        }


        StringRequest request = new StringRequest(ApiConfig.MAIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");

                    if (status == 0) {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray categoriesArray = jsonResponse.getJSONArray("categories");
                        categoryList = new Gson().fromJson(categoriesArray.toString(), new TypeToken<ArrayList<com.example.myapplication.Category>>() {
                        }.getType());
                        categoryAdapter = new com.example.myapplication.CategoryAdapter(categoryList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                Intent intent = new Intent(MainActivity.this, com.example.myapplication.ProductsActivity.class);
                                intent.putExtra("category", categoryList.get(position));
                                startActivity(intent);
                            }
                        });

                        rvCategories.setAdapter(categoryAdapter);

                    }

                } catch (JSONException e) {
                    errorLayout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Unable to process server response ", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);




                error.printStackTrace();

                Toast.makeText(MainActivity.this, "Unable to get data from server ", Toast.LENGTH_SHORT).show();

            }
        });


        Volley.newRequestQueue(getApplicationContext()).add(request);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    }





