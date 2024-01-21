package com.example.technologycompare.api;

import com.example.technologycompare.helper.User;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @FormUrlEncoded
    @POST("/api/login_api.php")
    Call<JsonObject> login_u(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("/api/update_user_api.php")
    Call<JsonObject> update_u(@Field("id") String id,@Field("name") String name,@Field("gender") String gender,@Field("birthday") String birthday,@Field("email") String email);
    @FormUrlEncoded
    @POST("/api/get_user.php")
    Call<JsonObject> get_u(@Field("id") String id);
    @FormUrlEncoded
    @POST("/api/get_history_api.php")
    Call<JsonObject> get_hc(@Field("user_id") String user_id);
    @FormUrlEncoded
    @POST("/api/delete_history_api.php")
    Call<JsonObject> delete_hc(@Field("user_id") String user_id,@Field("id") String id);
    @FormUrlEncoded
    @POST("/api/register_api.php")
    Call<JsonObject> regis_u(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("/api/history_compare_api.php")
    Call<JsonObject> insert_hc(@Field("user_id") String user_id,@Field("phone1_id") String phone1_id,@Field("phone2_id") String phone2_id);
    @FormUrlEncoded
    @POST("/api/get_mylike_api.php")
    Call<JsonObject> get_ml(@Field("user_id") String user_id);
    @FormUrlEncoded
    @POST("/api/delete_mylikes_api.php")
    Call<JsonObject> delete_ml(@Field("user_id") String user_id,@Field("id") String id);
    @FormUrlEncoded
    @POST("/api/check_mylike_api.php")
    Call<JsonObject> check_ml(@Field("user_id") String user_id,@Field("phone_id") String phone_id);

    @POST("khanhtestapi.php")
    @Multipart
    Call<JsonObject> updateUserImage(@Part("id") RequestBody id, @Part MultipartBody.Part img_user);
}
