package com.ridango.bob.mtb.core;

import static org.assertj.core.api.Assertions.*;

import com.google.common.io.BaseEncoding;
import java.io.IOException;
import org.junit.Test;

public class CompressorServiceTest {
    private static final byte ZLIB_MAGIC_BYTE = 0x78;

    private final CompressorService sut = new CompressorService();

    @Test
    public void compressBytesZlibCorrectData() throws IOException {
        // Arrange
        final String inputHex =
                "48656C6C6F2C20576F726C6421204920616D2076657279206C6F6E6720746578742077697468206E6F20636F6E7465787420666F72204D5442206C6962726172792074657374";
        final String outputHex =
                "789CF348CDC9C9D75108CF2FCA495154F05448CC55284B2DAA54C8C9CF4B572849AD285128CF2CC950C8CB5748CECF03F3D3F28B147C439C147232938A12812A4B528B4B0057C918B9";
        final byte[] input = BaseEncoding.base16().decode(inputHex);
        final byte[] output = BaseEncoding.base16().decode(outputHex);

        // Act
        byte[] result = sut.compressBytesZlib(input);

        // Assert
        assertThat(result)
                .hasSize(output.length)
                .startsWith(ZLIB_MAGIC_BYTE)
                .asHexString()
                .isEqualTo(outputHex);
    }

    @Test
    public void isZlibCompressedCorrectData() throws IOException {
        // Arrange
        final String inputHex =
                "789CF348CDC9C9D75108CF2FCA495154F05448CC55284B2DAA54C8C9CF4B572849AD285128CF2CC950C8CB5748CECF03F3D3F28B147C439C147232938A12812A4B528B4B0057C918B9";
        final byte[] input = BaseEncoding.base16().decode(inputHex);

        // Act
        boolean result = sut.isZlibCompressed(input);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isZlibCompressedCorruptedData() throws IOException {
        // Arrange
        final String inputHex =
                "789CF348CDC9C9D75108CF2FCA495154F05448CC55284B2DAA54C8C9CF4B572849AD285128CF2CC950C8CB5748CECF03F3D3F28B147C439C147232938A12812A4B528B4B0057C918A9";
        final byte[] input = BaseEncoding.base16().decode(inputHex);

        // Act
        boolean result = sut.isZlibCompressed(input);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isZlibCompressedNotCompressedData() throws IOException {
        // Arrange
        final String inputHex = "F726C6421204920616D20766";
        final byte[] input = BaseEncoding.base16().decode(inputHex);

        // Act
        boolean result = sut.isZlibCompressed(input);

        // Assert
        assertThat(result).isFalse();
    }
}
