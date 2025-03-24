rootProject.name = "yieldInsights-libs"

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

include(":yieldInsights-lib-logging-common")
include(":yieldInsights-lib-logging-kermit")
include(":yieldInsights-lib-logging-logback")
include(":yieldInsights-lib-logging-socket")
