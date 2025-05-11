plugins {
    alias(libs.plugins.movieapp.jvm.library)
    alias(libs.plugins.movieapp.hilt)
}

dependencies {
    implementation(libs.paging.common)
}
