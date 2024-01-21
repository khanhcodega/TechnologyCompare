package com.example.technologycompare.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.technologycompare.api.ApiClientLike;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.technologycompare.R;
import com.example.technologycompare.api.ApiClientItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    private TextView tvName, tvPrice, tvBattery, tvRam, tvStorage, tvChip, tvCamera, tvScreen, luotlike;
    private ImageView imgDevice, backButton;
    private Button btnBuy, btnLike;
    private static final String TAG = TestActivity.class.getSimpleName();
    private String iduser,userlike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_item_phone);

        // Ánh xạ views
        tvName = findViewById(R.id.tvNameDevice);
        tvPrice = findViewById(R.id.tvPriceDevice);
        tvBattery = findViewById(R.id.tvBattery);
        tvRam = findViewById(R.id.tvRam);
        tvStorage = findViewById(R.id.tvStorage);
        tvChip = findViewById(R.id.tvChip);
        tvCamera = findViewById(R.id.tvCamera);
        tvScreen = findViewById(R.id.tvScreen);
        luotlike = findViewById(R.id.luotlike);
        imgDevice = findViewById(R.id.imgDevice);
        ImageView backButton = findViewById(R.id.backButton);
        btnLike = findViewById(R.id.btnLike);
        LinearLayout btnBuy = findViewById(R.id.btnBuy);
        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        int receivedProductId = intent.getIntExtra("PRODUCT_ID", -1);
/*
        iduser1 = intent.getStringExtra("ID_USER");
*/
// Trong một phương thức hoặc vị trí khác trong ứng dụng
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
// Lấy giá trị iduser từ SharedPreferences
        iduser = sharedPreferences.getString("id", "");
        if (TextUtils.isEmpty(iduser)) {
            iduser = "0";
        }
// Ví dụ in giá trị iduser ra Logcat
        Toast.makeText(TestActivity.this,iduser, Toast.LENGTH_SHORT).show();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackFunction();
            }

            private void goBackFunction() {
                onBackPressed();
            }
        });
        // xu lí nút like
//        boolean isLiked = false;
//        if (isLiked) {
//            // Đặt màu nền xanh cho nút like
//            btnLike.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorGreen));
//        } else {
//            // Đặt màu nền đỏ cho nút like
//            btnLike.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorRed));
//        }
        // Gọi phương thức để lấy dữ liệu từ API
        // Lặp qua mỗi đối tượng trong mảng JSON
        // Lấy ra đối tượng JSON từ mảng
        // Lấy giá trị tên từ đối tượng JSON
        // Trong phương thức onSuccess, sau khi lấy link từ jsonObject
        // xữ lý nút like
        // Đặt màu nền xanh cho nút like
        // Đặt màu nền đỏ cho nút like
        // Bạn có thể thêm một nút lắng nghe sự kiện cho nút Buy
        // Xử lý ngoại lệ nếu có lỗi trong quá trình xử lý JSON
        // Xử lý lỗi ở đây
        ApiClientItem.getItemById(String.valueOf(receivedProductId), iduser, new ApiClientItem.ApiResultListener() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    // Lặp qua mỗi đối tượng trong mảng JSON
                    for (int i = 0; i < response.length(); i++) {
                        // Lấy ra đối tượng JSON từ mảng
                        JSONObject jsonObject = response.getJSONObject(i);
                        // Lấy giá trị tên từ đối tượng JSON
                        tvName.setText(Html.fromHtml("<b>Tên: </b>"+jsonObject.getString("name")));

                        tvPrice.setText(Html.fromHtml("<b>Giá: </b>"+jsonObject.getString("price")));
                        tvBattery.setText(Html.fromHtml("<b>Pin: </b>"+jsonObject.getString("Battery")));
                        tvRam.setText(Html.fromHtml("<b>Ram: </b>"+jsonObject.getString("Ram")));
                        tvStorage.setText(Html.fromHtml("<b>Bộ nhớ: </b>"+jsonObject.getString("Memory")));
                        tvChip.setText(Html.fromHtml("<b>CPU: </b>"+jsonObject.getString("nameprocessor")));
                        tvCamera.setText(Html.fromHtml("<b>Camera: </b>"+jsonObject.getString("namecamera")));
                        tvScreen.setText(Html.fromHtml("<b>Màn hình: </b>"+jsonObject.getString("namescreen")));
                        luotlike.setText(Html.fromHtml("<b>Lượt thích: </b>"+jsonObject.getString("total_likes")));
                        Picasso.get().load(jsonObject.getString("img")).into(imgDevice);
                        // Trong phương thức onSuccess, sau khi lấy link từ jsonObject
                        String link = jsonObject.getString("link");
                        // xữ lý nút like
                        // Đặt màu nền xanh cho nút like
                        boolean isLiked = false;
                        userlike = jsonObject.getString("user_like_status");
//                        Toast.makeText(TestActivity.this,jsonObject.getString("user_like_status"), Toast.LENGTH_SHORT).show();
                        if (jsonObject.getString("user_like_status").equals("1")) {   // Đặt màu nền xanh cho nút like
                            isLiked = true;
                            colorLike(isLiked);
                        } else {
                            // Đặt màu nền đỏ cho nút like
                            isLiked = false;
                            colorLike(isLiked);
                        }

                        // Bạn có thể thêm một nút lắng nghe sự kiện cho nút Buy
                        btnBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TestActivity.this, BuyDeviceActivity.class);
                                intent.putExtra("link", link);
                                startActivity(intent);
                            }
                        });
                        // Lắng nghe sự kiện like
                        btnLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(iduser != "0") {
                                    ApiClientLike.insertLikeToMyLikes(iduser, receivedProductId, userlike, new ApiClientLike.ApiDataListener() {
                                        @Override
                                        public void onDataReceived(String apiResponse) {
                                            // Xử lý phản hồi từ server (nếu cần thiết)
                                            Toast.makeText(getApplicationContext(), apiResponse, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    // Gọi lại phương thức để lấy dữ liệu từ API và cập nhật giao diện người dùng
                                    ApiClientItem.getItemById(String.valueOf(receivedProductId), iduser, new ApiClientItem.ApiResultListener() {
                                        @Override
                                        public void onSuccess(JSONArray response) {
                                            try {
                                                JSONObject jsonObject = response.getJSONObject(0);
                                                String updatedLikeStatus = jsonObject.getString("user_like_status");
                                                // Cập nhật giao diện người dùng với dữ liệu mới
                                                Toast.makeText(TestActivity.this, jsonObject.getString("user_like_status"), Toast.LENGTH_SHORT).show();
                                                userlike = jsonObject.getString("user_like_status");
                                                luotlike.setText(Html.fromHtml("<b>Lượt thích: </b>" + jsonObject.getString("total_likes")));
                                                colorLike(updatedLikeStatus.equals("1"));

                                            } catch (JSONException e) {
                                                Log.e(TAG, "Error: " + e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onFailure(String message) {
                                            // Xử lý lỗi ở đây
                                            Log.e(TAG, "Error: " + message);
                                        }
                                    });
                                } else {
                                    // Hiển thị thông báo và chuyển qua trang đăng nhập
                                    Toast.makeText(TestActivity.this, "Please log in to perform this action.", Toast.LENGTH_SHORT).show();
                                    // Chuyển đến trang đăng nhập
                                    Intent loginIntent = new Intent(TestActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                }
                            }
                        });

                    }
                } catch (JSONException e) {
                    // Xử lý ngoại lệ nếu có lỗi trong quá trình xử lý JSON
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                // Xử lý lỗi ở đây
                Log.e(TAG, "Error: " + message);
            }
        });
    }
    private void colorLike(boolean isLiked) {
        CheckBox btnLike = findViewById(R.id.btnLike); // Lấy đối tượng CheckBox từ tài nguyên

        // Đặt trạng thái được chọn hoặc không được chọn dựa trên giá trị của isLiked
        btnLike.setChecked(isLiked);

        // Thay đổi drawable của nút dựa trên trạng thái đã chọn
        if (isLiked) {
            btnLike.setButtonDrawable(R.drawable.ic_like); // Đặt drawable cho trạng thái đã chọn
        } else {
            btnLike.setButtonDrawable(R.drawable.custom_drawable_checkbox); // Đặt drawable cho trạng thái không được chọn
        }
    }

}
//    private void insertLikeToMyLikes(String iduser, int productId, String userlike) {
//        String url = "https://ktappmobile.000webhostapp.com/api/api_like_phone.php"; // Thay thế bằng URL thực tế của API
//
//        // Tạo request truy cập API bằng phương thức POST
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Xử lý phản hồi từ server (nếu cần thiết)
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Xử lý lỗi (nếu cần thiết)
//                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", String.valueOf(productId));
//                params.put("user_id", iduser);
//                params.put("user_like_status", userlike);
//                return params;
//            }
//        };
//
////        // Thêm request vào hàng đợi yêu cầu
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }


