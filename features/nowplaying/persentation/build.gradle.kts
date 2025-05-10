plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.android.library.compose)
    alias(libs.plugins.movieapp.hilt)
}

android {
    namespace = "com.etax.movies.nowplaying.persentation"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}