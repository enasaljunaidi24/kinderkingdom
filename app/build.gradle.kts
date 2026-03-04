plugins {
id("com.android.application")
id("org.jetbrains.kotlin.android")
id("com.google.gms.google-services")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

}

android {
    namespace = "com.example.kinderkingdom"
    compileSdk = 36
    buildFeatures {
        dataBinding =true
    }
    defaultConfig {
        applicationId = "com.example.kinderkingdom"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions{
        jvmTarget="17"
    }
    buildFeatures {
        viewBinding = true
    }


}

dependencies {
  
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))

// Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-firestore:24.10.0")

// Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

// Firebase Analytics (اختياري)
    implementation("com.google.firebase:firebase-analytics")




    // مكتبات أندرويد الأساسية
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
// مكتبة تحديد الموقع (Location)
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.measurement.api)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)



}