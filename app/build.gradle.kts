plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.gms.google-services")
}

android {
    namespace = "cafe.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "cafe.app"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.10")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("androidx.databinding:databinding-runtime:8.2.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")
    // Firebase SDK
    implementation("com.google.firebase:firebase-auth:22.3.1")
    // Google Sign-In SDK
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Glide for Images
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    // JUnit
    testImplementation("junit:junit:4.13.2")
    // Mockito
    testImplementation("org.mockito:mockito-core:3.11.2")
    // AndroidX Test - Core Library
    androidTestImplementation("androidx.test:core:1.4.0")
    // AndroidX Test - JUnit
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    // Espresso Core for UI Tests
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    // Mockito Android
    androidTestImplementation("org.mockito:mockito-android:3.11.2")
}
