package com.ridango.bob.mtb.transport;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.common.io.BaseEncoding;
import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.common.BitMatrix;
import com.ridango.bob.mtb.core.CompressorService;
import java.io.IOException;

public class AztecBarcode {
    private final CompressorService compressorService;

    public AztecBarcode(CompressorService compressorService) {
        this.compressorService = compressorService;
    }

    public Bitmap generateMtbBarcodeBitmap(String mtb, int ecc) throws IOException {
        if (!BaseEncoding.base64Url().canDecode(mtb)) {
            throw new IllegalArgumentException("MTB is not valid Base64URL string");
        }
        if (!compressorService.isZlibCompressed(BaseEncoding.base64Url().decode(mtb))) {
            throw new IllegalArgumentException("MTB is not Zlib compressed");
        }
        if (ecc < 23 || ecc > 100) {
            throw new IllegalArgumentException("ECC value must be between 23 and 100");
        }
        byte[] data = BaseEncoding.base64Url().decode(mtb);
        BitMatrix bitMatrix = Encoder.encode(data, ecc, 0).getMatrix();
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
