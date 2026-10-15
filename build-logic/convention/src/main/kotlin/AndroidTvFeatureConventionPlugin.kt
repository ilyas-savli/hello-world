import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * A TV feature module (:tv:feature:<name>): TV screens only, reusing the ViewModel + UiState of its mirror
 * :feature:<name>. Enforces TVM-03: no other feature, no other TV feature and no phone UI modules.
 * No Kover rule here: the logic it shows is covered in the mirror feature.
 */
class AndroidTvFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.android.library")
        pluginManager.apply("app.android.compose")
        pluginManager.apply("app.hilt")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        val mirror = ":feature:$name"
        dependencies {
            add("implementation", project(":core:common"))
            add("implementation", project(":core:designsystem-tv"))
            add("implementation", project(":core:i18n"))
            add("implementation", project(":core:model"))
            findProject(mirror)?.let { add("implementation", it) }
            findProject(":core:data")?.let { add("implementation", it) }
            findProject(":core:domain")?.let { add("implementation", it) }

            add("implementation", libs.lib("androidx-tv-material"))
            add("implementation", libs.lib("androidx-lifecycle-runtime-compose"))
            add("implementation", libs.lib("androidx-lifecycle-viewmodel-compose"))
            add("implementation", libs.lib("androidx-navigation3-runtime"))
            add("implementation", libs.lib("androidx-hilt-lifecycle-viewmodel-compose"))
            add("implementation", libs.lib("kotlinx-collections-immutable"))
            add("implementation", libs.lib("kotlinx-serialization-json"))

            add("testImplementation", project(":core:testing"))
            add("testImplementation", project(":core:testing-tv"))
            add("testImplementation", libs.lib("kotlinx-coroutines-test"))
        }

        afterEvaluate {
            val forbidden = setOf(":core:designsystem", ":core:ui")
            configurations.forEach { configuration ->
                configuration.dependencies.withType<ProjectDependency>().forEach { dependency ->
                    val dep = dependency.path
                    val otherFeature = dep.startsWith(":feature:") && dep != mirror
                    val otherTvFeature = dep.startsWith(":tv:feature:") && dep != path
                    if (otherFeature || otherTvFeature || dep in forbidden) {
                        throw GradleException(
                            "$path depends on $dep (TVM-03): a TV feature uses only its mirror $mirror, core " +
                                "modules and :core:designsystem-tv. Share code through a core module instead.",
                        )
                    }
                }
            }
        }
    }
}
