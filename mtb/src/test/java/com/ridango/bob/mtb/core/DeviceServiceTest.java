package com.ridango.bob.mtb.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import android.provider.Settings;
import java.time.Instant;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class DeviceServiceTest {
    private final DeviceService sut = new DeviceService();

    @Test
    public void getDeviceId() {
        // Arrange
        final String deviceIdHex = "cc549bc985d6ba70";
        final String expected = "gwVdvnLNNxg";
        try (MockedStatic<Settings.Secure> secureMock = Mockito.mockStatic(Settings.Secure.class)) {
            secureMock.when(() -> Settings.Secure.getString(any(), any())).thenReturn(deviceIdHex);

            // Act
            String result = sut.getDeviceId(null);

            // Assert
            assertThat(result).isEqualTo(expected);
        }
    }

    @Test
    public void getDeviceDateTimeCorrectFormat() {
        // Arrange
        final Instant mockedNow = Instant.ofEpochMilli(1699448862179L);
        final String expected = "20231108T130742.17Z";
        try (MockedStatic<Instant> instantMock = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            instantMock.when(Instant::now).thenReturn(mockedNow);

            // Act
            String result = sut.getDeviceDateTime();

            // Assert
            assertThat(result).isEqualTo(expected);
        }
    }
}
