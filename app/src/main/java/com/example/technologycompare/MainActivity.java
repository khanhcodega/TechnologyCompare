package com.example.technologycompare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.example.technologycompare.Sp.SpActivity;
import com.example.technologycompare.api.ApiClient;
import com.example.technologycompare.api.ApiService;
import com.example.technologycompare.helper.DataManager;
import com.example.technologycompare.helper.Device;
import com.example.technologycompare.helper.DeviceAdapter;
import com.example.technologycompare.ui.CompareActivity;
import com.example.technologycompare.ui.TestActivity;
import com.example.technologycompare.ui.UserActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements DeviceAdapter.OnCompareCountChangeListener {
    private GridView gridView;
    private DeviceAdapter adapter;
    private List<Device> devices;
    private Device selectedDevice1, selectedDevice2;
    private LinearLayout layoutCompare;
    private ImageView imgDevice1, imgDevice2, imgClose1, imgClose2, btnSearch;
    private TextView tvDevice1, tvDevice2;
    private LinearLayout tvHome, tvSupport, tvUser;
    private Button btnCompare, btnCancel;
    private EditText etSearch;

    private SharedPreferences sharedPreferences;
    private String iduser, idphone1, idphone2;
    private int compareButtonClickCount, selectID ;
    ApiService apiService;
    private List<Integer> filteredToOriginalIndices = new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private boolean isFiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);
        initUi();


        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        imgClose1.setVisibility(View.GONE);
        imgClose2.setVisibility(View.GONE);
        layoutCompare.setVisibility(View.GONE);


        adapter = new DeviceAdapter(this, R.layout.layout_item_device, devices, layoutCompare, selectedDevice1, selectedDevice2);
        adapter.setOnCompareCountChangeListener(this);
        gridView.setAdapter(adapter);
        ReadContentJson("https://ktappmobile.000webhostapp.com/myphp/fulldatajson.php");


        Intent intent1 = getIntent();
        iduser = intent1.getStringExtra("ID_USER");
//        Toast.makeText(MainActivity.this, "id_user: " + iduser, Toast.LENGTH_SHORT).show();
        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.d("bbb", selectedDevice1.getImageDevice() + selectedDevice2.getImageDevice() + "");

                if (selectedDevice1 != null && selectedDevice2 != null) {
                    // Chuyển chỉ số của các thiết bị được chọn thay vì đối tượng Device
                    int deviceIndex1 = devices.indexOf(selectedDevice1);
                    int deviceIndex2 = devices.indexOf(selectedDevice2);

                    if (deviceIndex1 != -1 && deviceIndex2 != -1) {
                        Intent intent = new Intent(MainActivity.this, CompareActivity.class);
                        intent.putExtra("deviceIndex1", deviceIndex1);
                        intent.putExtra("deviceIndex2", deviceIndex2);
                        InsertHistory();
                        startActivity(intent);
                        ResetCompare();

                    } else {
                        Toast.makeText(MainActivity.this, "Lỗi khi lấy chỉ số thiết bị.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn hai thiết bị để so sánh.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetCompare();
            }
        });

        imgClose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDevice1 = null;
                imgDevice1.setImageResource(R.drawable.add_devices);
                compareButtonClickCount = 0;
                imgClose1.setVisibility(View.GONE);
                tvDevice1.setText("Device1");
                adapter.setCompareButtonClickCount(compareButtonClickCount);
            }
        });

        imgClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDevice2 = null;
                imgDevice2.setImageResource(R.drawable.add_devices);
                if (selectedDevice1 == null) {
                    compareButtonClickCount = 2;
                } else {
                    compareButtonClickCount = 1;
                }
                imgClose2.setVisibility(View.GONE);
                tvDevice2.setText("Device2");
                adapter.setCompareButtonClickCount(compareButtonClickCount);
            }
        });

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (isFiltered) {
                int originalIndex = filteredToOriginalIndices.get(i);
                 selectID = devices.get(originalIndex).getIdDevice();
            } else {
                 selectID = devices.get(i).getIdDevice(); // Handle item click based on the full list
            }
            boolean hasAccess = getIntent().getBooleanExtra("hasAccess", false);
            if (hasAccess) {
                int resultCode = getIntent().getIntExtra("Device", -1);
                if (resultCode == 1 || resultCode == 2) {
                    int requestId = getIntent().getIntExtra("idDevice", -1);
                    if(requestId != (selectID -1)){
                        ResultIntent(selectID -1, resultCode);
                    }else {
                        Toast.makeText(this, "Thiết bị đã được chọn , vui lòng chọn thiết bị khác !", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {

                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("PRODUCT_ID", selectID);
                intent.putExtra("ID_USER", iduser); // Chỉ sử dụng một đối tượng Intent
                startActivity(intent);
            }
        });

        setupSearchDevices();

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch("");
            }
        });

        tvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SpActivity.class));
            }
        });

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        });
    }

    private void ResetCompare(){
        compareButtonClickCount = 0;
        selectedDevice1 = null;
        selectedDevice2 = null;
        imgDevice1.setImageResource(R.drawable.add_devices);
        imgDevice2.setImageResource(R.drawable.add_devices);
        layoutCompare.setVisibility(View.GONE);
        adapter.setCompareButtonClickCount(compareButtonClickCount);
    }
    private void InsertHistory() {

        iduser = sharedPreferences.getString("id", "");
        idphone1 = String.valueOf(selectedDevice1.getIdDevice());
        idphone2 = String.valueOf(selectedDevice2.getIdDevice());

        Call<JsonObject> call3 = apiService.insert_hc(iduser, idphone1, idphone2);
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

                        Log.d("MainActivity", "Success aa: " + iduser + idphone1 + idphone2);
//                        Toast.makeText(MainActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("MainActivity", "Error : " + message);
//                        Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
    private void ReadContentJson(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        devices.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                int id = (Integer.valueOf(object.getString("id")));
                                String name = (object.getString("name"));
                                String price = (object.getString("price"));
                                String link = (object.getString("link"));
                                String image = (object.getString("img"));
                                String screen = (object.getString("namescreen"));
                                String system = (object.getString("operating_system"));
                                String processor = (object.getString("nameprocessor"));
                                String memory = (object.getString("internal_memory"));
                                String ram = (object.getString("ram"));
                                String sim = (object.getString("sim_slots"));
                                String battery = (object.getString("battery_capacity"));
                                String camera = (object.getString("namecamera"));
                                String kpicamera = (object.getString("kpi_camera"));
                                String kpiprocessor = (object.getString("kpi_processor"));
                                String kpiscreen = (object.getString("kpi_screen"));
                                Device device = new Device(id, image, name, link, screen, system, processor, memory, ram, sim, battery, camera, price, kpiscreen, kpiprocessor, kpicamera);
                                devices.add(device);
//                                Log.d("Devices", device.getIdDevice() + "-" + device.getImageDevice() + "-" + device.getNameDevice() + "-" + device.getPriceDevice() + "-" + device.getLinkDevice() + "-" +
//                                        device.getScreenDevice() + "-" + device.getSystemDevice() + "-" + device.getBatteryDevice() + "-" + device.getRamDevice());

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        DataManager dataManager = DataManager.getInstance();
                        dataManager.setDevices(devices);
                        // Update the DeviceAdapter's dataset
                        adapter.setDevices(devices);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "loi", Toast.LENGTH_SHORT).show();
                        Log.e("errorVolley", error.toString());
                    }
                });
        requestQueue.add(jsonArray);
    }
    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
//        tabLayout = findViewById(R.id.tablayout);
        gridView = findViewById(R.id.gridView);
        layoutCompare = findViewById(R.id.layoutCompare);
        imgDevice1 = findViewById(R.id.imgCompareDevice1);
        imgDevice2 = findViewById(R.id.imgCompareDevice2);
        imgClose1 = findViewById(R.id.imgClose1);
        imgClose2 = findViewById(R.id.imgClose2);
        tvDevice1 = findViewById(R.id.tvCompareDevice1);
        tvDevice2 = findViewById(R.id.tvCompareDevice2);
        btnCompare = findViewById(R.id.btnCompare);
        btnCancel = findViewById(R.id.btnCancel);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tvHome = findViewById(R.id.tvHome);
        tvSupport = findViewById(R.id.tvSupport);
        tvUser = findViewById(R.id.tvUser);

        apiService = ApiClient.getClient().create(ApiService.class);
        devices = new ArrayList<>();
    }
    public void onCompareCountChanged(int cout) {
        // Đã thay đổi compareButtonClickCount ở Adapter, cập nhật giao diện ở đây
        updateUI();
    }
    private void updateUI() {
        compareButtonClickCount = adapter.getCompareButtonClickCount();
        selectedDevice1 = adapter.getSelectedDevice1();
        selectedDevice2 = adapter.getSelectedDevice2();
//        Log.d("HomeFragment", "compareButtonClickCount: " + compareButtonClickCount);
        if (compareButtonClickCount == 1 && selectedDevice1 != null) {
            Glide.with(this)
                    .load(selectedDevice1.getImageDevice())
                    .into(imgDevice1);
            tvDevice1.setText(selectedDevice1.getNameDevice());
            imgClose1.setVisibility(View.VISIBLE);
        } else if (compareButtonClickCount == 2 && selectedDevice2 != null) {
            // Thực hiện các thay đổi giao diện ở đây, ví dụ:
            Glide.with(this)
                    .load(selectedDevice2.getImageDevice())
                    .into(imgDevice2);
            tvDevice2.setText(selectedDevice2.getNameDevice());
            imgClose2.setVisibility(View.VISIBLE);

        }
    }
    private void performSearch(String keyword) {
        List<Device> filteredDevices = new ArrayList<>();
        filteredToOriginalIndices.clear(); // Clear the initUi
        isFiltered = !keyword.isEmpty(); // Set isFiltered based on whether a keyword is provided

        if (isFiltered) {
            for (int i = 0; i < devices.size(); i++) {
                Device device = devices.get(i);
                if (device.getNameDevice().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredDevices.add(device);
                    filteredToOriginalIndices.add(i); // Store the initUi
                }
            }
        } else {
            filteredDevices.addAll(devices);
        }

        adapter.setDevices(filteredDevices);
        adapter.notifyDataSetChanged();
    }
    private void setupSearchDevices() {
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN)) {
                    // Xử lý sự kiện khi người dùng nhấn "Done" hoặc "Enter" trên bàn phím
                    performSearch(String.valueOf(etSearch.getText()));

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }
    private void ResultIntent(int position, int requestCode) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("resultdata", position);
        resultIntent.putExtra("requestcode", requestCode);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
