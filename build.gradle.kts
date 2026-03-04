// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false

    // Android Gradle Plugin
    id("com.android.application") version "8.9.1" apply false
    id("com.android.library") version "8.9.1" apply false

    // Kotlin
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false

    // Firebase Google services plugin
    id("com.google.gms.google-services") version "4.4.4" apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false


}




