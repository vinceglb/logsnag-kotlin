import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.logsnag.sample.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        androidResources.enable = true
    }

    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SampleSharedKit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.components.resources)

            // LogSnag
            implementation(projects.logsnagKotlin)
        }

        androidMain.dependencies {
            // Ktor Engine - OkHttp
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            // Ktor Engine - OkHttp
            implementation(libs.ktor.client.okhttp)
        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
