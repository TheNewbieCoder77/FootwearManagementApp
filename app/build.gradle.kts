plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "uz.excellentshoes.businesscalculation"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.excellentshoes.businesscalculation"
        minSdk = 26
        targetSdk = 34
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database2)
    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

// ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

// LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

// Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

// Optional: for ReactiveStreams support
    implementation(libs.androidx.lifecycle.reactivestreams.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.recyclerview.swipedecorator)

//ViewPager2
    implementation (libs.material.v190)
    implementation(libs.androidx.viewpager2)


}