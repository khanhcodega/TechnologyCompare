package com.example.technologycompare.api;


import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class ApiClientLike {
    public interface ApiDataListener {
        void onDataReceived(String apiResponse);
    }

    public static void insertLikeToMyLikes(String iduser, int productId, String userlike, ApiDataListener listener) {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... objects) {
                return sendLikeDataToServer(objects[0].toString(), (int) objects[1], objects[2].toString());
            }

            @Override
            protected void onPostExecute(String result) {
                if (listener != null) {
                    listener.onDataReceived(result);
                }
            }
        }.execute(iduser, productId, userlike);
    }

    private static String sendLikeDataToServer(String iduser, int productId, String userlike) {
        try {
            URL url = new URL("https://ktappmobile.000webhostapp.com/api/api_like_phone.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "id=" + productId + "&user_id=" + iduser + "&user_like_status=" + userlike;

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
