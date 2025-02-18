plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation(project(":yieldInsights-be-api-v1-jackson"))
    implementation(project(":yieldInsights-common"))

    testImplementation(kotlin("test-junit"))
}