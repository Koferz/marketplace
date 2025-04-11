package ru.otus.otuskotlin.yieldInsights.app.rabbit.config

import CorSettings
import ru.otus.otuskotlin.marketplace.app.common.IYieldInsightsAppSettings
import DepositProcessor

data class DepositAppSettings(
    override val corSettings: CorSettings = CorSettings(),
    override val processor: DepositProcessor = DepositProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
    override val controllersConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
): IYieldInsightsAppSettings, DepositAppRabbitSettings
