package ru.otus.otuskotlin.yieldInsights.biz.validation

import models.Command
import kotlin.test.Test

class BizValidationClosingTest: BaseBizValidationTest() {
    override val command = Command.CLOSING

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}
