package com.ridango.bob.mtb.core;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.provider.Settings;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DeviceService {
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SS'Z'").withZone(ZoneId.from(ZoneOffset.UTC));

    @SuppressLint("HardwareIds")
    public String getDeviceId(ContentResolver resolver) {
        byte[] deviceIdBytes = new byte[8];
        String deviceId =
                Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID).toUpperCase();
        Hashing.sha256()
                .hashBytes(BaseEncoding.base16().decode(deviceId))
                .writeBytesTo(deviceIdBytes, 0, deviceIdBytes.length);
        return BaseEncoding.base64Url().omitPadding().encode(deviceIdBytes);
    }

    public String getDeviceDateTime() {
        return dateTimeFormatter.format(Instant.now());
    }
}
