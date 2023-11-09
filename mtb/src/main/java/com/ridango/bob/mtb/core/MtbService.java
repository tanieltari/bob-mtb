package com.ridango.bob.mtb.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.ridango.bob.mtb.model.Container;
import com.ridango.bob.mtb.model.DeviceBundleHeader;
import java.io.IOException;

public class MtbService {
    private static final String DEFAULT_ALGORITHM = "HS256";
    private static final String MTS_MAJOR_VERSION = "1";
    private final ObjectMapper cborMapper;
    private final CompressorService compressorService;

    public MtbService(CompressorService compressorService) {
        this.cborMapper = new CBORMapper();
        this.compressorService = compressorService;
    }

    public String createDeviceSignedContainer(
            String issuerMtb, DeviceBundleHeader bundleHeader, String key, boolean compressed) throws IOException {
        if (!BaseEncoding.base64Url().canDecode(issuerMtb)) {
            throw new IllegalArgumentException("Issuer MTB is not valid Base64URL string");
        }
        if (!this.isDeviceBundleHeaderValid(bundleHeader)) {
            throw new IllegalArgumentException("Device bundle header is invalid");
        }
        byte[] header = cborMapper.writeValueAsBytes(bundleHeader);
        byte[] payload = this.unwrapIssuerSignedContainerPayload(issuerMtb);
        byte[] signature = this.generateHs256Signature(header, payload, key);
        byte[] bundle = cborMapper.writeValueAsBytes(new byte[][] {header, payload, signature});
        byte[] container = this.wrapDeviceSignedBundle(bundle);
        if (!compressed) {
            return BaseEncoding.base64Url().omitPadding().encode(container);
        }
        byte[] compressedContainer = compressorService.compressBytesZlib(container);
        return BaseEncoding.base64Url().omitPadding().encode(compressedContainer);
    }

    private boolean isDeviceBundleHeaderValid(DeviceBundleHeader header) {
        return !Strings.isNullOrEmpty(header.getAlgorithm())
                && !Strings.isNullOrEmpty(header.getDeviceId())
                && !Strings.isNullOrEmpty(header.getKeyId())
                && header.getAlgorithm().equals(DEFAULT_ALGORITHM);
    }

    private byte[] unwrapIssuerSignedContainerPayload(String encodedContainer) throws IOException {
        byte[] containerBytes = BaseEncoding.base64Url().decode(encodedContainer);
        Container container = cborMapper.readValue(containerBytes, Container.class);
        return container.getPayload();
    }

    private byte[] generateHs256Signature(byte[] header, byte[] payload, String key) throws IOException {
        byte[][] signedData = {header, payload};
        return Hashing.hmacSha256(BaseEncoding.base64Url().decode(key))
                .hashBytes(cborMapper.writeValueAsBytes(signedData))
                .asBytes();
    }

    private byte[] wrapDeviceSignedBundle(byte[] deviceBundle) throws IOException {
        Container container = new Container();
        container.setVersion(cborMapper.writeValueAsBytes(MTS_MAJOR_VERSION));
        container.setPayload(deviceBundle);
        return cborMapper.writeValueAsBytes(container);
    }
}
