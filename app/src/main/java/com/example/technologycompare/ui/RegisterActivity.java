package com.example.technologycompare.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.example.technologycompare.R;
import com.example.technologycompare.api.ApiClient;
import com.example.technologycompare.api.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class RegisterActivity extends AppCompatActivity {

    ApiService apiService;
    private TextInputEditText etUsername, etPassword;
    private Button btnRegis, btnToLoginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                regisAccout("https://ktappmobile.000webhostapp.com/api/register_api.php");
                RegisUser();
            }
        });

        btnToLoginForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    private void initUi() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegis = findViewById(R.id.btnRegister);
        btnToLoginForm = findViewById(R.id.btnLogin);
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void RegisUser() {

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Call<JsonObject> call3 = apiService.regis_u(username,password);
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                Log.d("res", response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String result = jsonObject.getString("result");
                    String message = jsonObject.getString("message");
                    if (result.equals("Success")) {
                        // Lưu username vào SharedPreferences

                        Log.d("UserActivity", "Success : " + message );
                        Toast.makeText(RegisterActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();

                    } else {
                        Log.d("LoginActivity", "Error : " + message);
                        Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}