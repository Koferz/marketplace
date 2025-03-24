package config

import CorSettings
import ru.otus.otuskotlin.marketplace.app.common.IYieldInsightsAppSettings
import ru.otus.yieldInsights.biz.DepositProcessor

data class  AppSettings(
    override val corSettings: CorSettings,
    override val processor: DepositProcessor,
): IYieldInsightsAppSettings
