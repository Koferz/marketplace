package ru.otus.otuskotlin.yieldInsights.biz.validation

import models.Command
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = Command.UPDATE

    @Test fun correctDepositName() = validationDepositNameCorrect(command, processor)
    @Test fun trimDepositName() = validationDepositNameTrim(command, processor)
    @Test fun emptyDepositName() = validationDepositNameEmpty(command, processor)
    @Test fun badSymbolsDepositName() = validationDepositNameSymbols(command, processor)

    @Test fun correctDepositAmount() = validationDepositAmountCorrect(command, processor)
    @Test fun trimDepositAmount() = validationDepositAmountTrim(command, processor)
    @Test fun emptyDepositAmount() = validationDepositAmountEmpty(command, processor)
    @Test fun badSymbolsDepositAmount() = validationDepositAmountSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
