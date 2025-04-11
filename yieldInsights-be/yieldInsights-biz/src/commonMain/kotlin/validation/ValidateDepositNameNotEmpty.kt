package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.validateDepositNameNotEmpty(title: String) = worker {
    this.title = title
    on { depositValidating.depositName.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "depositName",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
