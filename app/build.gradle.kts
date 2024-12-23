plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "1.9.0-1.0.12"
    id("kotlin-parcelize")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.capstone.kulinerkita"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.capstone.kulinerkita"
        minSdk = 24
        targetSdk = 35
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
    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.jetbrains.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.places)
    implementation(libs.places)
    implementation(libs.play.services.fitness)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.androidx.cardview)
    implementation (libs.material.v1100)
    implementation (libs.github.glide)

    // Import the Firebase BoM
    implementation(libs.firebase.bom.v3370)
    implementation(libs.com.google.firebase.firebase.auth)
    implementation(libs.play.services.auth)
    implementation (libs.firebase.auth)

    // room database
    implementation (libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Reforfit
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)

    //Maps
    implementation (libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation (libs.android.maps.utils)
    implementation (libs.play.services.tasks)

    //Animasi
    implementation (libs.github.glide)
    annotationProcessor (libs.compiler)
}