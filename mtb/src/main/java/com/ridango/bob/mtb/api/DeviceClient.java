package com.ridango.bob.mtb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.base.Strings;
import com.ridango.bob.mtb.api.dto.DeviceKeyRequest;
import com.ridango.bob.mtb.api.dto.DeviceKeyResponse;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceClient {
    private static final String MEDIA_TYPE_JSON = "application/json";

    private final ObjectMapper jsonMapper = new JsonMapper();
    private final OkHttpClient client = new OkHttpClient();

    public DeviceKeyResponse getDeviceKey(String url, String deviceId, String authToken) throws IOException {
        DeviceKeyRequest requestDto = new DeviceKeyRequest();
        requestDto.setDeviceId(deviceId);
        String requestJson = jsonMapper.writeValueAsString(requestDto);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-BoB-AuthToken", authToken)
                .addHeader("Content-Type", MEDIA_TYPE_JSON)
                .addHeader("Accept", MEDIA_TYPE_JSON)
                .post(RequestBody.create(requestJson, MediaType.parse(MEDIA_TYPE_JSON)))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            if (response.body() == null) {
                throw new IOException("Response body was null");
            }
            String responseJson = response.body().string();
            if (Strings.isNullOrEmpty(responseJson)) {
                throw new IOException("Response body was null");
            }
            return jsonMapper.readValue(responseJson, DeviceKeyResponse.class);
        }
    }
}
