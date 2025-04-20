import java.util.Properties
import java.io.File

// Load your OpenAI key from local.properties
val localProperties = Properties().apply {
    val localFile = File(rootDir, "local.properties")
    if (localFile.exists()) {
        load(localFile.inputStream())
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace   = "com.example.studybuddy"
    compileSdk  = 35

    defaultConfig {
        applicationId = "com.example.studybuddy"
        minSdk        = 24
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"

        // Expose OPENAI_API_KEY to BuildConfig
        buildConfigField("String", "OPENAI_API_KEY", "\"${localProperties["openai.api.key"]}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    // Core Android & Material
    implementation("androidx.core:core-ktx:${libs.versions.coreKtx.get()}")
    implementation("androidx.appcompat:appcompat:${libs.versions.appcompat.get()}")
    implementation("com.google.android.material:material:${libs.versions.material.get()}")

    // Retrofit + Gson (KTor backend)
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // OpenAI Chat client
    implementation("com.theokanning.openai-gpt3-java:client:0.10.0")

    // ZXing for QR codes
    implementation("com.google.zxing:core:3.5.1")

    // Navigation & image loading (retorfitKTorConnection branch)
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("uk.co.samuelwall:material-tap-target-prompt:3.3.2")
    implementation("com.lorentzos.swipecards:library:1.0.9")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
