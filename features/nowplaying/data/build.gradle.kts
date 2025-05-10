plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.android.retrofit)
    alias(libs.plugins.movieapp.hilt)
}

android {
    namespace = "com.etax.movies.nowplaying.data"
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}