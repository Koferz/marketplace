package ru.otus.otuskotlin.yieldInsights.e2e.be.docker

import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.docker.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock", 8080, "docker-compose-wiremock.yml"
)
