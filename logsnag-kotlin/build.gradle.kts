import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.mavenPublishVanniktech)
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    android {
        namespace = "com.logsnag.kotlin"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        withHostTest {}
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
            implementation(libs.ktor.client.auth)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Kermit Logs
            implementation(libs.kermit)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
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

        val nonAndroidMain = create("nonAndroidMain") { dependsOn(commonMain.get()) }
        nativeMain.get().dependsOn(nonAndroidMain)
        jvmMain.get().dependsOn(nonAndroidMain)

        val nonAndroidTest = create("nonAndroidTest") { dependsOn(commonTest.get()) }
        nativeTest.get().dependsOn(nonAndroidTest)
        jvmTest.get().dependsOn(nonAndroidTest)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

val localProperties = Properties().apply {
    rootProject.file("local.properties")
        .takeIf { it.isFile }
        ?.inputStream()
        ?.use { load(it) }
}

buildkonfig {
    packageName = "com.logsnag"
    exposeObjectWithName = "LogSnagSecrets"

    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "LogSnagToken",
            value = localProperties.getProperty("LOGSNAG_TOKEN"),
            nullable = true
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "LogSnagProject",
            value = localProperties.getProperty("LOGSNAG_PROJECT"),
            nullable = true,
        )
    }
}
