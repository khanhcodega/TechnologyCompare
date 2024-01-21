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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.technologycompare.MainActivity;
import com.example.technologycompare.R;
import com.example.technologycompare.api.ApiClient;
import com.example.technologycompare.api.ApiService;
import com.example.technologycompare.helper.DataManager;
import com.example.technologycompare.helper.Device;
import com.example.technologycompare.helper.History;
import com.example.technologycompare.helper.HistoryAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class HistoryCompareActivity extends AppCompatActivity {

    private List<History> histories;
    private HistoryAdapter adapter;
    private ListView listView;
    private ApiService apiService;
    private String iduser;
    private SharedPreferences sharedPreferences;
    private TextView noItemsTextView;


    DataManager dataManager = DataManager.getInstance();
    List<Device> devices = dataManager.getDevices();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_compare);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        initUi();
        adapter = new HistoryAdapter(this, R.layout.layout_item_history, histories);
        listView.setAdapter(adapter);
        History();
        checkHistory();

// Đăng ký lắng nghe sự kiện onDeleteClick
        adapter.setOnDeleteClickListener(new HistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(History history) {
                // Ở đây bạn có thể sử dụng thông tin từ mục lịch sử
                String device1Id = history.getDevice1();
                String device2Id = history.getDevice2();
                String historyId = history.getIdHistory();
                Log.d("HistoryCompareActivity", "item: " + device1Id + "-" + device2Id + "-" + historyId);

                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryCompareActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa !")
                        .setPositiveButton("Đồng ý. ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteHistory(historyId);
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

                // Thực hiện xử lý dựa trên thông tin từ mục lịch sử ở đây
                // Ví dụ: xóa lịch sử dựa trên historyId hoặc hiển thị thông tin trong Toast
            }
        });
        Log.d("HistoryCompareActivity", "Size of histories: " + histories.size());
        // Xử lý khi một mục trong ListView được chọn
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy mục được chọn từ danh sách histories
//                String id1 = null;
//                String id2 = null;
//                id1 = histories.get(position).getDevice1();
//                id2 = histories.get(position).getDevice2();
//                int idDevice1 = devices.get(Integer.parseInt(id1)-1).getIdDevice();
//                int idDevice2 = devices.get(Integer.parseInt(id2)-1).getIdDevice();
//
//
//                // Xử lý khi mục được chọn
////                int idDevice1 = Integer.parseInt(devices.get(Integer.parseInt(selectedHistory.getDevice1())).getIdDevice())-1;
////                int idDevice2 = Integer.parseInt(selectedHistory.getDevice2())-1;
//                Intent intent = new Intent(HistoryCompareActivity.this, CompareActivity.class);
//                intent.putExtra("deviceIndex1", idDevice1);
//                intent.putExtra("deviceIndex2", idDevice2);
//                startActivity(intent);
            }
        });


    }

    private void initUi() {
        listView = findViewById(R.id.listView);
        noItemsTextView = findViewById(R.id.noItemsTextView);
        histories = new ArrayList<>();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void History() {

        iduser = sharedPreferences.getString("id", "");
        Call<JsonObject> call3 = apiService.get_hc(iduser);
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
                            String phone1_id = historyItem.getString("phone1_id");
                            String phone2_id = historyItem.getString("phone2_id");
                            String id = historyItem.getString("id");

                            History history = new History(phone1_id, phone2_id, id);
                            histories.add(history);
                            adapter.notifyDataSetChanged();
                            checkHistory();
                            Log.d("HistoryCompareActivity", "Item " + (i + 1) + ": phone1_id - " + phone1_id + ", phone2_id - " + phone2_id);
                        }

                        Log.d("LoginActivity", "Success : " + message);
//                        Toast.makeText(HistoryCompareActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                        Log.d("HistoryCompareActivity", "Size of histories: " + histories.size());


                    } else {
                        Log.d("LoginActivity", "Error : " + message);
//                        Toast.makeText(HistoryCompareActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("HistoryCompareActivity", "Error fetching history data: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void DeleteHistory(String id) {

        iduser = sharedPreferences.getString("id", "");
        Call<JsonObject> call3 = apiService.delete_hc(iduser, id);
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
                        int positionToRemove = -1;
                        for (int i = 0; i < histories.size(); i++) {
                            if (histories.get(i).getIdHistory().equals(id)) {
                                positionToRemove = i;
                                break;
                            }
                        }

                        if (positionToRemove != -1) {
                            histories.remove(positionToRemove);
                            // Thông báo cho adapter rằng dữ liệu đã thay đổi
                            adapter.notifyDataSetChanged();
                        }
                        Log.d("LoginActivity", "Success : " + message);
                        Toast.makeText(HistoryCompareActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                        Log.d("HistoryCompareActivity", "Size of histories: " + histories.size());


                    } else {
                        Log.d("LoginActivity", "Error : " + message);
                        Toast.makeText(HistoryCompareActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("HistoryCompareActivity", "Error fetching history data: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void checkHistory(){
        if (histories.size()<0){
            noItemsTextView.setVisibility(View.VISIBLE);
        }else {
            noItemsTextView.setVisibility(View.GONE);
        }
    }
}