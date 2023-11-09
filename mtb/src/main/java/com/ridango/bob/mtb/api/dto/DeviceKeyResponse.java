package com.ridango.bob.mtb.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceKeyResponse {
    @JsonProperty("kid")
    private String keyId;

    @JsonProperty("did")
    private String deviceId;

    @JsonProperty("kty")
    private String keyType;

    @JsonProperty("k")
    private String key;

    @JsonProperty("iat")
    private Integer issuedAt;

    @JsonProperty("exp")
    private Integer expiration;

    @JsonProperty("ua")
    private Integer userAgent;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Integer issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public Integer getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Integer userAgent) {
        this.userAgent = userAgent;
    }
}
