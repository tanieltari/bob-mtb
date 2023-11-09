package com.ridango.bob.mtb.api;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.ridango.bob.mtb.api.dto.DeviceKeyRequest;
import com.ridango.bob.mtb.api.dto.DeviceKeyResponse;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeviceClientTest {
    private static final MockWebServer MOCK_WEB_SERVER = new MockWebServer();
    public static final String MEDIA_TYPE_JSON = "application/json";
    private final ObjectMapper jsonMapper = new JsonMapper();
    private final DeviceClient sut = new DeviceClient();

    @BeforeClass
    public static void setup() throws IOException {
        MOCK_WEB_SERVER.start();
    }

    @AfterClass
    public static void cleanup() throws IOException {
        MOCK_WEB_SERVER.shutdown();
    }

    @Test
    public void getDeviceKeySuccessful() throws IOException, InterruptedException {
        // Arrange
        final String path = "/api/v1/device/key";
        final String url = String.format("http://localhost:%d%s", MOCK_WEB_SERVER.getPort(), path);
        final String deviceId = "gwVdvnLNNxg";
        final String authToken = "123456789";
        final String keyId = "1337:20231109";
        final String key = "m9kaRQMwpp50MKtOUUR2Q";
        final String keyType = "oct";
        final int expiration = 1490002591;
        final int issuedAt = 1489998991;

        DeviceKeyRequest mockRequest = new DeviceKeyRequest();
        mockRequest.setDeviceId(deviceId);
        String mockRequestJson = jsonMapper.writeValueAsString(mockRequest);

        DeviceKeyResponse mockResponse = new DeviceKeyResponse();
        mockResponse.setDeviceId(deviceId);
        mockResponse.setKeyId(keyId);
        mockResponse.setKeyType(keyType);
        mockResponse.setExpiration(expiration);
        mockResponse.setIssuedAt(issuedAt);
        mockResponse.setKey(key);
        String mockResponseJson = jsonMapper.writeValueAsString(mockResponse);
        MOCK_WEB_SERVER.enqueue(new MockResponse().setBody(mockResponseJson).setResponseCode(201));

        // Act
        DeviceKeyResponse keyResponse = sut.getDeviceKey(url, deviceId, authToken);

        // Assert
        RecordedRequest recordedRequest = MOCK_WEB_SERVER.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getHeader("Content-Type")).isNotNull().contains(MEDIA_TYPE_JSON);
        assertThat(recordedRequest.getHeader("X-BoB-AuthToken")).isNotNull().isEqualTo(authToken);
        assertThat(recordedRequest.getPath()).isNotNull().isEqualTo(path);
        assertThat(recordedRequest.getBody().readUtf8()).isEqualTo(mockRequestJson);

        assertThat(keyResponse.getDeviceId()).isEqualTo(deviceId);
        assertThat(keyResponse.getKeyId()).isEqualTo(keyId);
        assertThat(keyResponse.getKey()).isEqualTo(key);
        assertThat(keyResponse.getKeyType()).isEqualTo(keyType);
        assertThat(keyResponse.getExpiration()).isEqualTo(expiration);
        assertThat(keyResponse.getIssuedAt()).isEqualTo(issuedAt);
    }

    @Test
    public void getDeviceKeyServerError() {
        // Arrange
        final String path = "/api/v1/device/key";
        final String url = String.format("http://localhost:%d%s", MOCK_WEB_SERVER.getPort(), path);
        final String deviceId = "gwVdvnLNNxg";
        final String authToken = "123456789";

        MOCK_WEB_SERVER.enqueue(new MockResponse().setResponseCode(500));

        // Act & assert
        assertThatThrownBy(() -> sut.getDeviceKey(url, deviceId, authToken))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Unexpected code");
    }

    @Test
    public void getDeviceKeyEmptyBody() {
        // Arrange
        final String path = "/api/v1/device/key";
        final String url = String.format("http://localhost:%d%s", MOCK_WEB_SERVER.getPort(), path);
        final String deviceId = "gwVdvnLNNxg";
        final String authToken = "123456789";

        MOCK_WEB_SERVER.enqueue(new MockResponse().setResponseCode(201));

        // Act & assert
        assertThatThrownBy(() -> sut.getDeviceKey(url, deviceId, authToken))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Response body was null");
    }
}
