plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.android.library.compose)
    alias(libs.plugins.movieapp.hilt)
}

android {
    namespace = "com.etax.movies.nowplaying.persentation"
}

dependencies {
    implementation(project(":features:nowplaying:domain"))
    implementation(libs.bundles.paging)
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}