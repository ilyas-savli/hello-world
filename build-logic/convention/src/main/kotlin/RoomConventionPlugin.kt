import androidx.room.gradle.RoomExtension
import com.android.build.api.dsl.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Room with exported schemas (needed for Migration tests — DATA-04, TEST-08). Migration tests run on Robolectric in
 * src/test so the gate runs them: room-testing is a test dependency and the schemas are test assets.
 */
class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("androidx.room")
        pluginManager.apply("com.google.devtools.ksp")

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }
        extensions.configure<KspExtension> {
            arg("room.generateKotlin", "true")
        }
        extensions.configure<LibraryExtension> {
            // MigrationTestHelper reads <schemas>/<Database class>/<version>.json from the test assets.
            sourceSets.getByName("test").assets.directories.add("$projectDir/schemas")
        }
        dependencies {
            add("implementation", libs.lib("androidx-room-runtime"))
            add("implementation", libs.lib("androidx-room-ktx"))
            add("ksp", libs.lib("androidx-room-compiler"))
            add("testImplementation", libs.lib("androidx-room-testing"))
            add("testImplementation", libs.lib("robolectric"))
        }
    }
}
