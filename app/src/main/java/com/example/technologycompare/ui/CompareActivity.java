package com.example.technologycompare.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.technologycompare.MainActivity;
import com.example.technologycompare.R;
//import com.example.technologycompare.fragment.HomeFragment;
import com.example.technologycompare.helper.DataManager;
import com.example.technologycompare.helper.Device;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CompareActivity extends AppCompatActivity {
    List<Device>devices;
    DataManager dataManager;
    private ImageView imgSelection1,imgSelection2;
    private ImageButton btnBack;
    private LinearLayout layoutInforCompare;
    private Button btnBuyDevice1,btnBuyDevice2,btnResultCompare;
    private Device device2,device1;
    private int resultDevice,requestcode,deviceIndex1,deviceIndex2;
    private TextView tvNameDevice1,tvNameDevice2;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null){
                        resultDevice = data.getIntExtra("resultdata",-1);
                        requestcode = data.getIntExtra("requestcode",-1);
                        if (requestcode == 1){
                            deviceIndex1 = resultDevice;
                            device1 = devices.get(deviceIndex1);
                            getNameImageDevice();
                        } else if (requestcode == 2) {
                            deviceIndex2 = resultDevice;
                            device2= devices.get(deviceIndex2);
                            getNameImageDevice();
                        }
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);


        initUi();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBuyDevice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = device1.getLinkDevice();
                getWebBuyDevice(url);
            }
        });
        btnBuyDevice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = device2.getLinkDevice();
                getWebBuyDevice(url);
            }
        });
        btnResultCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCompareDevice();
            }
        });

        imgSelection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompareActivity.this, MainActivity.class);
                intent.putExtra("hasAccess", true);
                intent.putExtra("Device",1);
                intent.putExtra("idDevice",deviceIndex2);
                launcher.launch(intent);
            }
        });
        imgSelection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompareActivity.this, MainActivity.class);
                intent.putExtra("hasAccess", true);
                intent.putExtra("Device",2);
                intent.putExtra("idDevice",deviceIndex1);
                launcher.launch(intent);
            }
        });
        deviceIndex1 = getIntent().getIntExtra("deviceIndex1", -1);
        deviceIndex2 = getIntent().getIntExtra("deviceIndex2", -1);
        getNameImageDevice();
        startCompareDevice();
    }
    private void getNameImageDevice(){
        if (deviceIndex1 != -1 && deviceIndex2 != -1) {
            device1 = devices.get(deviceIndex1);
            device2= devices.get(deviceIndex2);
            tvNameDevice1.setText(device1.getNameDevice());
            tvNameDevice2.setText(device2.getNameDevice());
            // Sử dụng Glide để hiển thị hình ảnh từ imageUrl1 và imageUrl2
            Glide.with(this)
                    .load(device1.getImageDevice())
                    .into(imgSelection1);

            Glide.with(this)
                    .load(device2.getImageDevice())
                    .into(imgSelection2);
        } else {
            Toast.makeText(this, "Lỗi khi lấy chỉ số thiết bị.", Toast.LENGTH_SHORT).show();
        }
    }
    private void startCompareDevice(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        Resources res =getResources();
        String[] titles = res.getStringArray(R.array.title);
        String[] device1Values = {
                device1.getNameDevice(),
                device1.getSystemDevice(),
                formatPrice(device1.getPriceDevice()),
                device1.getProcessorDevice(),
                device1.getMemoryDevice(),
                device1.getRamDevice(),
                device1.getBatteryDevice(),
                device1.getScreenDevice(),
                device1.getCameraDevice(),
                device1.getSimDevice()
        };

        String[] device2Values = {
                device2.getNameDevice(),
                device2.getSystemDevice(),
                formatPrice(device2.getPriceDevice()),
                device2.getProcessorDevice(),
                device2.getMemoryDevice(),
                device2.getRamDevice(),
                device2.getBatteryDevice(),
                device2.getScreenDevice(),
                device2.getCameraDevice(),
                device2.getSimDevice()
        };


        String[] contract1 = {
                 device1.getNameDevice(),
                 device1.getSystemDevice(),
                device1.getPriceDevice(),
                device1.getKpiProcessor(),
                 device1.getMemoryDevice(),
                 device1.getRamDevice(),
                 device1.getBatteryDevice(),
               device1.getKpiScreen(),
                 device1.getKpiCamera(),
                device1.getSimDevice()
        };
        String[] contract2 = {
                 device2.getNameDevice(),
                 device2.getSystemDevice(),
                device2.getPriceDevice(),
                device2.getKpiProcessor(),
                 device2.getMemoryDevice(),
                 device2.getRamDevice(),
                 device2.getBatteryDevice(),
               device2.getKpiScreen(),
                 device2.getKpiCamera(),
                device2.getSimDevice()
        };

        layoutInforCompare.removeAllViews();
        for (int i = 0; i < titles.length; i++) {
            View itemInfo = inflater.inflate(R.layout.layout_item_compare, null);
            TextView title = itemInfo.findViewById(R.id.titleCompare);
            TextView infoDevice1 = itemInfo.findViewById(R.id.inforDevice1);
            TextView infoDevice2 = itemInfo.findViewById(R.id.inforDevice2);
            TextView performance = itemInfo.findViewById(R.id.performance);
            ImageView imageView = itemInfo.findViewById(R.id.imgPerformance);


            String name = titles[i];
            title.setText(name);
            infoDevice1.setText(device1Values[i]);
            infoDevice2.setText(device2Values[i]);

            // Truyền TextView performance vào hàm isNumeric
            performance.setText(isNumeric(contract1[i], contract2[i], performance,imageView));


            layoutInforCompare.addView(itemInfo);
        }

    }
    private void initUi(){
        imgSelection1 = findViewById(R.id.imgSelection1);
        imgSelection2 = findViewById(R.id.imgSelection2);
        layoutInforCompare = findViewById(R.id.layoutInforCompare);
        btnBack = findViewById(R.id.btnBack);
        btnBuyDevice1 = findViewById(R.id.btnBuyDevice1);
        btnBuyDevice2 = findViewById(R.id.btnBuyDevice2);
        btnResultCompare = findViewById(R.id.btnResultCompare);
        tvNameDevice1 = findViewById(R.id.tvNameDevice1);
        tvNameDevice2 = findViewById(R.id.tvNameDevice2);
        dataManager = DataManager.getInstance();
        devices = dataManager.getDevices();
    }
    public static String isNumeric(String str1, String str2, TextView textView,ImageView imageView) {
        if (str1 == null || str2 == null) {
            imageView.setVisibility(View.GONE);
            return "Đang cập nhật";
        }

        try {
            double a = Double.parseDouble(str1);
            double b = Double.parseDouble(str2);
            if (a >= b) {
                imageView.setImageResource(R.drawable.ic_increase);
                textView.setTextColor(textView.getContext().getResources().getColor(android.R.color.holo_green_light));
            } else {
                imageView.setImageResource(R.drawable.ic_decrease);
                textView.setTextColor(textView.getContext().getResources().getColor(android.R.color.holo_red_light));

            }
            int c = (int) (100 - (b * 100 / a));
            return c + "%";
        } catch (NumberFormatException e) {
            imageView.setVisibility(View.GONE);
            return "Đang cập nhật";
        }
    }


    private String formatPrice(String price) {
        try {
            double priceValue = Double.parseDouble(price);
            DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
            return decimalFormat.format(priceValue);
        } catch (NumberFormatException e) {
            return "Updating ...";
        }
    }
    private void getWebBuyDevice(String url){
//        Toast.makeText(CompareActivity.this, arrayLink.get(i), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CompareActivity.this, BuyDeviceActivity.class);
        intent.putExtra("link",url);
        startActivity(intent);
    }

}