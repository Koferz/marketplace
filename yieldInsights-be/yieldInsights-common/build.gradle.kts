plugins {
    id("build-kmp")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                api("ru.otus.otuskotlin.yieldInsights.libs:yieldInsights-lib-logging-common")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                //implementation(project(":yieldInsights-be-api-v1-jackson"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}