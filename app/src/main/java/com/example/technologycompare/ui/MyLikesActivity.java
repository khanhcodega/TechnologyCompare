package com.example.technologycompare.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.technologycompare.R;
import com.example.technologycompare.api.ApiClient;
import com.example.technologycompare.api.ApiService;


import com.example.technologycompare.helper.DataManager;
import com.example.technologycompare.helper.Device;
import com.example.technologycompare.helper.History;
import com.example.technologycompare.helper.HistoryAdapter;
import com.example.technologycompare.helper.MyLike;
import com.example.technologycompare.helper.MyLikeAdpter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyLikesActivity extends AppCompatActivity {

    private List<MyLike> myLikes;
    private MyLikeAdpter adapter;
    private GridView gvMylikes;
    private ApiService apiService;
    private String iduser;
    private SharedPreferences sharedPreferences;
    private TextView noItemsTextView;

    DataManager dataManager = DataManager.getInstance();
    List<Device> devices = dataManager.getDevices();
    private boolean isLongClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_likes);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        initUi();
        adapter = new MyLikeAdpter(this, R.layout.layout_item_my_likes, myLikes);
        gvMylikes.setAdapter(adapter);
        getMyLikes();
        checkHistory();
        // Xử lý khi một mục trong ListView được chọn
        gvMylikes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                if (!isLongClick) {
                    Intent intent = new Intent(MyLikesActivity.this, TestActivity.class);
                    intent.putExtra("PRODUCT_ID", devices.get(myLikes.get(i).getIdPhone()).getIdDevice()-1);
                    startActivity(intent);
                    Log.d("MyLikesActivity", "Item " + (i) + ": idphone - " + myLikes.get(i).getIdPhone() + ", i : - " + devices.get(myLikes.get(i).getIdPhone()).getIdDevice());                }
                isLongClick = false;
            }
        });

        gvMylikes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                isLongClick = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(MyLikesActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa !")
                        .setPositiveButton("Đồng ý. ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteMylikes(String.valueOf(myLikes.get(i).getIdMylike()));
                                myLikes.remove(i);
                                adapter.notifyDataSetChanged();
                                checkHistory();
                            }
                        })
                        .setNegativeButton("Hủy.", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show(); // Hiển thị hộp thoại lên màn hình
                return true;
            }
        });


    }

    private void initUi() {
        gvMylikes = findViewById(R.id.gvMyLike);
        noItemsTextView = findViewById(R.id.noItemsTextView);
        myLikes = new ArrayList<>();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void getMyLikes() {

        iduser = sharedPreferences.getString("id", "");
        Call<JsonObject> call3 = apiService.get_ml(iduser);
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
                        JSONArray historyArray = jsonObject.getJSONArray("history");

                        for (int i = 0; i < historyArray.length(); i++) {
                            JSONObject historyItem = historyArray.getJSONObject(i);
                            int phone = historyItem.getInt("phone_id");
                            int id = historyItem.getInt("id");
                            MyLike myLike = new MyLike(id, phone);
                            myLikes.add(myLike);
                            adapter.notifyDataSetChanged();

                            Log.d("MyLikesActivity", "Item get" + (i + 1) + ": phone - " + phone + ", id - " + id);
                        }

                        Log.d("LoginActivity", "Success : " + message);
//                        Toast.makeText(MyLikesActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                        Log.d("MyLikesActivity", "Size of histories: " + myLikes.size());


                    } else {
                        Log.d("LoginActivity", "Error : " + message);
//                        Toast.makeText(MyLikesActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("MyLikesActivity", "Error fetching history data: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void DeleteMylikes(String id) {

        iduser = sharedPreferences.getString("id", "");
        Call<JsonObject> call3 = apiService.delete_ml(iduser, id);
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

                        Log.d("MyLikesActivity", "Success : " + message);
                        Toast.makeText(MyLikesActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                        Log.d("MyLikesActivity", "Size of histories: " + myLikes.size());


                    } else {
                        Log.d("LoginActivity", "Error : " + message);
                        Toast.makeText(MyLikesActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("MyLikesActivity", "Error fetching history data: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
    private void checkHistory(){
        if (myLikes.size()<=0){
            noItemsTextView.setVisibility(View.GONE);
        }else {
            noItemsTextView.setVisibility(View.VISIBLE);
        }
    }
}