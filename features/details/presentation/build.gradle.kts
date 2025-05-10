plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.android.library.compose)
    alias(libs.plugins.movieapp.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.etax.movies.details.presentation"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}