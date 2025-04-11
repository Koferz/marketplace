package ru.otus.otuskotlin.yieldInsights.biz.validation

import CorSettings
import models.Command
import DepositProcessor

abstract class BaseBizValidationTest {
    protected abstract val command: Command
    private val settings by lazy { CorSettings() }
    protected val processor by lazy { DepositProcessor(settings) }
}
