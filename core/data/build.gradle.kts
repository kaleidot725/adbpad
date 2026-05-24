plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktlint)
}

repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
}

kotlin {
    jvm()
    jvmToolchain(17)

    sourceSets {
        val jvmMain by getting {
            kotlin.srcDirs("main/kotlin")
            resources.srcDirs("main/resources")
            dependencies {
                implementation(libs.adam)
                implementation(libs.kotlin.coroutines)
                implementation(libs.kotlin.result)
                implementation(libs.kotlin.serialization)
                implementation(project(":core:domain"))
            }
        }
        val jvmTest by getting {
            kotlin.srcDirs("test/kotlin")
            resources.srcDirs("test/resources")
            dependencies {
                implementation(libs.junit5)
                runtimeOnly(libs.junit.platform.launcher)
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
