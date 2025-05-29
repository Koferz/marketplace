package ru.otus.otuskotlin.yieldInsights.e2e.be.test

import ru.otus.otuskotlin.yieldInsights.blackbox.fixture.client.RabbitClient
import ru.otus.otuskotlin.yieldInsights.e2e.be.docker.RabbitDockerCompose
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.BaseFunSpec

class AccRabbitTest : BaseFunSpec(RabbitDockerCompose, {
    val client = RabbitClient(RabbitDockerCompose)

    testApiV1(client)
})
