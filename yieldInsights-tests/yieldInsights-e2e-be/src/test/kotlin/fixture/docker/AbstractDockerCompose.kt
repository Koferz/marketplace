package ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.docker

import co.touchlab.kermit.Logger
import io.ktor.http.*
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.otus.otuskotlin.yieldInsights.blackbox.fixture.docker.DockerCompose
import java.io.File

private val log = Logger

/**
 * apps - список приложений в docker-compose. Первое приложение - "главное", его url возвращается как inputUrl
 * (например ваш сервис при работе по rest или брокер сообщений при работе с брокером)
 * dockerComposeNames - имена docker-compose файлов (относительно ok-marketplace-acceptance/docker-compose)
 */
abstract class AbstractDockerCompose(
    private val apps: List<AppInfo>,
    private val dockerComposeNames: List<String>,
) : DockerCompose {
    constructor(
        service: String,
        port: Int,
        vararg dockerComposeName: String
    ): this(
        listOf(AppInfo(service, port)),
        dockerComposeName.toList()
    )
    private fun getComposeFiles(): List<File> = dockerComposeNames.map {
        val file = File("docker-compose/$it")
        if (!file.exists()) throw IllegalArgumentException("file $it not found!")
        file
    }

    private val compose by lazy {
        ComposeContainer(getComposeFiles()).apply {
            apps.forEach { (service, port) ->
                withExposedService(
                    service,
                    port,
                )
                waitingFor(service, Wait.forHealthcheck())
            }
        }
    }

    override fun start() {
        kotlin.runCatching { compose.start() }.onFailure {
            log.e { "Failed to start $dockerComposeNames" }
            throw it
        }

        log.w("\n=========== $dockerComposeNames started =========== \n")
        apps.forEachIndexed { index, _ ->
            log.i { "Started docker-compose with App at: ${getUrl(index)}" }
        }
    }

    override fun stop() {
        compose.close()
        log.w("\n=========== $dockerComposeNames complete =========== \n")
    }

    override fun clearDb() {
        log.w("===== clearDb =====")
    }

    override val inputUrl: URLBuilder
        get() = getUrl(0)

    fun getUrl(no: Int) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = apps[no].let { compose.getServiceHost(it.service, it.port) },
        port = apps[no].let { compose.getServicePort(it.service, it.port) },
    )
    data class AppInfo(
        val service: String,
        val port: Int,
    )

}
