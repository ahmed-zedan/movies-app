import com.etax.movieapp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class NetworkRetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")

            dependencies {
                "implementation"(libs.findLibrary("retrofit-core").get())
                "implementation"(libs.findLibrary("retrofit-converter-moshi").get())
                "implementation"(libs.findLibrary("moshi-core").get())
                "ksp"(libs.findLibrary("moshi-kotlin-codegen").get())
                val bom = libs.findLibrary("okhttp-bom").get()
                "implementation"(platform(bom))
                "implementation"(libs.findLibrary("okhttp-core").get())
                "implementation"(libs.findLibrary("okhttp-logging").get())
            }
        }
    }
}
