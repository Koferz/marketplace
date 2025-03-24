package ru.otus.otuskotlin.marketplace.app.common

import CorSettings
import ru.otus.yieldInsights.biz.DepositProcessor

interface IYieldInsightsAppSettings {
    val processor: DepositProcessor
    val corSettings: CorSettings
}
