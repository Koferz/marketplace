plugins {
    id("build-jvm")
    application
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.yieldInsights.app.rabbit.ApplicationKt")
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(libs.rabbitmq.client)
    implementation(libs.jackson.databind)
    implementation(libs.logback)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.datetime)

    implementation(project(":yieldInsights-common"))
    implementation(project(":yieldInsights-app-common"))
    implementation("ru.otus.otuskotlin.yieldInsights.libs:yieldInsights-lib-logging-logback")

    // v1 api
    implementation(project(":yieldInsights-be-api-v1-jackson"))
    implementation(project(":yieldInsights-api-v1-mappers"))

    // v2 api
    implementation(project(":yieldInsights-api-v2-kmp"))

    implementation(project(":yieldInsights-biz"))
    implementation(project(":yieldInsights-stubs"))

    testImplementation(libs.testcontainers.rabbitmq)
    testImplementation(kotlin("test"))
}