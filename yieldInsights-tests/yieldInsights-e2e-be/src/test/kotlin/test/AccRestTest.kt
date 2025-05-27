package ru.otus.otuskotlin.yieldInsights.e2e.be.test

import io.kotest.core.annotation.Ignored
import ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1.toV1
import ru.otus.otuskotlin.yieldInsights.blackbox.fixture.docker.DockerCompose
import ru.otus.otuskotlin.yieldInsights.e2e.be.docker.SpringDockerCompose
import ru.otus.otuskotlin.yieldInsights.e2e.be.docker.WiremockDockerCompose
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.BaseFunSpec
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.RestClient

enum class TestDebug {
    STUB, PROD, TEST
}

@Ignored
open class AccRestTestBaseFull(dockerCompose: DockerCompose, debug: TestDebug = TestDebug.STUB) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, prefix = "rest ", debug = debug.toV1())
})
@Ignored
open class AccRestTestBaseShort(dockerCompose: DockerCompose, debug: TestDebug = TestDebug.STUB) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
})

class AccRestWiremockTest : AccRestTestBaseFull(WiremockDockerCompose)

class AccRestSpringTest : AccRestTestBaseFull(SpringDockerCompose, debug = TestDebug.PROD)
