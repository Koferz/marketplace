plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                api(libs.coroutines.core)
                api(libs.coroutines.test)
                implementation(project(":yieldInsights-common"))
                implementation(project(":yieldInsights-repo-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":yieldInsights-stubs"))
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}
