rootProject.name = "yieldInsights-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

//plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
//}

// Включает вот такую конструкцию
//implementation(projects.m2l5Gradle.sub1.ssub1)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":yieldInsights-be-api-v1-jackson")
include(":yieldInsights-api-v1-mappers")
include(":yieldInsights-api-v2-kmp")
include(":yieldInsights-common")
include(":yieldInsights-app-spring")
include(":yieldInsights-app-common")
include(":yieldInsights-app-rabbit")
include(":yieldInsights-api-log1")
include(":yieldInsights-biz")
include(":yieldInsights-stubs")

