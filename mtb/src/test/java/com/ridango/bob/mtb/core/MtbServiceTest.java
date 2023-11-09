package com.ridango.bob.mtb.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ridango.bob.mtb.model.DeviceBundleHeader;
import java.io.IOException;
import org.junit.Test;

public class MtbServiceTest {
    private final MtbService sut = new MtbService(new CompressorService());

    @Test
    public void createDeviceSignedContainerSignsSuccessfully() throws IOException {
        // Arrange
        final String issuerMtb =
                "v2F2QmExYXBY14NYRL9jYWxnZUVTMjU2Y2tpZHE2MDoyMDIzMDJfYm9iX210YmNpaWRiNjBjbWl2YTRjZXhwcDIwMjMwNTEyVDExNDEwNFr_WEy_YjYwgb9kY192bmE1ZGNfdm1hMWRjX3Z0YTFkY19tZIK_Y3RpZGozMDEzOTQ5NDA4_79lcF9jaWRpVlQ0MzJJNEU4_2RjX3RjYP__WEBWPApX9HNGcNgF3pEKoDdVwYOlbtqWvzpjS4JY4A7qKKDRysSG-mn4faEQeKgUFs3PF2zmJhOMXduTHq1maxs9_w";
        final String expected =
                "eJzbn1jmlGiYWBDJqNocob4_OTEnPdUj2MjULDklMyU7vTwspSzPx8-vIj05OzMlLTu10tDI-H_E9eYIF4haV7BaoFyhmYGVkYGRsYFRfFJ-UnxuSVJyZmZKkplBcm5mWaJJcmpFQQFY3tTQKMTQ0MTQwCTqf4TPfqCKxv0pyfFleYmmICo30RBElYCp3JSm_cklmSlZxgaGxpYmliYGFv_3pxbEJ2emZIaFmBgbeZq4WvwHKixJTvj_P8IhzIYr_EuxW8EN1nsTuRaYhx5sXpp3a9p-q2TvpogHfK80Flw8daTtV-aP2oUCFStExM6eF895pibcE3t7stzatGxp2wiF0O3Ly531Qx-92j4_ZOoKkeUHfa_xf004JJjSftFQo4Tx_38Askh7yg";
        final String key = "MTIzNA";
        final DeviceBundleHeader bundleHeader = new DeviceBundleHeader();
        bundleHeader.setAlgorithm("HS256");
        bundleHeader.setDeviceId("gwVdvnLNNxg");
        bundleHeader.setKeyId("key123");

        // Act
        String result = sut.createDeviceSignedContainer(issuerMtb, bundleHeader, key, true);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void createDeviceSignedContainerSignsSuccessfullyUncompressed() throws IOException {
        // Arrange
        final String issuerMtb =
                "v2F2QmExYXBY14NYRL9jYWxnZUVTMjU2Y2tpZHE2MDoyMDIzMDJfYm9iX210YmNpaWRiNjBjbWl2YTRjZXhwcDIwMjMwNTEyVDExNDEwNFr_WEy_YjYwgb9kY192bmE1ZGNfdm1hMWRjX3Z0YTFkY19tZIK_Y3RpZGozMDEzOTQ5NDA4_79lcF9jaWRpVlQ0MzJJNEU4_2RjX3RjYP__WEBWPApX9HNGcNgF3pEKoDdVwYOlbtqWvzpjS4JY4A7qKKDRysSG-mn4faEQeKgUFs3PF2zmJhOMXduTHq1maxs9_w";
        final String expected =
                "v2F2QmExYXBZASWDWCe_Y2FsZ2VIUzI1NmNkaWRrZ3dWZHZuTE5OeGdja2lkZmtleTEyM_9Y14NYRL9jYWxnZUVTMjU2Y2tpZHE2MDoyMDIzMDJfYm9iX210YmNpaWRiNjBjbWl2YTRjZXhwcDIwMjMwNTEyVDExNDEwNFr_WEy_YjYwgb9kY192bmE1ZGNfdm1hMWRjX3Z0YTFkY19tZIK_Y3RpZGozMDEzOTQ5NDA4_79lcF9jaWRpVlQ0MzJJNEU4_2RjX3RjYP__WEBWPApX9HNGcNgF3pEKoDdVwYOlbtqWvzpjS4JY4A7qKKDRysSG-mn4faEQeKgUFs3PF2zmJhOMXduTHq1maxs9WCBVt6d3Qy9V4uq3n1SVqBSnwU3WD_VgwhFkh9ExKHQB__8";
        final String key = "MTIzNA";
        final DeviceBundleHeader bundleHeader = new DeviceBundleHeader();
        bundleHeader.setAlgorithm("HS256");
        bundleHeader.setDeviceId("gwVdvnLNNxg");
        bundleHeader.setKeyId("key123");

        // Act
        String result = sut.createDeviceSignedContainer(issuerMtb, bundleHeader, key, false);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void createDeviceSignedContainerFailsForNonBase64Url() {
        // Arrange
        final String issuerMtb = "v2F2QmExYXBY14NYRL9+/";
        final String key = "MTIzNA";
        final DeviceBundleHeader bundleHeader = new DeviceBundleHeader();
        bundleHeader.setAlgorithm("HS256");
        bundleHeader.setDeviceId("gwVdvnLNNxg");
        bundleHeader.setKeyId("key123");

        // Act & assert
        assertThatThrownBy(() -> sut.createDeviceSignedContainer(issuerMtb, bundleHeader, key, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Issuer MTB is not valid Base64URL string");
    }

    @Test
    public void createDeviceSignedContainerFailsForInvalidHeader() {
        // Arrange
        final String issuerMtb =
                "v2F2QmExYXBY14NYRL9jYWxnZUVTMjU2Y2tpZHE2MDoyMDIzMDJfYm9iX210YmNpaWRiNjBjbWl2YTRjZXhwcDIwMjMwNTEyVDExNDEwNFr_WEy_YjYwgb9kY192bmE1ZGNfdm1hMWRjX3Z0YTFkY19tZIK_Y3RpZGozMDEzOTQ5NDA4_79lcF9jaWRpVlQ0MzJJNEU4_2RjX3RjYP__WEBWPApX9HNGcNgF3pEKoDdVwYOlbtqWvzpjS4JY4A7qKKDRysSG-mn4faEQeKgUFs3PF2zmJhOMXduTHq1maxs9_w";
        final String key = "MTIzNA";
        final DeviceBundleHeader bundleHeader = new DeviceBundleHeader();
        bundleHeader.setAlgorithm("HS256_128");
        bundleHeader.setDeviceId("gwVdvnLNNxg");
        bundleHeader.setKeyId("key123");

        // Act & assert
        assertThatThrownBy(() -> sut.createDeviceSignedContainer(issuerMtb, bundleHeader, key, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Device bundle header is invalid");
    }
}
