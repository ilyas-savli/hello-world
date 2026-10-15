plugins {
    id("app.android.application")
    id("app.hilt")
}

/** Reads `api.<flavor>.<key>` from gradle.properties / ~/.gradle / -P (ENV-01, SEC-01). */
fun apiProperty(
    flavor: String,
    key: String,
): String = providers.gradleProperty("api.$flavor.$key").getOrElse("")

android {
    defaultConfig {
        // gradle.properties (rendered from APPLICATION_ID / VERSION_CODE / VERSION_NAME): Migrate keeps the old app's id.
        applicationId = providers.gradleProperty("app.applicationId").get()
        versionCode = providers.gradleProperty("app.versionCode").get().toInt()
        versionName = providers.gradleProperty("app.versionName").get()
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true // Robolectric UI tests read the app's resources
    }

    buildFeatures {
        resValues = true // the stage flavor renames the app with resValue
    }

    // ENV-01: one flavor per environment. Everything that differs per environment lives here, never in if-checks.
    // Single-environment app (intake answer): delete this block and keep only the prod values in defaultConfig.
    flavorDimensions += "environment"
    productFlavors {
        create("stage") {
            dimension = "environment"
            applicationIdSuffix = ".stage"
            versionNameSuffix = "-stage"
            resValue("string", "common_app_name", "Hello World Stage")
            buildConfigField("String", "API_BASE_URL", "\"${apiProperty("stage", "baseUrl")}\"")
            buildConfigField("String", "API_CERT_PINS", "\"${apiProperty("stage", "certPins")}\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "API_BASE_URL", "\"${apiProperty("prod", "baseUrl")}\"")
            buildConfigField("String", "API_CERT_PINS", "\"${apiProperty("prod", "certPins")}\"")
        }
    }

    // HARD-07: signing secrets only from the environment (CI secrets / local shell), never from files in git.
    signingConfigs {
        create("release") {
            val keystorePath = providers.environmentVariable("KEYSTORE_PATH").orNull
            if (keystorePath != null) {
                storeFile = file(keystorePath)
                storePassword = providers.environmentVariable("KEYSTORE_PASSWORD").orNull
                keyAlias = providers.environmentVariable("KEY_ALIAS").orNull
                keyPassword = providers.environmentVariable("KEY_PASSWORD").orNull
            }
        }
    }

    buildTypes {
        release {
            // HARD-03: shrink + obfuscate with project keep rules.
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release").takeIf { it.storeFile != null }
        }
    }
}

dependencies {
    // Only what :app itself uses; features bring their own dependencies (DEAD-03).
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.i18n)
    implementation(projects.feature.hello)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.timber)

    // OBS-04: leak detection in debug builds only.
    debugImplementation(libs.leakcanary)

    testImplementation(projects.core.testing)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.test)
}
