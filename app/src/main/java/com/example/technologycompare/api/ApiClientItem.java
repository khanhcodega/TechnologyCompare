package com.example.technologycompare.api;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClientItem {
    private static final String TAG = ApiClientItem.class.getSimpleName();
    private static final String BASE_URL = "https://ktappmobile.000webhostapp.com/api/api_show_item_phone.php"; // Thay thế "your_api_url" bằng URL của API

    public interface ApiResultListener {
        void onSuccess(JSONArray response);
        void onFailure(String message);
    }

    public static void getItemById(final String id,final String userId, final ApiResultListener listener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String response = "";
                try {
                    URL url = new URL(BASE_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    String postData = "id=" + id + "&user_id=" + userId; // Thêm user_id vào dữ liệu gửi đi
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes());
                    os.flush();
                    os.close();

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    connection.disconnect();
                } catch (IOException e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                    response = "";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                if (!response.isEmpty()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        listener.onSuccess(jsonArray);
                    } catch (JSONException e) {
                        listener.onFailure("Error parsing data.");
                    }
                } else {
                    listener.onFailure("No data received from the server.");
                }
            }
        }.execute();
    }
}

