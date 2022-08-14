package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout tilName;
    private EditText etName;
    private TextInputLayout tilEmail;
    private EditText etEmail;
    private TextInputLayout tilPassword;
    private EditText etPassword;
    private TextInputLayout tilMobile;
    private EditText etMobile;
    private Button btnSignup;
    private ProgressBar progressBar;
    private TextView tvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tilName=findViewById(R.id.tilFullName);
        etName=findViewById(R.id.etFullName);
        tilMobile=findViewById(R.id.tilMobile);
        etMobile=findViewById(R.id.etMobile);
        tilEmail=findViewById(R.id.tilEmail);
        etEmail=findViewById(R.id.etEmail);
        tilPassword=findViewById(R.id.tilPassword);
        etPassword=findViewById(R.id.etPassword);
        tvLogin=findViewById(R.id.tvLogin);
        progressBar=findViewById(R.id.progressBar);
        btnSignup=findViewById(R.id.btnSignUp);

        getSupportActionBar().hide();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName= etName.getText().toString().trim();
                String email=etEmail.getText().toString().trim();
                String password=etPassword.getText().toString().trim();
                String mobileNumber=etMobile.getText().toString().trim();


                tilMobile.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilPassword.setErrorEnabled(false);
                tilEmail.setErrorEnabled(false);


                if(fullName.isEmpty())
                {
                    tilName.setErrorEnabled(true);
                    tilName.setError("Enter Name !");

                }else if(email.isEmpty())
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
                else  if(mobileNumber.isEmpty())
                {
                    tilMobile.setErrorEnabled(true);
                    tilMobile.setError("Enter Mobile Number !");
                }
                else
                {
                    signUp(fullName,email,password,mobileNumber);
                }

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();

            }
        });


    }
    private void  signUp(String fullName, String email, String password,String mobileNumber)
    {
        progressBar.setVisibility(View.VISIBLE);
        btnSignup.setVisibility(View.GONE);
        StringRequest request=new StringRequest(Request.Method.POST, ApiConfig.SignUp_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    int status = jsonResponse.getInt("status");

                    if (status == 0) {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "SIGNUP SUCCESSFUL !", Toast.LENGTH_SHORT).show();

                        JSONObject userObject=jsonResponse.getJSONObject("user");
                        User user=new Gson().fromJson(userObject.toString(),User.class);
                        SessionHelper.setCurrentUser(SignUpActivity.this,user);
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
                btnSignup.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("fullName",fullName);
                params.put("email",email);
                params.put("password",password);
                params.put("mobileNumber",mobileNumber);



                return params;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(3000,0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));

        Volley.newRequestQueue(SignUpActivity.this).add(request);

    }
}