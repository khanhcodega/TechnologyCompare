package com.example.technologycompare.api;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClientPhone {
    public interface ApiDataListener {
        void onDataReceived(String apiResponse);
    }

    public static void getPhoneData(int id, ApiDataListener listener) {
        new AsyncTask<Integer, Void, String>() {
            @Override
            protected String doInBackground(Integer... integers) {
                return getPhoneInfoFromServer(integers[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                if (listener != null) {
                    listener.onDataReceived(result);
                }
            }
        }.execute(id);
    }

    private static String getPhoneInfoFromServer(int id) {
        try {
            URL url = new URL("https://ktappmobile.000webhostapp.com/api/supportapi.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "id=" + id;

            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
                os.write(postDataBytes, 0, postDataBytes.length);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
