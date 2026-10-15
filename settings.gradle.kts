pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    // Auto-provisions the JDK used to run unit tests (Robolectric needs a newer Java than the build).
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "hello-world"

// Modules — keep sorted; android-engineer adds a line per module it creates.
include(":core:common")
include(":core:data")
include(":core:designsystem")
include(":core:i18n")
include(":core:model")
include(":core:testing")
include(":core:ui")
include(":feature:hello")
include(":app")
