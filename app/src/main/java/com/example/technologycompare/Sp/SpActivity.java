package com.example.technologycompare.Sp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technologycompare.MainActivity;
import com.example.technologycompare.R;
import com.example.technologycompare.api.ApiClientPhone;
import com.example.technologycompare.api.ApiClientSp;
import com.example.technologycompare.ui.UserActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpActivity extends AppCompatActivity {
    private GridView gridView;
    private Button btnCompare;
    private EditText etPrice;
    private CheckBox cbCamera, cbRam, cbProcessor, cbBattery, cbMemory, cbScreen;
    private TextView tvResult;
    private List<String> checkboxList = new ArrayList<>();
    private LinearLayout checkboxLayout,tvUser,tvHome;
    private TextView tvDevice1, tvDevice2,tvSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        tvHome = findViewById(R.id.tvHome);
        tvUser = findViewById(R.id.tvUser);
        btnCompare = findViewById(R.id.btnCompare);
        etPrice = findViewById(R.id.etPrice);
        cbCamera = findViewById(R.id.cbCamera);
        cbRam = findViewById(R.id.cbRam);
        cbProcessor = findViewById(R.id.cbProcessor);
        cbBattery = findViewById(R.id.cbBattery);
        cbMemory = findViewById(R.id.cbMemory);
        cbScreen = findViewById(R.id.cbScreen);
        gridView = findViewById(R.id.gridView);
        checkboxLayout = findViewById(R.id.checkboxLayout);

        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comparePhones();
            }
        });

        setupCheckboxListeners();

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SpActivity.this, MainActivity.class));
                finish();
            }
        });
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SpActivity.this, UserActivity.class));
                finish();
            }
        });
    }
    //hàm tick vào checkbox
    private void setupCheckboxListeners() {
        cbCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                handleCheckboxChange(isChecked, "Camera");
            }
        });

        cbRam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                handleCheckboxChange(isChecked, "Ram");
            }
        });

        cbProcessor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                handleCheckboxChange(isChecked, "Processor");
            }
        });

        cbBattery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                handleCheckboxChange(isChecked, "Battery");
            }
        });

        cbMemory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                handleCheckboxChange(isChecked, "Memory");
            }
        });

        cbScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                handleCheckboxChange(isChecked, "Screen");
            }
        });
    }
    // hàm thêm thêm hoặc xóa các item trong checkbox
    private void handleCheckboxChange(boolean isChecked, String checkboxName) {
        if (isChecked) {
            if (!checkboxList.contains(checkboxName)) {
                checkboxList.add(checkboxName);
            }
        } else {
            checkboxList.remove(checkboxName);
        }
        // show item
        displayCheckboxList();
    }
    // show danh sach item đã check
    private void displayCheckboxList() {
        checkboxLayout.removeAllViews();
        for (String item : checkboxList) {
            TextView textView = new TextView(this);
            textView.setText(item);
            checkboxLayout.addView(textView);
        }
    }
    // lấy giá trị price và cách thông tin check box
    private void comparePhones() {
        String input = etPrice.getText().toString();
        int price;
        if (input.isEmpty()) {
            price = 100000000;
        } else {
            price = Integer.parseInt(input);
        }
        new ComparePhonesTask().execute(price, checkboxList);
    }

    private class ComparePhonesTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            try {
                int price = (int) params[0];
                List<String> checkboxList = (List<String>) params[1];
                return ApiClientSp.comparePhones(price, checkboxList);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Kiểm tra dữ liệu JSON trước khi xử lý
                Log.d("JSON Data", result);

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<Device> deviceList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String imageUrl = jsonObject.getString("img");
                        String name = jsonObject.getString("name");
                        String price = jsonObject.getString("price");
                        String ram = jsonObject.getString("Ram");
                        String storage = jsonObject.getString("Memory");
                        String battery = jsonObject.getString("Battery");
                        String chip = jsonObject.getString("nameprocessor");
                        String camera = jsonObject.getString("namecamera");
                        String sim = jsonObject.getString("sim_slots");
                        // Và sau đó sử dụng chúng để tạo đối tượng Device mới
                        String link = jsonObject.getString("link");
                        String id = jsonObject.getString("id");
                        Device device = new Device(imageUrl, name, price, ram, storage, battery, chip, camera, sim, link, id);
                        deviceList.add(device);
                    }

                    // Truyền danh sách device vào Adapter
                    DeviceAdapter adapter = new DeviceAdapter(SpActivity.this, R.layout.activity_support, deviceList);
                    gridView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                gridView.setText("Lỗi kết nối hoặc lỗi dữ liệu.");
            }
        }

    }

}
