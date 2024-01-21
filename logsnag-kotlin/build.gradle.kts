import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.mavenPublishVanniktech)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release", "debug")
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "logsnag"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.json)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Kermit Logs
            implementation(libs.kermit)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.androidx.appStartup)
            implementation(libs.androidx.workmanager)
        }

        jvmTest.dependencies {
            // Ktor Engine - OkHttp
            implementation(libs.ktor.client.okhttp)
        }

        nativeTest.dependencies {
            // Ktor Engine - Darwin
            implementation(libs.ktor.client.darwin)
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

android {
    namespace = "com.logsnag.kotlin"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

buildkonfig {
    packageName = "com.logsnag"
    exposeObjectWithName = "LogSnagSecrets"

    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "LogSnagToken",
            value = gradleLocalProperties(project.rootDir).getProperty("LOGSNAG_TOKEN"),
            nullable = true
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "LogSnagProject",
            value = gradleLocalProperties(project.rootDir).getProperty("LOGSNAG_PROJECT"),
            nullable = true,
        )
    }
}
