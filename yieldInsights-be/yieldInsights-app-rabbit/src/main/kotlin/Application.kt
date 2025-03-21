package ru.otus.otuskotlin.yieldInsights.app.rabbit

import CorSettings
import MpLoggerProvider
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.DepositAppSettings
import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.yieldInsights.app.rabbit.mappers.fromArgs
import ru.otus.otuskotlin.yieldInsights.logging.jvm.mpLoggerLogback


fun main(vararg args: String) = runBlocking {
    val appSettings = DepositAppSettings(
        rabbit = RabbitConfig.fromArgs(*args),
        corSettings = CorSettings(
            loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }
        )
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}
