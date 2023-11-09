package com.ridango.bob.mtb.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceKeyRequest {
    @JsonProperty("did")
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
