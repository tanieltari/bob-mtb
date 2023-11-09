package com.ridango.bob.mtb.transport;

import static org.assertj.core.api.Assertions.assertThat;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ridango.bob.mtb.core.CompressorService;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class AztecBarcodeTest {
    private final AztecBarcode sut = new AztecBarcode(new CompressorService());

    @Test
    public void generateMtbBarcodeBitmap() throws IOException {
        // Arrange
        final String mtb = "eJzbn1jmlGiYWBDJqNocob4_OTEnPdUj2MjULDklMyU7vTwspSzPx8-vIj05OzMlLTu10tDI-H_E9eYIF4haV7BaoFyhmYGVkYGRsYFRfFJ-UnxuSVJyZmZKkplBcm5mWaJJcmpFQQFY3tTQKMTQ0MTQwCTqf4TPfqCKxv0pyfFleYmmICo30RBElYCp3JSm_cklmSlZxgaGxpYmliYGFv_3pxbEJ2emZIaFmBgbeZq4WvwHKixJTvj_P8IhzIYr_EuxW8EN1nsTuRaYhx5sXpp3a9p-q2TvpogHfK80Flw8daTtV-aP2oUCFStExM6eF895pibcE3t7stzatGxp2wiF0O3Ly531Qx-92j4_ZOoKkeUHfa_xf004JJjSftFQo4Tx_38Askh7yg";
        final int ecc = 90;
        final int expectedSize = 75;

        // Act
        Bitmap result = sut.generateMtbBarcodeBitmap(mtb, ecc);

        // Assert
        assertThat(result.getWidth()).isEqualTo(expectedSize);
        assertThat(result.getHeight()).isEqualTo(expectedSize);
    }
}