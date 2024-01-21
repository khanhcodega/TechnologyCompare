package com.example.technologycompare.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiClientSp {
    static List<String> checkboxList2 = Arrays.asList("Battery", "Camera", "Memory", "Ram", "Processor", "Screen");

    public static String comparePhones(int price, List<String> checkboxList) {
        try {
            URL url = new URL("https://ktappmobile.000webhostapp.com/api/supportapi.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            List<String> checkboxList3 = new ArrayList<>(checkboxList);
            for (String item : checkboxList2) {
                if (!checkboxList3.contains(item)) {
                    checkboxList3.add(item);
                }
            }
            StringBuilder postData = new StringBuilder();
            postData.append("price=").append(price);
            for (int i = 0; i < checkboxList3.size(); i++) {
                postData.append("&checkboxList[").append(i).append("]=").append(checkboxList3.get(i));
            }

            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
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
