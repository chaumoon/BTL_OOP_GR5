package com.coachweb.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.coachweb.service.OrderStatusService;
import com.coachweb.util.HMACUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class OrderStatusServiceImpl implements OrderStatusService{

    @Value("${appid}")
    private String appid;

    @Value("${key1}")
    private String key1;

    @Value("${key2}")
    private String key2;

    @Value("${endpoint}")
    private String endpoint;

    public JSONObject getOrderStatus(String apptransid) {
        JSONObject responseJson = new JSONObject();
        try {

            String data = appid + "|" + apptransid + "|" + key1; // appid|apptransid|key1
            String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, key1, data);

            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            String params = "appid=" + appid + "&apptransid=" + apptransid + "&mac=" + mac;
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(params.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                responseJson.put("error", "HTTP response code: " + responseCode);
                return responseJson;
            }

            // Read response from ZaloPay Server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
            return new JSONObject(result.toString());

        } catch (Exception e) {
            responseJson.put("error", "Exception occurred");
            responseJson.put("message", e.getMessage());
            e.printStackTrace();
        }

        return responseJson;
    }
}

