package com.example.technologycompare.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.technologycompare.MainActivity;
import com.example.technologycompare.R;
import com.example.technologycompare.Sp.SpActivity;
import com.example.technologycompare.api.ApiClient;
import com.example.technologycompare.api.ApiService;
import com.example.technologycompare.api.Const;
import com.example.technologycompare.api.RealPathUtil;
import com.example.technologycompare.helper.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    public static final String TAG = UserActivity.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    private TextView  tvUser, tvTag, btnLogout,tvHistoty,tvMylikes;
    private EditText etName , etBirthday, etEmail;
    private Spinner etGender;
    private ImageView btnEdit;
    private Button btnSave, btnLogin;
    private SharedPreferences sharedPreferences;
    private LinearLayout layoutUser, layoutUserEdit, tvHome, tvSupport;
    private String iduser = "",nameuser = "",imguser = "", genderuser = "",birthdayuser = "",emailuser = "", selectedGender;
    private CircleImageView imgAvatarUser;
    ApiService apiService;
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ArrayAdapter<CharSequence> adapter;
    private ProgressDialog progressDialog;
    private ActivityResultLauncher<Intent> mActivityResultLauncher;
    private Bitmap imageBitmap;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initUi();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        adapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etGender.setAdapter(adapter);
        checklogin();
        InsertUser();

        // Initialize the ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");

        // Initialize the ActivityResultLauncher
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.e(TAG, "onActivityResult");
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                return;
                            }
                            Uri uri = data.getData();
                            try {
                                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imgAvatarUser.setImageBitmap(imageBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                                // Handle the exception
                            }
                        }
                    }
                }
        );

        tvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, SpActivity.class));
                finish();
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, MainActivity.class));
                finish();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("id");
                editor.apply();

                // Hide the views after logout
                layoutUserEdit.setVisibility(View.GONE);
                btnEdit.setVisibility(View.GONE);
                tvTag.setVisibility(View.GONE);
                tvUser.setVisibility(View.GONE);
                layoutUser.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                imgAvatarUser.setImageResource(R.drawable.default_avatar);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertUser();
                layoutUserEdit.setVisibility(View.VISIBLE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutUserEdit.setVisibility(View.GONE);
                UpdateUser();
//                InsertUser();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
            }
        });

        tvHistoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, HistoryCompareActivity.class));
            }
        });

        tvMylikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, MyLikesActivity.class));
            }
        });

        imgAvatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iduser = sharedPreferences.getString("id", "");
                if (imageBitmap != null) {
                    uploadToServer(iduser, imageBitmap);
                }
            }
        });

        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay();
            }
        });
    }

    private void initUi() {
        tvMylikes = findViewById(R.id.tvMylikes);
        tvHistoty = findViewById(R.id.tvHistoty);
        etName = findViewById(R.id.etname);
        etBirthday = findViewById(R.id.etbirthday);
        etGender = findViewById(R.id.etgender);
        etEmail = findViewById(R.id.etemail);
        tvHome = findViewById(R.id.tvHome);
        tvSupport = findViewById(R.id.tvSupport);
        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEditInforUser);
        tvUser = findViewById(R.id.tvNameUser);
        tvTag = findViewById(R.id.tvTag);
        btnSave = findViewById(R.id.btnSaveInforUser);
        btnLogin = findViewById(R.id.btnLogin);
        layoutUser = findViewById(R.id.layoutUser);
        layoutUserEdit = findViewById(R.id.layoutEditInforUser);
        imgAvatarUser = findViewById(R.id.imgAvatarUser);

        apiService = ApiClient.getClient().create(ApiService.class);
    }


    private void checklogin() {
        iduser = sharedPreferences.getString("id", "");

        if (!iduser.isEmpty()) {

            // Sử dụng tên tài khoản savedUsername ở đây
            btnEdit.setVisibility(View.VISIBLE); // Show edit button
            layoutUserEdit.setVisibility(View.GONE); // Show edit layout
            tvTag.setVisibility(View.VISIBLE); // Show tag
            tvUser.setVisibility(View.VISIBLE); // Show user
            layoutUser.setVisibility(View.VISIBLE); // Show user layout
            btnLogin.setVisibility(View.GONE);

        } else {
            // Xử lý trường hợp khi chưa có tên tài khoản (người dùng chưa đăng nhập)
            btnEdit.setVisibility(View.GONE);
            layoutUserEdit.setVisibility(View.GONE);
            tvTag.setVisibility(View.GONE);
            tvUser.setVisibility(View.GONE);
            layoutUser.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
        InsertUser();
    }

    private void UpdateUser() {

        iduser = sharedPreferences.getString("id", "");
        String name = String.valueOf(etName.getText());

        setupSpinner();
        String gender = selectedGender;
        String birthday = String.valueOf(etBirthday.getText());
        String email = String.valueOf(etEmail.getText());
        Call<JsonObject> call3 = apiService.update_u(iduser, name, gender, birthday, email);
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
                        JSONObject userJson = jsonObject.getJSONObject("user");
                        nameuser = userJson.getString("name");
                        imguser = userJson.getString("img_user");
                        genderuser = userJson.getString("gender");
                        birthdayuser = userJson.getString("birthday");
                        emailuser = userJson.getString("email");
                        int position = adapter.getPosition(genderuser);

                        tvUser.setText(nameuser);
                        etName.setText(nameuser);
                        if (position >= 0) {
                            // Đặt giá trị mặc định của Spinner dựa trên vị trí tìm thấy
                            etGender.setSelection(position);
                        } else {
                            // Nếu không tìm thấy giới tính hiện tại trong danh sách, có thể đặt giá trị mặc định khác ở đây
                            etGender.setSelection(adapter.getPosition("Chưa chọn"));
                        }
                        etBirthday.setText(birthdayuser);
                        etEmail.setText(emailuser);
                        Glide.with(UserActivity.this)
                                .load(imguser) // Đường dẫn hình ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // Không lưu vào bộ nhớ cache
                                .skipMemoryCache(true) // Không lưu vào bộ nhớ trong
                                .placeholder(R.drawable.default_avatar) // Hình ảnh thay thế tạm thời
//                .error(R.drawable.error_image) // Hình ảnh hiển thị khi có lỗi
                                .into(imgAvatarUser);


//                        Log.d("UserActivity", "Success : " + iduser+ nameuser+genderuser+birthdayuser+ emailuser);
                        Toast.makeText(UserActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                    } else {
//                        Log.d("LoginActivity", "Error : " + message);
                        Toast.makeText(UserActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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

    private void InsertUser() {

        iduser = sharedPreferences.getString("id", "");

        Call<JsonObject> call3 = apiService.get_u(iduser);
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
                        JSONObject userJson = jsonObject.getJSONObject("user");
                        nameuser = userJson.getString("name");
                        imguser = userJson.getString("img_user");
                        genderuser = userJson.getString("gender");
                        birthdayuser = userJson.getString("birthday");
                        emailuser = userJson.getString("email");
                        int position = adapter.getPosition(genderuser);
                        tvUser.setText(nameuser);
                        etName.setText(nameuser);
                        if (position >= 0) {
                            // Đặt giá trị mặc định của Spinner dựa trên vị trí tìm thấy
                            etGender.setSelection(position);
                        } else {
                            // Nếu không tìm thấy giới tính hiện tại trong danh sách, có thể đặt giá trị mặc định khác ở đây
                            etGender.setSelection(adapter.getPosition("Chưa chọn"));
                        }
                        etBirthday.setText(birthdayuser);
                        etEmail.setText(emailuser);
                        Glide.with(UserActivity.this)
                                .load(imguser) // Đường dẫn hình ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // Không lưu vào bộ nhớ cache
                                .skipMemoryCache(true) // Không lưu vào bộ nhớ trong
                                .placeholder(R.drawable.default_avatar) // Hình ảnh thay thế tạm thời
//                .error(R.drawable.error_image) // Hình ảnh hiển thị khi có lỗi
                                .into(imgAvatarUser);

//
//                        Log.e("UserActivity", "Success : " + "message" +imguser);
//                        Toast.makeText(UserActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();

                    } else {
//                        Log.e("LoginActivity", "Error : " + message);
//                        Toast.makeText(UserActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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

    private void openGallery() {
//        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(gallery, PICK_IMAGE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imgAvatarUser.setImageURI(data.getData());
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imgAvatarUser.setImageBitmap(imageBitmap);
        }
    }

 private void uploadToServer(final String userId, Bitmap bitmap) {
        String url = "http://ktappmobile.000webhostapp.com/api_upload_img.php";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_user", userId)
                .addFormDataPart("image", userId + ".jpg", RequestBody.create(MediaType.parse("image/jpeg"), imageInByte))
                .build();

        okhttp3.Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserActivity.this, "Error uploading file", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(UserActivity.this, responseData, Toast.LENGTH_SHORT).show();
                        Log.d("Response Data", responseData);
                    }
                });
            }
        });
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_MEDIA_IMAGES};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied to access external storage", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void ChonNgay(){
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                etBirthday.setText(simpleDateFormat.format(calendar.getTime()));

            }
        },year,month,date);
        datePickerDialog.show();
    }

    private void setupSpinner() {
        etGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedGender = etGender.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Đặt giá trị mặc định là "Chưa chọn" khi không có giới tính nào được chọn
                etGender.setSelection(adapter.getPosition("Chưa chọn"));
                selectedGender ="Chưa chọn";
            }
        });
    }
}