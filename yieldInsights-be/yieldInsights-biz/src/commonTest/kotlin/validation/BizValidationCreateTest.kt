package ru.otus.otuskotlin.yieldInsights.biz.validation

import models.Command
import kotlin.test.Test

// смотрим пример теста валидации, собранного из тестовых функций-оберток
class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: Command = Command.CREATE

    @Test fun correctDepositName() = validationDepositNameCorrect(command, processor)
    @Test fun trimDepositName() = validationDepositNameTrim(command, processor)
    @Test fun emptyDepositName() = validationDepositNameEmpty(command, processor)
    @Test fun badSymbolsDepositName() = validationDepositNameSymbols(command, processor)

    @Test fun correctDepositAmount() = validationDepositAmountCorrect(command, processor)
    @Test fun trimDepositAmount() = validationDepositAmountTrim(command, processor)
    @Test fun emptyDepositAmount() = validationDepositAmountEmpty(command, processor)
    @Test fun badSymbolsDepositAmount() = validationDepositAmountSymbols(command, processor)
}
