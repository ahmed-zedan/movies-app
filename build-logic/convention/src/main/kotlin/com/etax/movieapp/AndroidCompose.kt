package com.etax.movieapp

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "implementation"(libs.findLibrary("compose-foundation").get())
            "implementation"(libs.findLibrary("compose-foundation-layout").get())
            "implementation"(libs.findLibrary("compose-material3").get())
            "implementation"(libs.findLibrary("compose-navigation").get())
            "implementation"(libs.findLibrary("compose-ui-tooling-preview").get())
            "debugImplementation"(libs.findLibrary("compose-ui-tooling").get())
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }
}
