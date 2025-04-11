package ru.otus.otuskotlin.marketplace.app.common

import CorSettings
import DepositProcessor

interface IYieldInsightsAppSettings {
    val processor: DepositProcessor
    val corSettings: CorSettings
}
