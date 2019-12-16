import org.gradle.kotlin.dsl.extra

object Versions {
    private const val kotlinVersion = "1.3.50"
    private const val ktorVersion = "1.2.5"
    private const val coroutinesVersion = "1.3.2"
    private const val serializationVersion = "0.10.0"
    private const val kodeinVersion = "6.4.1"

    object Shared {
        const val kodeinCore = "org.kodein.di:kodein-di-core:$kodeinVersion"
        const val kodeinErased = "org.kodein.di:kodein-di-erased:$kodeinVersion"

        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib"
        const val serializationRuntime =
            "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion"
        const val serialization =
            "org.jetbrains.kotlinx:kotlinx-serialization:$serializationVersion"

        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core$coroutinesVersion"
    }

    object Common {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-common:$kotlinVersion"

        const val ktorClientCore = "io.ktor:ktor-client-core:$ktorVersion"
        const val ktorClientJson = "io.ktor:ktor-client-json:$ktorVersion"
        const val ktorClientSerialization = "io.ktor:ktor-client-serialization:$ktorVersion"

        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion"
        const val serialization =
            "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion"
    }

    object Jvm {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"

        const val ktorAuth = "io.ktor:ktor-auth:$ktorVersion"
        const val ktorWebSockets = "io.ktor:ktor-websockets:$ktorVersion"
        const val ktorClientApache = "io.ktor:ktor-client-apache:$ktorVersion"
        const val ktorClientCore = "io.ktor:ktor-client-core-jvm:$ktorVersion"
        const val ktorClientJson = "io.ktor:ktor-client-json-jvm:$ktorVersion"
        const val ktorClientSerialization = "io.ktor:ktor-client-serialization-jvm:$ktorVersion"
        const val ktorServerNetty = "io.ktor:ktor-server-netty:$ktorVersion"
        const val ktorSerialization = "io.ktor:ktor-serialization:$ktorVersion"

        const val kodeinGeneric = "org.kodein.di:kodein-di-generic-jvm:$kodeinVersion"
        const val kodeinKtorServer = "org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodeinVersion"
        const val jetbrainsExposed = "org.jetbrains.exposed:exposed:0.17.7"
    }

    object Android {
        private const val lifecycleVersion = "2.2.0-alpha05"

        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    }
}