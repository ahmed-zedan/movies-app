plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.android.retrofit)
    alias(libs.plugins.movieapp.android.room)
    alias(libs.plugins.movieapp.hilt)
}

android {
    namespace = "com.etax.movies.nowplaying.data"
}

dependencies {
    implementation(project(":features:nowplaying:domain"))
    implementation(libs.paging.runtime)
    implementation(libs.bundles.workmanager)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}