package config

import CorSettings
import MpLoggerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.yieldInsights.logging.jvm.mpLoggerLogback
import DepositProcessor
import DepositRepoStub
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit
import ru.otus.otuskotlin.yieldInsights.repo.inmemory.DepositRepoInMemory
import ru.otus.otuskotlin.yieldInsights.repo.postgresql.RepoDepositSql

@Suppress("unused")
@EnableConfigurationProperties(DepositConfigPostgres::class)
@Configuration
class DepositConfig(val postgresConfig: DepositConfigPostgres) {
    val logger: Logger = LoggerFactory.getLogger(DepositConfig::class.java)

    @Bean
    fun processor(corSettings: CorSettings) = DepositProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoDeposit = DepositRepoInMemory()

    @Bean
    fun prodRepo(): IRepoDeposit = RepoDepositSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }

    @Bean
    fun stubRepo(): IRepoDeposit = DepositRepoStub()


    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
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