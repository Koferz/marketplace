plugins {
    id("build-jvm")
}
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":yieldInsights-common"))
    implementation(project(":yieldInsights-common"))
    api(":yieldInsights-repo-common")

    implementation(libs.coroutines.core)
    implementation(libs.uuid)

    implementation(libs.db.postgres)
//  implementation(libs.db.hikari)
    implementation(libs.bundles.exposed)

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":yieldInsights-repo-tests"))
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)

}
