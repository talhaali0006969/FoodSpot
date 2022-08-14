package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class NewArrivalActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView rvNewArrival;
    private ArrayList<com.example.myapplication.NewArrival> NewArrivalList;
    private com.example.myapplication.NewArrivalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_arrival);
        progressBar=findViewById(R.id.progressBar);
        rvNewArrival=findViewById(R.id.rvNewArrival);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("New Dishes");
        fetchNewArrival();
    }
    private void fetchNewArrival()
    {

        NewArrivalList=new ArrayList<>();
        String dealsUrl="https://sherlocked00069.000webhostapp.com/ShopEasy/categoryDetail.php?category_id=14";
        StringRequest request = new StringRequest(dealsUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");

                    if (status==0){
                        String message = jsonResponse.getString("message");
                        Toast.makeText(NewArrivalActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {

                        JSONArray categoriesArray = jsonResponse.getJSONArray("products");
                        NewArrivalList = new Gson().fromJson(categoriesArray.toString(), new TypeToken<ArrayList<com.example.myapplication.NewArrival>>(){}.getType());
                        rvNewArrival.setLayoutManager(new LinearLayoutManager(NewArrivalActivity.this));
                        adapter = new com.example.myapplication.NewArrivalAdapter(NewArrivalList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                Intent intent = new Intent(NewArrivalActivity.this , com.example.myapplication.NewArrivalDetailActivity.class);
                                intent.putExtra("product", NewArrivalList.get(position) );

                                startActivity(intent);

                            }
                        });

                        rvNewArrival.setAdapter(adapter);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(NewArrivalActivity.this, "Unable to process server response ", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                error.printStackTrace();

                Toast.makeText(NewArrivalActivity.this, "Unable to get data from server ", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(request);
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