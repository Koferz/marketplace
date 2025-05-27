//plugins {
//    kotlin("jvm") apply false
//}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}


group = "ru.otus.yieldInsights"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        mavenCentral()
    }
}

tasks {
//
//    create("check") {
//        group = "verification"
//        dependsOn(gradle.includedBuild("yieldInsights-be").task(":check"))
//    }

    create("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }
    val buildMigrations: Task by creating {
        dependsOn(gradle.includedBuild("yieldInsights-other").task(":buildImages"))
    }

    val buildImages: Task by creating {
        dependsOn(buildMigrations)
        dependsOn(gradle.includedBuild("yieldInsights-be").task(":buildImages"))
    }
    val e2eTests: Task by creating {
        dependsOn(buildImages)
        dependsOn(gradle.includedBuild("yieldInsights-tests").task(":e2eTests"))
        mustRunAfter(buildImages)
    }

    create("check") {
        group = "verification"
        dependsOn(buildImages)
        dependsOn(e2eTests)
    }
}