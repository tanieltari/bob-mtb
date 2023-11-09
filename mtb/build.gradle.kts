plugins {
    id("com.android.library")
    id("com.diffplug.spotless") version "6.22.0"
}

spotless {
    java {
        target("src/*/java/**/*.java")
        importOrder()
        removeUnusedImports()
        palantirJavaFormat()
    }
}

android {
    namespace = "com.ridango.bob.mtb"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.15.3")
    implementation("com.google.guava:guava:32.1.3-android")
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //implementation("androidx.appcompat:appcompat:1.6.1")
    //implementation("com.google.android.material:material:1.10.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("org.assertj:assertj-core:3.24.2")
}