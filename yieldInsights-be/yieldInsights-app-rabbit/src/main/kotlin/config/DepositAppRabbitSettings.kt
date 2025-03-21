package ru.otus.otuskotlin.yieldInsights.app.rabbit.config

import ru.otus.otuskotlin.marketplace.app.common.IYieldInsightsAppSettings

interface DepositAppRabbitSettings: IYieldInsightsAppSettings {
    val rabbit: RabbitConfig
    val controllersConfigV1: RabbitExchangeConfiguration
    val controllersConfigV2: RabbitExchangeConfiguration
}