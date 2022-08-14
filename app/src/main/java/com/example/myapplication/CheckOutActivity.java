package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {


    private EditText etDeliveryAddress;
    private TextView tvTotal;
    private User currentUser;
    private Button btnOrder;

    private   ArrayList<CartItem> cartItemList;
    private DbHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        etDeliveryAddress = findViewById(R.id.etDeliveryAddress);
        tvTotal = findViewById(R.id.tvTotal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnOrder = findViewById(R.id.btnOrder);

        currentUser = SessionHelper.getCurrentUser(CheckOutActivity.this);
        dbHelper = new DbHelper(CheckOutActivity.this);
        cartItemList = dbHelper.getAllCartItems();


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deliveryAddress = etDeliveryAddress.getText().toString().trim();
                if (deliveryAddress.isEmpty()) {
                    etDeliveryAddress.setError("Please enter the delivery Address");
                    etDeliveryAddress.requestFocus();
                    return;
                }
                String userId = String.valueOf(currentUser.getUserId());

                String paymentMethod="COD";

                final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StringRequest request = new StringRequest(Request.Method.POST, ApiConfig.Save_Order_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("mytag", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String message = jsonObject.getString("message");
                            if (status == 0) {

                                Toast.makeText(CheckOutActivity.this, message, Toast.LENGTH_SHORT).show();

                            } else {
                                dbHelper.ClearCart();
                                new AlertDialog.Builder(CheckOutActivity.this)
                                        .setTitle("Thank You")
                                        .setMessage("Your Order Submitted SuccessFully")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(CheckOutActivity.this,ExtraActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();


                                            }
                                        })
                                        .setCancelable(false)
                                        .show();




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CheckOutActivity.this, "unable to process server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CheckOutActivity.this, "unable to fetch data from server", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameterss = new HashMap<>();
                        float totalAmount = 0;
                        for (int i = 0; i < cartItemList.size(); i++) {
                            float amount = cartItemList.get(i).getProduct().getProductPrice() * cartItemList.get(i).getQuantity();
                            totalAmount = totalAmount + amount;
                        }
                        parameterss.put("cartList", new Gson().toJson(cartItemList));
                        parameterss.put("userId", String.valueOf(userId));
                        parameterss.put("deliveryAddress", deliveryAddress);

                        parameterss.put("totalAmount", String.valueOf(totalAmount));

                        return parameterss;
                    }
                };
                request.setShouldCache(false);
                request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));

                Volley.newRequestQueue(
                        CheckOutActivity.this).add(request);



            }
        });
        calculateTotal();

    }

    public void calculateTotal() {
        float totalAmount = 0;
        for (int i = 0; i < cartItemList.size(); i++) {
            float amount = cartItemList.get(i).getProduct().getProductPrice() * cartItemList.get(i).getQuantity();
            totalAmount = totalAmount + amount;
        }
        tvTotal.setText( "Rs."+  String.valueOf(totalAmount));
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



