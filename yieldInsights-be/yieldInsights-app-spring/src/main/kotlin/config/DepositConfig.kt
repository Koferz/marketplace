package config

import CorSettings
import MpLoggerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.yieldInsights.logging.jvm.mpLoggerLogback
import ru.otus.yieldInsights.biz.DepositProcessor

@Suppress("unused")
@Configuration
class DepositConfig {
    @Bean
    fun processor(corSettings: CorSettings) = DepositProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings(
        corSettings: CorSettings,
        processor: DepositProcessor,
    ) = AppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}