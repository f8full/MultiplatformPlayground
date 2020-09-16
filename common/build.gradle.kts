import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("dev.icerock.mobile.multiplatform-resources")
}

version = "1.0.0"

kotlin {
    android()
    ios {
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs =
                    listOf("-Xobjc-generics", "-Xopt-in=kotlin.RequiresOptIn")
            }
        }
    }
    cocoapods {
        summary = "AppPortfolio common code"
        homepage = "AppPortfolio home page"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":commonModel"))
                // Ktor-client for network requests
                implementation(Versions.Common.KTOR_CLIENT_CORE)
                api(Versions.Common.KTOR_LOGGING)
                implementation(Versions.Common.KTOR_CLIENT_JSON)
                implementation(Versions.Common.KTOR_CLIENT_SERIALIZATION)

                implementation(Versions.Common.COROUTINES_CORE)
                implementation(Versions.Common.SERIALIZATION)
                // Debug menu
                api(Versions.Common.BEAGLE_LOG)
                api(Versions.Common.BEAGLE_LOG_KTOR)
                // Multiplatform resources
                api(Versions.Common.MOKO_RESOURCES)
                // TODO remove this workaround after the 1.4.1 release
                implementation("dev.icerock.moko:parcelize:0.4.0")
                implementation("dev.icerock.moko:graphics:0.4.0")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Versions.Android.LIFECYCLE_EXTENSIONS)
                implementation(Versions.Android.LIVE_DATA)
                implementation(Versions.Android.VIEW_MODEL)

                // Ktor-client for network requests
                implementation(Versions.Android.KTOR_CLIENT)

                implementation(Versions.Jvm.KODEIN_GENERIC)
                implementation(Versions.Android.KODEIN_ANDROID_X)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Versions.iOS.KTOR_CLIENT)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core") {
                    version {
                        strictly(Versions.COROUTINES_VERSION)
                    }
                }
            }
        }
    }
}

android {
    // todo extract these
    compileSdkVersion(Versions.Android.SDK_VERSION)
    buildToolsVersion(Versions.Android.BUILD_TOOLS_VERSION)
    defaultConfig {
        minSdkVersion(Versions.Android.MINIMUM_SDK_VERSION)
        targetSdkVersion(Versions.Android.SDK_VERSION)
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    with(kotlinOptions) {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.halcyonmobile.multiplatformplayground" // required
}