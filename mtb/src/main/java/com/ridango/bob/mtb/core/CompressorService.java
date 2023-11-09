package com.ridango.bob.mtb.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressorService {
    private static final byte ZLIB_MAGIC_BYTE = 0x78;

    public byte[] compressBytesZlib(byte[] input) throws IOException {
        byte[] buffer = new byte[1024];
        Deflater compressor = new Deflater();
        compressor.setInput(input);
        compressor.finish();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            while (!compressor.finished()) {
                int count = compressor.deflate(buffer);
                baos.write(buffer, 0, count);
            }
            return baos.toByteArray();
        }
    }

    public boolean isZlibCompressed(byte[] input) throws IOException {
        if (input[0] != ZLIB_MAGIC_BYTE) {
            return false;
        }
        byte[] buffer = new byte[1024];
        Inflater decompressor = new Inflater();
        decompressor.setInput(input);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buffer);
                baos.write(buffer, 0, count);
            }
            return true;
        } catch (DataFormatException e) {
            return false;
        }
    }
}
