package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tilEmail;
    private EditText etEmail;
    private TextInputLayout tilPassword;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private ProgressBar progressBar;
    private static final int SIGN_UP_CODE=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tilEmail=findViewById(R.id.tilEmail);
        etEmail=findViewById(R.id.etEmail);
        tilPassword=findViewById(R.id.tilPassword);
        etPassword=findViewById(R.id.etPassword);
        tvSignup=findViewById(R.id.tvSignup);
        btnLogin=findViewById(R.id.btnLogin);
        progressBar=findViewById(R.id.progressBar);


        getSupportActionBar().hide();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=etEmail.getText().toString().trim();
                String password=etPassword.getText().toString().trim();

                tilPassword.setErrorEnabled(false);
                tilEmail.setErrorEnabled(false);


                if(email.isEmpty())
                {
                    tilEmail.setErrorEnabled(true);
                    tilEmail.setError("Enter Email !");

                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    tilEmail.setErrorEnabled(true);
                    tilEmail.setError("Enter Valid Email !");
                }

                else if(password.isEmpty())
                {
                    tilPassword.setErrorEnabled(true);
                    tilPassword.setError("Enter Password !");

                }
                else if(password.length()<4)
                {
                    tilPassword.setErrorEnabled(true);
                    tilPassword.setError("Enter Minimum 4 character Password !");

                }

                else{
                    login(email,password);
                }




        }
        });


        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivityForResult(mainIntent,SIGN_UP_CODE);


            }
        });


    }
    private void login(String email,String password)

    {
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        StringRequest request=new StringRequest(Request.Method.POST, ApiConfig.Login_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    int status = jsonResponse.getInt("status");

                    if (status == 0) {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        JSONObject userObject=jsonResponse.getJSONObject("user");
                        User user=new Gson().fromJson(userObject.toString(),User.class);
                        SessionHelper.setCurrentUser(LoginActivity.this,user);
                        setResult(RESULT_OK);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                btnLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,"unable to fetch data from server",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("email",email);
                params.put("password",password);




                return params;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(3000,0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));

        Volley.newRequestQueue(LoginActivity.this).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_UP_CODE) {

            setResult(resultCode);
            finish();

        }
    }
}