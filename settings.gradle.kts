rootProject.name = "yieldInsights"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

includeBuild("lessons")
includeBuild("yieldInsights-be")
includeBuild("yieldInsights-libs")