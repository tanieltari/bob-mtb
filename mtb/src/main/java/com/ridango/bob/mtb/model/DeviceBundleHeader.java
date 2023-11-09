package com.ridango.bob.mtb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceBundleHeader {
    @JsonProperty("alg")
    private String algorithm;
    @JsonProperty("did")
    private String deviceId;
    @JsonProperty("kid")
    private String keyId;
    @JsonProperty("app")
    private String app;
    @JsonProperty("t")
    private String timestamp;
    @JsonProperty("prx")
    private String proximity;
    @JsonProperty("loc")
    private String location;
    @JsonProperty("ua")
    private Integer userAgentId;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProximity() {
        return proximity;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getUserAgentId() {
        return userAgentId;
    }

    public void setUserAgentId(Integer userAgentId) {
        this.userAgentId = userAgentId;
    }
}
