plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    // Внутренние модели
    implementation(project(":yieldInsights-common"))
    implementation(project(":yieldInsights-app-common"))
    implementation("ru.otus.otuskotlin.yieldInsights.libs:yieldInsights-lib-logging-logback")

    // v1 api
    implementation(project(":yieldInsights-be-api-v1-jackson"))
    implementation(project(":yieldInsights-api-v1-mappers"))

    // v2 api
    implementation(project(":yieldInsights-api-v2-kmp"))

    // biz
    implementation(project(":yieldInsights-biz"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}