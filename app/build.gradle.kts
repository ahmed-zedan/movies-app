plugins {
    alias(libs.plugins.movieapp.android.application)
    alias(libs.plugins.movieapp.android.application.compose)
    alias(libs.plugins.movieapp.hilt)
    alias(libs.plugins.movieapp.android.retrofit)
    alias(libs.plugins.movieapp.android.room)
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.etax.movieapp"

    defaultConfig {
        applicationId = "com.etax.movieapp"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(project(":features:nowplaying:data"))
    implementation(project(":features:nowplaying:domain"))
    implementation(project(":features:nowplaying:persentation"))

    implementation(project(":features:details:data"))
    implementation(project(":features:details:domain"))
    implementation(project(":features:details:presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.workmanager)
    implementation(libs.startup.runtime)
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
