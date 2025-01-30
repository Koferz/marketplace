rootProject.name = "marketplace"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

includeBuild("lessons")
includeBuild("marketplace-be")