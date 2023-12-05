## What is BoB standard?
[Introduction to BoB standard by Samtrafiken](https://samtrafiken.atlassian.net/wiki/spaces/BOB/pages/2908651521/Introduction+to+the+BoB+Standard)
## What is this library for?
Standard defines that mobile ticket bundles (MTB-s) are signed both by issuer and the device itself.
This helper library helps to construct device signed ticket bundles for Android devices more easily.
Also this library can be used as a research basis for BoB standard in general as there are not many actual implementations 
that developers can use for examples.
## Using library
This library is not (yet) published to any remote repositories, so first step is to actually checkout the code.
```shell
git clone https://github.com/tanieltari/bob-mtb.git
```
After checkout one must use platform specific Gradle wrapper script to run task called `publishToMavenLocal`
```shell
# Windows users
.\gradlew.bat publishToMavenLocal
# *nix users (Linux, MacOS etc)
./gradlew publishToMavenLocal
```
This will build the AAR file and publish it to the local Maven repository (~/.m2 directory)
## API
### Device ID (used in bundle header & to retrieve device key)
*Content resolver can be retrieved only from inside Activity*
```java
DeviceService deviceService = new DeviceService();
String deviceId = deviceService.getDeviceId(getContentResolver());
```
### Device current timestamp (used in bundle header)
```java
DeviceService deviceService = new DeviceService();
String deviceDateTime = deviceService.getDeviceDateTime();
```
### Device key (for signing bundle)
*Retrieving the authentication token from BoB Authentication API is developer responsibility*
```java
DeviceClient deviceClient = new DeviceClient();
DeviceKeyResponse deviceKeyResponse = deviceClient.getDeviceKey("https://device.bob.example.com/api/v1/device/key", deviceId, authToken);
```
### Device bundle header
*Must define at least key, algorithm and device ID as defined in the standard*
```java
DeviceBundleHeader deviceBundleHeader = new DeviceBundleHeader();
deviceBundleHeader.setKeyId(deviceKeyResponse.getKeyId());
deviceBundleHeader.setAlgorithm("HS256");
deviceBundleHeader.setDeviceId(deviceId);
deviceBundleHeader.setTimestamp(deviceDateTime);
/* ... */
```
### Compressor service
```java
CompressorService compressorService = new CompressorService();
try {
    byte[] compressedBytes = compressorService.compressBytesZlib(new byte[] {1, 2, 3});
} catch (IOException e) {
    // Handle exception
}
try {
    boolean isZlibBytes = compressorService.isZlibCompressed(new byte[] { 1, 2, 3 });
} catch (IOException e) {
    // Handle exception
}
```
### MTB service
*Last boolean in the method definition tells if MTB should be compressed or not*
```java
MtbService mtbService = new MtbService(compressorService);
try {
    String issuerSignedMtb = mtbService.createDeviceSignedContainer(issuerMtb, deviceBundleHeader, deviceKeyResponse.getKey(), true);
} catch (IOException e) {
    // Handle exception
}
```
### Aztec barcode service
*Last integer in the method definition tells what ECC (error code correction) percentage should be used*
```java
AztecBarcode aztecBarcode = new AztecBarcode(compressorService);
try {
    Bitmap mtbAztec = aztecBarcode.generateMtbBarcodeBitmap(issuerSignedMtb, 23);
} catch (IOException e) {
    // Handle exception
}
```