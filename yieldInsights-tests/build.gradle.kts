plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.otus.otuskotlin.yieldInsights.tests"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-deposit-v1.yaml").toString())
    set("spec-v2", specDir.file("specs-deposit-v2.yaml").toString())
}

tasks {
    arrayOf("build", "clean", "check").forEach {tsk ->
        create(tsk) {
            group = "build"
            dependsOn(subprojects.map {  it.getTasksByName(tsk,false)})
        }
    }
//    create("e2eTests") {
//        dependsOn(project(":yieldInsights-e2e-be").tasks.getByName("check"))
//    }
}
