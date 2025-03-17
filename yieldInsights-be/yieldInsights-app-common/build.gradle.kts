plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(libs.coroutines.core)
                implementation(libs.kotlinx.datetime)

                // transport models
                implementation(project(":yieldInsights-common"))
                implementation(project(":yieldInsights-api-log1"))
                implementation(project(":yieldInsights-be-api-v1-jackson"))
                implementation(project(":yieldInsights-api-v1-mappers"))
                implementation(project(":yieldInsights-biz"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.coroutines.test)
                implementation(project(":yieldInsights-be-api-v1-jackson"))
                implementation(project(":yieldInsights-api-v1-mappers"))
                implementation(project(":yieldInsights-api-log1"))
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