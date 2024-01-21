package com.example.technologycompare.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.technologycompare.MainActivity;
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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private Button btnToRegisForm, btnLogin;
    private CheckBox cbSaveLogin;
    private TextView tvForgotPassword;
    private SharedPreferences sharedPreferences ;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("id", "");
        if (!savedUsername.isEmpty()) {
            etUsername.setText(savedUsername);
        }

        btnToRegisForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LoginAccout("https://ktappmobile.000webhostapp.com/api/login_api.php");
                LoginUser();
            }
        });
    }

    private void LoginUser(){

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Call<JsonObject> call3 = apiService.login_u(username, password);
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
                        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                        JSONObject userJson = jsonObject.getJSONObject("user");
                        String iduser = userJson.getString("id");

//                         Truyền id_user qua MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("ID_USER", iduser);
                        startActivity(intent);
                        finish();

                        // Lưu id_user vào SharedPreferences nếu cần thiết
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id", iduser);
                        editor.apply();

                        // Hiển thị thông báo Toast để xác nhận rằng id_user đã được lấy thành công
//                        Toast.makeText(LoginActivity.this, "id_user: " + iduser, Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity","Success : " + message);
                        Toast.makeText(LoginActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Log.d("LoginActivity","Error : " + message);
                        Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

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

    private void initUi(){
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnToRegisForm = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        cbSaveLogin = findViewById(R.id.cbSaveLogin);
        tvForgotPassword = findViewById(R.id.tvFotgotPassword);
        apiService = ApiClient.getClient().create(ApiService.class);
    }
}